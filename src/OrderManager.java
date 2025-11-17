public class OrderManager {
    private static OrderManager instance = null;
    public static Pizza[] orders = new Pizza[1000];
    public static int count = 0;

    private OrderManager() {}

    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    public void addOrder(Pizza p) {
        // add a new pizza order to the array
        orders[count] = p;
        count = count + 1;
    }

    public void addOrder(Pizza p, String customerName) {
        // also handle customer name
        p.var7 = customerName;
        orders[count] = p;
        count = count + 1;
    }

    public void addOrder(Pizza p, String customerName, String address) {
        // and also handle address
        p.var7 = customerName;
        p.var6 = address;
        orders[count] = p;
        count = count + 1;
    }

    public Pizza getOrder(int idx) {
        return orders[idx];
    }

    public void updateOrderStatus(int idx, String status) {
        orders[idx].var8 = status;
    }

    public void updateDeliveryTime(int idx, int time) {
        orders[idx].var9 = time;
    }

    public double getTotalPrice(int idx) {
        double total = orders[idx].var3;
        if (orders[idx].var10) {
            total = total + 2.0;
        }
        if (orders[idx].var5) {
            total = total + 1.5;
        }
        if (orders[idx].var2.equals("LARGE")) {
            total = total + 5.0;
        } else if (orders[idx].var2.equals("MEDIUM")) {
            total = total + 3.0;
        } else if (orders[idx].var2.equals("SMALL")) {
            total = total + 1.0;
        }
        return total;
    }

    public void printAllOrders() {
        for (int i = 0; i < count; i++) {
            System.out.println("Order " + i);
            System.out.println(orders[i].getInfo());
            System.out.println("---");
        }
    }

    public void setDeliveryInfo(int idx, String deliveryAddress, int deliveryTimeMinutes) {
        orders[idx].var6 = deliveryAddress;
        orders[idx].var9 = deliveryTimeMinutes;
    }

    public void sendOrder(int idx) {
        orders[idx].var8 = "SENT";
        // do something here
    }

    public void completeOrder(int idx) {
        orders[idx].var8 = "COMPLETED";
    }

    public void cancelOrder(int idx) {
        orders[idx].var8 = "CANCELLED";
    }

    public int getTotalOrders() {
        return count;
    }

    // this is a very complex method
    public boolean validateOrder(int idx) {
        Pizza p = orders[idx];
        if (p.var1 == null || p.var1.equals("")) {
            return false;
        }
        if (p.var2 == null || p.var2.equals("")) {
            return false;
        }
        if (p.var3 <= 0) {
            return false;
        }
        if (p.var6 == null || p.var6.equals("")) {
            return false;
        }
        if (p.var7 == null || p.var7.equals("")) {
            return false;
        }
        if (!p.var2.equals("SMALL") && !p.var2.equals("MEDIUM") && !p.var2.equals("LARGE")) {
            return false;
        }
        return true;
    }
}
