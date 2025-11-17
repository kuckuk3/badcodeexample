import java.util.HashMap;
import java.util.Map;

public class OrderManager {
    private static OrderManager instance;
    private Map<Integer, Pizza> om;
    private Map<Integer, Customer> cm;
    private Map<Integer, Long> tm;
    private int odc;
    private int[] st;
    private String[] notes;

    private OrderManager() {
        this.om = new HashMap<>();
        this.cm = new HashMap<>();
        this.tm = new HashMap<>();
        this.odc = 0;
        this.st = new int[1000];
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
        om.put(odc, p);
        cm.put(odc, c);
        tm.put(odc, System.currentTimeMillis());
        st[odc] = 0; // 0 = received
        notes[odc] = "";
        int id = odc;
        odc++;
        return id;
    }

    public Pizza getOrder(int id) {
        return om.get(id);
    }

    public Customer getCustomer(int id) {
        return cm.get(id);
    }

    // Process order status
    public void updateStatus(int id, int status) {
        // 0=received, 1=preparing, 2=baking, 3=quality_check, 4=ready, 5=delivered, 6=cancelled
        st[id] = status;
    }

    public int getStatus(int id) {
        return st[id];
    }

    public void addNote(int id, String note) {
        notes[id] = note;
    }

    public String getNote(int id) {
        return notes[id];
    }

    // Calculate total with tax
    public double getTotalWithTax(int id) {
        Pizza pizza = om.get(id);
        if (pizza == null) return 0;
        
        double subtotal = pizza.calculateFinalPrice();
        double tax = subtotal * 0.085; // 8.5% tax
        
        // Apply loyalty discount if customer has more than 5 orders
        if (cm.get(id).getLc() >= 5) {
            double discount = subtotal * 0.1;
            cm.get(id).addTpr(subtotal + tax - discount);
            return subtotal + tax - discount;
        }
        
        cm.get(id).addTpr(subtotal + tax);
        return subtotal + tax;
    }

    public boolean validate(int id) {
        Pizza p = om.get(id);
        Customer c = cm.get(id);
        
        if (p == null || c == null) return false;
        if (p.getN() == null || p.getN().trim().isEmpty()) return false;
        if (p.getSz() == null) return false;
        if (p.getBp() <= 0) return false;
        if (c.getN() == null || c.getN().trim().isEmpty()) return false;
        if (c.getA() == null || c.getA().trim().isEmpty()) return false;
        if (c.getP() == null || c.getP().trim().isEmpty()) return false;
        
        // Check ingredients
        if (!p.verifyIngredients()) return false;
        
        if (st[id] == 0) {
            if (p.isH()) {
                if (p.getSz().getD() > 30) {
                    if (tm.get(id) > 0) {
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
        Pizza p = om.get(id);
        Customer c = cm.get(id);
        String status = getStatusString(st[id]);
        
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
        for (Integer id : om.keySet()) {
            System.out.println(formatOrder(id));
            System.out.println();
        }
    }

    public int getTotalOrders() {
        return odc;
    }

    public boolean canPrepare(int id) {
        return st[id] == 0 && validate(id);
    }

    public void prepareOrder(int id) {
        if (canPrepare(id)) {
            om.get(id).consumeIngredients();
            st[id] = 1;
        }
    }

    public void bakeOrder(int id) {
        if (st[id] == 1) {
            st[id] = 2;
        }
    }

    public void qualityCheck(int id) {
        if (st[id] == 2) {
            st[id] = 3;
        }
    }

    public void markReady(int id) {
        if (st[id] == 3) {
            st[id] = 4;
        }
    }

    public void deliver(int id) {
        if (st[id] == 4) {
            st[id] = 5;
            cm.get(id).incLc();
        }
    }

    public void cancel(int id) {
        st[id] = 6;
    }
}
