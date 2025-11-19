public class Customer {
    private String customerName;
    private String address;
    private String phoneNum;
    private String emailAddr;
    private int loyaltyCount;
    private double totalSpent;

    public Customer(String customerName, String address, String phoneNum) {
        this.customerName = customerName;
        this.address = address;
        this.phoneNum = phoneNum;
        this.emailAddr = "";
        this.loyaltyCount = 0;
        this.totalSpent = 0.0;
    }

    public String getN() {
        return customerName;
    }

    public void setN(String customerName) {
        this.customerName = customerName;
    }

    public String getA() {
        return address;
    }

    public void setA(String address) {
        this.address = address;
    }

    public String getP() {
        return phoneNum;
    }

    public void setP(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getE() {
        return emailAddr;
    }

    public void setE(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    public int getLc() {
        return loyaltyCount;
    }

    public void setLc(int loyaltyCount) {
        this.loyaltyCount = loyaltyCount;
    }

    public double getTpr() {
        return totalSpent;
    }

    public void setTpr(double totalSpent) {
        this.totalSpent = totalSpent;
    }

    public void incLc() {
        loyaltyCount++;
    }

    public void addTpr(double amount) {
        totalSpent += amount;
    }

    public String getInfo() {
        return customerName + "|" + address + "|" + phoneNum + "|" + emailAddr + "|" + loyaltyCount + "|" + totalSpent;
    }
}
