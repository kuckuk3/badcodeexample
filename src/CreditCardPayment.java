
public class CreditCardPayment extends PaymentMethod {
    private boolean cardValid;
    
    public CreditCardPayment() {
        super("CREDIT_CARD");
        this.cardValid = true;
    }
    

    @Override
    public double processPayment(double amount) {
        if (amount < 0) {
            return 0.0; 
        }
        if (!cardValid) {
            return 0.0; 
        }
        return amount * 1.02; 
    }
    
    @Override
    public boolean validateAmount(double amount) {
        return amount >= 0;
    }
    
    @Override
    public String getPaymentStatus() {
        return cardValid ? "APPROVED" : "DECLINED";
    }
    
    public void setCardValid(boolean valid) {
        this.cardValid = valid;
    }
}

