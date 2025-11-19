
public class CashPayment extends PaymentMethod {
    
    public CashPayment() {
        super("CASH");
    }

    @Override
    public double processPayment(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cash payment cannot be negative!"); 
        }
        if (amount == 0) {
            throw new IllegalArgumentException("Cash payment cannot be zero!"); 
        }
        return amount; 
    }
    

    @Override
    public boolean validateAmount(double amount) {
        return amount > 0; 
    }
    
    @Override
    public String getPaymentStatus() {
        return "PROCESSED";
    }
}

