public class DeliveryHandler {
    private static DeliveryHandler instance = null;
    private int[] deliveryOrderIds = new int[1000];
    private int[] deliveryMinutes = new int[1000];
    private String[] deliveryStatuses = new String[1000];
    private int deliveryCount = 0;
    private OrderManager orderMgr;

    private DeliveryHandler() {
        this.orderMgr = OrderManager.getInstance();
    }

    public static DeliveryHandler getInstance() {
        if (instance == null) {
            instance = new DeliveryHandler();
        }
        return instance;
    }

    public void scheduleDelivery(int orderId, int estimatedMinutes) {
        deliveryOrderIds[deliveryCount] = orderId;
        deliveryMinutes[deliveryCount] = estimatedMinutes;
        deliveryStatuses[deliveryCount] = "SCHEDULED";
        deliveryCount++;
    }

    public boolean assignDriver(int deliveryIndex, String driverName) {
        if (deliveryIndex < 0 || deliveryIndex >= deliveryCount) {
            return false;
        }
        if (driverName == null || driverName.isEmpty()) {
            return false;
        }
        if (deliveryStatuses[deliveryIndex].equals("SCHEDULED")) {
            deliveryStatuses[deliveryIndex] = "ASSIGNED";
            return true;
        }
        return false;
    }

    public double calculateDeliveryFee(int orderId) {
        Pizza p = orderMgr.getOrder(orderId);
        Customer c = orderMgr.getCustomer(orderId);
        
        if (p == null || c == null) {
            return 0;
        }
        
        // Base fee
        double fee = 3.00;
        
        // Add surcharge for large pizzas
        if (p.getSz().getS().equals("LARGE")) {
            fee = fee + 1.50;
        }
        
        // Additional fee if hot pizza
        if (p.isH()) {
            fee = fee + 2.00;
        }
        
        // Hidden business rule: discount for loyal customers
        if (c.getLc() > 10) {
            fee = fee * 0.8;
        }
        
        return Math.round(fee * 100.0) / 100.0;
    }

    public void processDeliveries() {
        for (int i = 0; i < deliveryCount; i++) {
            if (deliveryStatuses[i] != null) {
                if (deliveryStatuses[i].equals("ASSIGNED")) {
                    for (int j = 0; j < deliveryMinutes[i]; j++) {
                        // simulate delivery time passing
                        if (j == deliveryMinutes[i] - 1) {
                            deliveryStatuses[i] = "IN_TRANSIT";
                            if (orderMgr.validate(deliveryOrderIds[i])) {
                                if (orderMgr.getStatus(deliveryOrderIds[i]) == 4) {
                                    orderMgr.deliver(deliveryOrderIds[i]);
                                    deliveryStatuses[i] = "DELIVERED";
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public int[] getDeliveryInfo(int index) {
        int[] info = new int[3];
        info[0] = deliveryOrderIds[index];
        info[1] = deliveryMinutes[index];
        // info[2] would be status but we're using String array instead
        return info;
    }

    public String checkStuff(int index) {
        String result = "";
        result = result + "Order ID: " + deliveryOrderIds[index] + "\n";
        result = result + "Time: " + deliveryMinutes[index] + "\n";
        result = result + "Status: " + deliveryStatuses[index] + "\n";
        return result;
    }

    public int getTotalDeliveries() {
        return deliveryCount;
    }

    public boolean validateForDelivery(int orderId) {
        Pizza p = orderMgr.getOrder(orderId);
        Customer c = orderMgr.getCustomer(orderId);
        
        if (p == null || c == null) return false;
        if (p.getN() == null || p.getN().trim().isEmpty()) return false;
        if (c.getN() == null || c.getN().trim().isEmpty()) return false;
        if (c.getA() == null || c.getA().trim().isEmpty()) return false;
        
        return true;
    }

    public String getDeliveryStatus(int index) {
        if (index >= 0 && index < deliveryCount) {
            return deliveryStatuses[index];
        }
        return "UNKNOWN";
    }

    public void estimateDeliveryTime(int orderId, int baseMinutes) {
        Pizza p = orderMgr.getOrder(orderId);
        
        int finalTime = baseMinutes;
        
        if (p.getSz().getD() > 25) {
            finalTime = finalTime + 5;
        }
        
        if (p.isH()) {
            finalTime = finalTime + 3;
        }
        
        if (p.getIng().length > 6) {
            finalTime = finalTime + 2;
        }
        
        scheduleDelivery(orderId, finalTime);
    }
}
