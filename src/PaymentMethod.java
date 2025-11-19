
public abstract class PaymentMethod {
    protected String methodName;
    
    public PaymentMethod(String methodName) {
        this.methodName = methodName;
    }
    
    
    public abstract double processPayment(double amount);
    
   
    public boolean validateAmount(double amount) {
        return amount >= 0;
    }
    
    public String getMethodName() {
        return methodName;
    }
    
    // Get payment status - should return a status string
    public abstract String getPaymentStatus();
}

