
public class PaymentProcessor implements PaymentHandler {
    private int tc = 0;
    private double[] amounts = new double[1000];
    private String[] methods = new String[1000];
    private String[] stats = new String[1000];
    private long[] times = new long[1000];
    private static final double TAX_RATE = 0.085;
    private static final double DELIVERY_SURCHARGE = 2.5;
    private static final double LOYALTY_DISCOUNT_THRESHOLD = 10;
    private static final double LOYALTY_DISCOUNT_PERCENT = 0.15;
    private PaymentMethod currentPaymentMethod;

    public PaymentProcessor() {
        for (int i = 0; i < 1000; i++) {
            amounts[i] = 0;
            methods[i] = "";
            stats[i] = "PENDING";
            times[i] = 0;
        }
    }

    public int processPayment(int orderId, String paymentMethod, double amount) {
        if (orderId < 0 || amount <= 0 || paymentMethod == null) {
            return -1; 
        }

        OrderManager om = OrderManager.getInstance();
        
        if (!om.validate(orderId)) {
            return 0; 
        }

        if (paymentMethod.equalsIgnoreCase("CREDIT_CARD")) {
            currentPaymentMethod = new CreditCardPayment();
            if (validateCreditCard(amount)) {
                double result = currentPaymentMethod.processPayment(amount);
                if (result > 0 && processCardPayment(amount)) {
                    recordPayment(orderId, result, paymentMethod);
                    return 1; // SUCCESS
                } else {
                    return 2; // CARD_DECLINED
                }
            } else {
                return 3; // INVALID_AMOUNT
            }
        } else if (paymentMethod.equalsIgnoreCase("CASH")) {
            currentPaymentMethod = new CashPayment();
            try {
                double result = currentPaymentMethod.processPayment(amount); // May throw!
                recordPayment(orderId, result, paymentMethod);
                return 1; // SUCCESS
            } catch (IllegalArgumentException e) {
                return 3; // INVALID_AMOUNT 
            }
        } else if (paymentMethod.equalsIgnoreCase("PAYPAL")) {
            currentPaymentMethod = new PayPalPayment();
            if (validatePayPal(amount)) {
                double result = currentPaymentMethod.processPayment(amount);
                if (result < 0) {
                    return 4; // PAYPAL_ERROR
                }
                recordPayment(orderId, result, paymentMethod);
                return 1; // SUCCESS
            } else {
                return 4; // PAYPAL_ERROR
            }
        }

        return -2; 
    }

    private boolean validateCreditCard(double amount) {
        return amount > 0 && amount < 1000;
    }

    private boolean processCardPayment(double amount) {
        return Math.random() > 0.05;
    }

    private boolean validatePayPal(double amount) {
        return amount > 0;
    }
    
    @Override
    public boolean validateCash(double amount) {
        return amount > 0;
    }

    private void recordPayment(int orderId, double amount, String method) {
        amounts[tc] = amount;
        methods[tc] = method;
        stats[tc] = "PROCESSED";
        times[tc] = System.currentTimeMillis();
        tc++;
    }

    public double calculateTotal(int orderId) {
        OrderManager om = OrderManager.getInstance();
        Pizza p = om.getOrder(orderId);
        Customer c = om.getCustomer(orderId);

        if (p == null || c == null) {
            return 0;
        }

        double subtotal = p.calculateFinalPrice();
        double tax = subtotal * TAX_RATE;
        double delivery = DELIVERY_SURCHARGE;
        double discount = 0;

        if (c.getLc() >= LOYALTY_DISCOUNT_THRESHOLD) {
            discount = subtotal * LOYALTY_DISCOUNT_PERCENT;
        }

        return subtotal + tax + delivery - discount;
    }

    public String generateReceipt(int orderId) {
        OrderManager om = OrderManager.getInstance();
        Pizza p = om.getOrder(orderId);
        Customer c = om.getCustomer(orderId);

        if (p == null || c == null) {
            return "INVALID_ORDER";
        }

        String receipt = "";
        receipt = receipt + "================================\n";
        receipt = receipt + "       PIZZA SHOP RECEIPT       \n";
        receipt = receipt + "================================\n";
        receipt = receipt + "Order #" + orderId + "\n";
        receipt = receipt + "Customer: " + c.getN() + "\n";
        receipt = receipt + "Date: " + new java.util.Date() + "\n";
        receipt = receipt + "--------------------------------\n";
        receipt = receipt + "Item: " + p.getN() + "\n";
        receipt = receipt + "Size: " + p.getSz().getS() + "\n";
        receipt = receipt + "Base Price: $" + String.format("%.2f", p.getBp()) + "\n";
        receipt = receipt + "Subtotal: $" + String.format("%.2f", p.calculateFinalPrice()) + "\n";
        receipt = receipt + "Tax (8.5%): $" + String.format("%.2f", p.calculateFinalPrice() * TAX_RATE) + "\n";
        receipt = receipt + "Delivery: $" + String.format("%.2f", DELIVERY_SURCHARGE) + "\n";

        if (c.getLc() >= LOYALTY_DISCOUNT_THRESHOLD) {
            double discount = p.calculateFinalPrice() * LOYALTY_DISCOUNT_PERCENT;
            receipt = receipt + "Loyalty Discount: -$" + String.format("%.2f", discount) + "\n";
        }

        receipt = receipt + "--------------------------------\n";
        receipt = receipt + "TOTAL: $" + String.format("%.2f", calculateTotal(orderId)) + "\n";
        receipt = receipt + "================================\n";

        return receipt;
    }

    public boolean refundPayment(int transactionIndex, String reason) {
        if (transactionIndex < 0 || transactionIndex >= tc) {
            return false;
        }

        if (amounts[transactionIndex] <= 0) {
            return false;
        }

        stats[transactionIndex] = "REFUNDED";

        if (reason.equalsIgnoreCase("CUSTOMER_REQUEST")) {
        } else if (reason.equalsIgnoreCase("ORDER_CANCELLED")) {
        } else if (reason.equalsIgnoreCase("PAYMENT_ERROR")) {
        }

        return true;
    }

    public String getTransactionHistory() {
        String history = "";
        for (int i = 0; i < tc; i++) {
            history = history + "Trans #" + i + ": ";
            history = history + methods[i] + " ";
            history = history + "$" + amounts[i] + " ";
            history = history + stats[i] + "\n";
        }
        return history;
    }

    public int getTotalTransactions() {
        return tc;
    }
    
    @Override
    public boolean sendEmailReceipt(int orderId, String emailAddress) {
        return false; 
    }
    
    @Override
    public void sendPaymentConfirmation(int orderId) {
    }
    
    @Override
    public String exportToCSV() {
        return ""; 
    }
    
    @Override
    public void exportTransactionsToFile(String filename) {
    }
    
    @Override
    public String generateMonthlyReport(int month, int year) {
        return ""; 
    }
    
    @Override
    public double getTotalRevenue() {
        double total = 0;   
        for (int i = 0; i < tc; i++) {
            if (stats[i].equals("PROCESSED")) {
                total += amounts[i];
            }
        }
        return total;
    }
}
