public class Ingredient {
    private String ingredientName;
    private double unitCost;
    private boolean isAvailable;
    private int stockLevel;

    public Ingredient(String ingredientName, double unitCost) {
        this.ingredientName = ingredientName;
        this.unitCost = unitCost;
        this.isAvailable = true;
        this.stockLevel = 100;
    }

    public String getNm() {
        return ingredientName;
    }

    public double getC() {
        return unitCost;
    }

    public boolean isA() {
        return isAvailable;
    }

    public void setA(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public int getS() {
        return stockLevel;
    }

    public void setS(int stockLevel) {
        this.stockLevel = stockLevel;
    }

    public void reduceS(int amount) {
        stockLevel -= amount;
        if (stockLevel < 0) stockLevel = 0;
    }

    public boolean checkAvailability() {
        // checks if ingredient is available
        if (isAvailable && stockLevel > 0) {
            return true;
        }
        return false;
    }
}
