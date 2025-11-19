public class Pizza {
    private String pizzaName;
    private PizzaSize pizzaSize;
    private double basePrice;
    private Ingredient[] ingredientList;
    private int ingCount;
    private boolean isHot;
    private long createdTime;
    private String pizzaStatus;
    private int prepTime;

    public Pizza(String pizzaName, PizzaSize pizzaSize, double basePrice) {
        this.pizzaName = pizzaName;
        this.pizzaSize = pizzaSize;
        this.basePrice = basePrice;
        this.ingredientList = new Ingredient[20];
        this.ingCount = 0;
        this.isHot = false;
        this.createdTime = System.currentTimeMillis();
        this.pizzaStatus = "PENDING";
        this.prepTime = 0;
    }

    public String getN() {
        return pizzaName;
    }

    public void setN(String pizzaName) {
        this.pizzaName = pizzaName;
    }

    public PizzaSize getSz() {
        return pizzaSize;
    }

    public double getBp() {
        return basePrice;
    }

    public void setBp(double basePrice) {
        this.basePrice = basePrice;
    }

    public boolean isH() {
        return isHot;
    }

    public void setH(boolean isHot) {
        this.isHot = isHot;
    }

    public String getSt() {
        return pizzaStatus;
    }

    public void setSt(String pizzaStatus) {
        this.pizzaStatus = pizzaStatus;
    }

    public int getPt() {
        return prepTime;
    }

    public void setPt(int prepTime) {
        this.prepTime = prepTime;
    }

    public void addIng(Ingredient i) {
        if (ingCount < ingredientList.length) {
            ingredientList[ingCount] = i;
            ingCount++;
        }
    }

    public Ingredient[] getIng() {
        Ingredient[] result = new Ingredient[ingCount];
        for (int i = 0; i < ingCount; i++) {
            result[i] = ingredientList[i];
        }
        return result;
    }

    // Calculate total price with ingredients and size multiplier
    public double calculateFinalPrice() {
        double result = basePrice;
        
        // Apply size multiplier
        if (pizzaSize.getS().equals("LARGE")) {
            result = result * pizzaSize.getM();
        } else if (pizzaSize.getS().equals("MEDIUM")) {
            result = result * 1.0;
        } else if (pizzaSize.getS().equals("SMALL")) {
            result = result * 0.75;
        }
        
        // Add ingredient costs
        for (int i = 0; i < ingCount; i++) {
            result = result + ingredientList[i].getC();
        }
        
        // Hot pizza surcharge
        if (isHot) {
            result = result * 1.1;
        }
        
        return Math.round(result * 100.0) / 100.0;
    }

    // Check if all ingredients are available
    public boolean verifyIngredients() {
        boolean allGood = true;
        for (int i = 0; i < ingCount; i++) {
            if (!ingredientList[i].checkAvailability()) {
                allGood = false;
                break;
            }
        }
        return allGood;
    }

    // Reduce ingredient stock
    public void consumeIngredients() {
        for (int i = 0; i < ingCount; i++) {
            if (ingredientList[i].isA()) {
                ingredientList[i].reduceS((int)pizzaSize.getD());
            }
        }
    }

    public String getSummary() {
        String result = "Pizza: " + pizzaName + "\n";
        result = result + "Size: " + pizzaSize.getS() + "\n";
        result = result + "Base Price: $" + basePrice + "\n";
        result = result + "Ingredients: ";
        for (int i = 0; i < ingCount; i++) {
            result = result + ingredientList[i].getNm();
            if (i < ingCount - 1) result = result + ", ";
        }
        result = result + "\n";
        result = result + "Status: " + pizzaStatus + "\n";
        result = result + "Final Price: $" + calculateFinalPrice() + "\n";
        return result;
    }

    public String getFullDetails() {
        String output = "========== PIZZA DETAILS ==========\n";
        output = output + "Name: " + pizzaName + "\n";
        output = output + "Size: " + pizzaSize.getS() + " (Diameter: " + pizzaSize.getD() + "cm)\n";
        output = output + "Base Price: $" + basePrice + "\n";
        output = output + "Ingredients Count: " + ingCount + "\n";
        for (int j = 0; j < ingCount; j++) {
            output = output + "  - " + ingredientList[j].getNm() + " ($" + ingredientList[j].getC() + ")\n";
        }
        output = output + "Hot & Fresh: " + (isHot ? "Yes" : "No") + "\n";
        output = output + "Status: " + pizzaStatus + "\n";
        output = output + "Preparation Time: " + prepTime + " minutes\n";
        output = output + "Created: " + new java.util.Date(createdTime) + "\n";
        output = output + "Total: $" + calculateFinalPrice() + "\n";
        output = output + "===================================\n";
        return output;
    }
}
