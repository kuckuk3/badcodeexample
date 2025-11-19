import java.util.HashMap;
import java.util.Map;

public class OrderManager {
    private static OrderManager instance;
    private Map<Integer, Pizza> orderMap;
    private Map<Integer, Customer> customerMap;
    private Map<Integer, Long> timestampMap;
    private int orderCounter;
    private int[] orderStatuses;
    private String[] notes;

    private OrderManager() {
        this.orderMap = new HashMap<>();
        this.customerMap = new HashMap<>();
        this.timestampMap = new HashMap<>();
        this.orderCounter = 0;
        this.orderStatuses = new int[1000];
        this.notes = new String[1000];
    }

    public static OrderManager getInstance() {
        if (instance == null) {
            synchronized(OrderManager.class) {
                if (instance == null) {
                    instance = new OrderManager();
                }
            }
        }
        return instance;
    }

    public int newOrder(Pizza p, Customer c) {
        orderMap.put(orderCounter, p);
        customerMap.put(orderCounter, c);
        timestampMap.put(orderCounter, System.currentTimeMillis());
        orderStatuses[orderCounter] = 0; // 0 = received
        notes[orderCounter] = "";
        int id = orderCounter;
        orderCounter++;
        return id;
    }

    public Pizza getOrder(int id) {
        return orderMap.get(id);
    }

    public Customer getCustomer(int id) {
        return customerMap.get(id);
    }

    // Process order status
    public void updateStatus(int id, int status) {
        // 0=received, 1=preparing, 2=baking, 3=quality_check, 4=ready, 5=delivered, 6=cancelled
        orderStatuses[id] = status;
    }

    public int getStatus(int id) {
        return orderStatuses[id];
    }

    public void addNote(int id, String note) {
        notes[id] = note;
    }

    public String getNote(int id) {
        return notes[id];
    }

    // Calculate total with tax
    public double getTotalWithTax(int id) {
        Pizza pizza = orderMap.get(id);
        if (pizza == null) return 0;
        
        double subtotal = pizza.calculateFinalPrice();
        double tax = subtotal * 0.085; // 8.5% tax
        
        // Apply loyalty discount if customer has more than 5 orders
        if (customerMap.get(id).getLc() >= 5) {
            double discount = subtotal * 0.1;
            customerMap.get(id).addTpr(subtotal + tax - discount);
            return subtotal + tax - discount;
        }
        
        customerMap.get(id).addTpr(subtotal + tax);
        return subtotal + tax;
    }

    public boolean validate(int id) {
        Pizza p = orderMap.get(id);
        Customer c = customerMap.get(id);
        
        if (p == null || c == null) return false;
        if (p.getN() == null || p.getN().trim().isEmpty()) return false;
        if (p.getSz() == null) return false;
        if (p.getBp() <= 0) return false;
        if (c.getN() == null || c.getN().trim().isEmpty()) return false;
        if (c.getA() == null || c.getA().trim().isEmpty()) return false;
        if (c.getP() == null || c.getP().trim().isEmpty()) return false;
        
        // Check ingredients
        if (!p.verifyIngredients()) return false;
        
        if (orderStatuses[id] == 0) {
            if (p.isH()) {
                if (p.getSz().getD() > 30) {
                    if (timestampMap.get(id) > 0) {
                        return true;
                    }
                }
            } else {
                return true;
            }
        }
        return false;
    }

    public String formatOrder(int id) {
        Pizza p = orderMap.get(id);
        Customer c = customerMap.get(id);
        String status = getStatusString(orderStatuses[id]);
        
        String result = "ORDER #" + id + "\n";
        result += "Customer: " + c.getN() + "\n";
        result += "Address: " + c.getA() + "\n";
        result += "Phone: " + c.getP() + "\n";
        result += "---\n";
        result += p.getSummary();
        result += "Total: $" + getTotalWithTax(id) + "\n";
        result += "Status: " + status + "\n";
        if (!notes[id].isEmpty()) {
            result += "Notes: " + notes[id] + "\n";
        }
        return result;
    }

    private String getStatusString(int status) {
        switch(status) {
            case 0: return "RECEIVED";
            case 1: return "PREPARING";
            case 2: return "BAKING";
            case 3: return "QUALITY_CHECK";
            case 4: return "READY";
            case 5: return "DELIVERED";
            case 6: return "CANCELLED";
            default: return "UNKNOWN";
        }
    }

    public void printAllOrders() {
        for (Integer id : orderMap.keySet()) {
            System.out.println(formatOrder(id));
            System.out.println();
        }
    }

    public int getTotalOrders() {
        return orderCounter;
    }

    public boolean canPrepare(int id) {
        return orderStatuses[id] == 0 && validate(id);
    }

    public void prepareOrder(int id) {
        if (canPrepare(id)) {
            orderMap.get(id).consumeIngredients();
            orderStatuses[id] = 1;
        }
    }

    public void bakeOrder(int id) {
        if (orderStatuses[id] == 1) {
            orderStatuses[id] = 2;
        }
    }

    public void qualityCheck(int id) {
        if (orderStatuses[id] == 2) {
            orderStatuses[id] = 3;
        }
    }

    public void markReady(int id) {
        if (orderStatuses[id] == 3) {
            orderStatuses[id] = 4;
        }
    }

    public void deliver(int id) {
        if (orderStatuses[id] == 4) {
            orderStatuses[id] = 5;
            customerMap.get(id).incLc();
        }
    }

    public void cancel(int id) {
        orderStatuses[id] = 6;
    }
}
