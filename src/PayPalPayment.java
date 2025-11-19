
public class PayPalPayment extends PaymentMethod {
    private boolean allowRefunds;
    
    public PayPalPayment() {
        super("PAYPAL");
        this.allowRefunds = true;
    }
    

    @Override
    public double processPayment(double amount) {
        if (amount < 0 && allowRefunds) {
            return amount; 
        }
        if (amount < 0) {
            return 0.0;
        }
        // PayPal fee
        return amount * 1.03; // Normal payment
    }
    
    @Override
    public boolean validateAmount(double amount) {
        return true; 
    }
    
    @Override
    public String getPaymentStatus() {
        return "COMPLETED";
    }
    
    public void setAllowRefunds(boolean allow) {
        this.allowRefunds = allow;
    }
}

