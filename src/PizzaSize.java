public class PizzaSize {
    private String sizeLabel;
    private double priceMultiplier;
    private int diameterCm;

    public PizzaSize(String sizeLabel, double priceMultiplier, int diameterCm) {
        this.sizeLabel = sizeLabel;
        this.priceMultiplier = priceMultiplier;
        this.diameterCm = diameterCm;
    }

    public String getS() {
        return sizeLabel;
    }

    public double getM() {
        return priceMultiplier;
    }

    public int getD() {
        return diameterCm;
    }

    public void setS(String sizeLabel) {
        this.sizeLabel = sizeLabel;
    }

    public void setM(double priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    public void setD(int diameterCm) {
        this.diameterCm = diameterCm;
    }
}
