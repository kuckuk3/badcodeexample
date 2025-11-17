public class Customer {
    private String n;
    private String a;
    private String p;
    private String e;
    private int lc;
    private double tpr;

    public Customer(String n, String a, String p) {
        this.n = n;
        this.a = a;
        this.p = p;
        this.e = "";
        this.lc = 0;
        this.tpr = 0.0;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public int getLc() {
        return lc;
    }

    public void setLc(int lc) {
        this.lc = lc;
    }

    public double getTpr() {
        return tpr;
    }

    public void setTpr(double tpr) {
        this.tpr = tpr;
    }

    public void incLc() {
        lc++;
    }

    public void addTpr(double amount) {
        tpr += amount;
    }

    public String getInfo() {
        return n + "|" + a + "|" + p + "|" + e + "|" + lc + "|" + tpr;
    }
}
