public interface PaymentHandler {
    int processPayment(int orderId, String paymentMethod, double amount);
    double calculateTotal(int orderId);
    
    String generateReceipt(int orderId);
    
    boolean refundPayment(int transactionIndex, String reason);
    
    boolean validateCreditCard(double amount);
    boolean validatePayPal(double amount);
    boolean validateCash(double amount);
    
    String getTransactionHistory();
    int getTotalTransactions();
    
    boolean sendEmailReceipt(int orderId, String emailAddress);
    void sendPaymentConfirmation(int orderId);
    
    String exportToCSV();
    void exportTransactionsToFile(String filename);
    
    String generateMonthlyReport(int month, int year);
    double getTotalRevenue();
}

