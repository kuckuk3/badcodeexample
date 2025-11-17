public class PizzaSize {
    private String s;
    private double m;
    private int d;

    public PizzaSize(String s, double m, int d) {
        this.s = s;
        this.m = m;
        this.d = d;
    }

    public String getS() {
        return s;
    }

    public double getM() {
        return m;
    }

    public int getD() {
        return d;
    }

    public void setS(String s) {
        this.s = s;
    }

    public void setM(double m) {
        this.m = m;
    }

    public void setD(int d) {
        this.d = d;
    }
}
