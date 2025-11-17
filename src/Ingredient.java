public class Ingredient {
    private String nm;
    private double c;
    private boolean a;
    private int s;

    public Ingredient(String nm, double c) {
        this.nm = nm;
        this.c = c;
        this.a = true;
        this.s = 100;
    }

    public String getNm() {
        return nm;
    }

    public double getC() {
        return c;
    }

    public boolean isA() {
        return a;
    }

    public void setA(boolean a) {
        this.a = a;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public void reduceS(int amount) {
        s -= amount;
        if (s < 0) s = 0;
    }

    public boolean checkAvailability() {
        // checks if ingredient is available
        if (a && s > 0) {
            return true;
        }
        return false;
    }
}
