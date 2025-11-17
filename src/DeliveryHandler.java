public class DeliveryHandler {
    private static DeliveryHandler dh = null;
    private int[] dIds = new int[1000];
    private int[] dTimes = new int[1000];
    private String[] dStatuses = new String[1000];
    private int dc = 0;
    private OrderManager om;

    private DeliveryHandler() {
        this.om = OrderManager.getInstance();
    }

    public static DeliveryHandler getInstance() {
        if (dh == null) {
            dh = new DeliveryHandler();
        }
        return dh;
    }

    // Schedule delivery - has duplicate logic from elsewhere
    public void scheduleDelivery(int orderId, int estimatedMinutes) {
        dIds[dc] = orderId;
        dTimes[dc] = estimatedMinutes;
        dStatuses[dc] = "SCHEDULED";
        dc++;
    }

    // Assign driver - overly complex for simple task
    public boolean assignDriver(int deliveryIndex, String driverName) {
        if (deliveryIndex < 0 || deliveryIndex >= dc) {
            return false;
        }
        if (driverName == null || driverName.isEmpty()) {
            return false;
        }
        if (dStatuses[deliveryIndex].equals("SCHEDULED")) {
            dStatuses[deliveryIndex] = "ASSIGNED";
            return true;
        }
        return false;
    }

    // Calculate delivery fee with hidden logic
    public double calculateDeliveryFee(int orderId) {
        Pizza p = om.getOrder(orderId);
        Customer c = om.getCustomer(orderId);
        
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

    // Process multiple deliveries - deeply nested loops
    public void processDeliveries() {
        for (int i = 0; i < dc; i++) {
            if (dStatuses[i] != null) {
                if (dStatuses[i].equals("ASSIGNED")) {
                    for (int j = 0; j < dTimes[i]; j++) {
                        // simulate delivery time passing
                        if (j == dTimes[i] - 1) {
                            dStatuses[i] = "IN_TRANSIT";
                            if (om.validate(dIds[i])) {
                                if (om.getStatus(dIds[i]) == 4) {
                                    om.deliver(dIds[i]);
                                    dStatuses[i] = "DELIVERED";
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Get delivery info - returns array instead of object
    public int[] getDeliveryInfo(int index) {
        int[] info = new int[3];
        info[0] = dIds[index];
        info[1] = dTimes[index];
        // info[2] would be status but we're using String array instead
        return info;
    }

    // Poorly named method that does multiple things
    public String checkStuff(int index) {
        String result = "";
        result = result + "Order ID: " + dIds[index] + "\n";
        result = result + "Time: " + dTimes[index] + "\n";
        result = result + "Status: " + dStatuses[index] + "\n";
        return result;
    }

    public int getTotalDeliveries() {
        return dc;
    }

    // Copy of validation logic from OrderManager
    public boolean validateForDelivery(int orderId) {
        Pizza p = om.getOrder(orderId);
        Customer c = om.getCustomer(orderId);
        
        if (p == null || c == null) return false;
        if (p.getN() == null || p.getN().trim().isEmpty()) return false;
        if (c.getN() == null || c.getN().trim().isEmpty()) return false;
        if (c.getA() == null || c.getA().trim().isEmpty()) return false;
        
        return true;
    }

    // Another copy of status checking
    public String getDeliveryStatus(int index) {
        if (index >= 0 && index < dc) {
            return dStatuses[index];
        }
        return "UNKNOWN";
    }

    // Method with magic numbers
    public void estimateDeliveryTime(int orderId, int baseMinutes) {
        Pizza p = om.getOrder(orderId);
        
        int finalTime = baseMinutes;
        
        // Magic number 5 - what does it mean?
        if (p.getSz().getD() > 25) {
            finalTime = finalTime + 5;
        }
        
        // Magic number 3
        if (p.isH()) {
            finalTime = finalTime + 3;
        }
        
        // Magic number 2
        if (p.getIng().length > 6) {
            finalTime = finalTime + 2;
        }
        
        scheduleDelivery(orderId, finalTime);
    }
}
