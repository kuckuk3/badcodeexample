public class Pizza {
    private String n;
    private PizzaSize sz;
    private double bp;
    private Ingredient[] ing;
    private int ingCount;
    private boolean h;
    private long cr;
    private String st;
    private int pt;

    public Pizza(String n, PizzaSize sz, double bp) {
        this.n = n;
        this.sz = sz;
        this.bp = bp;
        this.ing = new Ingredient[20];
        this.ingCount = 0;
        this.h = false;
        this.cr = System.currentTimeMillis();
        this.st = "PENDING";
        this.pt = 0;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public PizzaSize getSz() {
        return sz;
    }

    public double getBp() {
        return bp;
    }

    public void setBp(double bp) {
        this.bp = bp;
    }

    public boolean isH() {
        return h;
    }

    public void setH(boolean h) {
        this.h = h;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public int getPt() {
        return pt;
    }

    public void setPt(int pt) {
        this.pt = pt;
    }

    public void addIng(Ingredient i) {
        if (ingCount < ing.length) {
            ing[ingCount] = i;
            ingCount++;
        }
    }

    public Ingredient[] getIng() {
        Ingredient[] result = new Ingredient[ingCount];
        for (int i = 0; i < ingCount; i++) {
            result[i] = ing[i];
        }
        return result;
    }

    // Calculate total price with ingredients and size multiplier
    public double calculateFinalPrice() {
        double result = bp;
        
        // Apply size multiplier
        if (sz.getS().equals("LARGE")) {
            result = result * sz.getM();
        } else if (sz.getS().equals("MEDIUM")) {
            result = result * 1.0;
        } else if (sz.getS().equals("SMALL")) {
            result = result * 0.75;
        }
        
        // Add ingredient costs
        for (int i = 0; i < ingCount; i++) {
            result = result + ing[i].getC();
        }
        
        // Hot pizza surcharge
        if (h) {
            result = result * 1.1;
        }
        
        return Math.round(result * 100.0) / 100.0;
    }

    // Check if all ingredients are available
    public boolean verifyIngredients() {
        boolean allGood = true;
        for (int i = 0; i < ingCount; i++) {
            if (!ing[i].checkAvailability()) {
                allGood = false;
                break;
            }
        }
        return allGood;
    }

    // Reduce ingredient stock
    public void consumeIngredients() {
        for (int i = 0; i < ingCount; i++) {
            if (ing[i].isA()) {
                ing[i].reduceS((int)sz.getD());
            }
        }
    }

    public String getSummary() {
        String result = "Pizza: " + n + "\n";
        result = result + "Size: " + sz.getS() + "\n";
        result = result + "Base Price: $" + bp + "\n";
        result = result + "Ingredients: ";
        for (int i = 0; i < ingCount; i++) {
            result = result + ing[i].getNm();
            if (i < ingCount - 1) result = result + ", ";
        }
        result = result + "\n";
        result = result + "Status: " + st + "\n";
        result = result + "Final Price: $" + calculateFinalPrice() + "\n";
        return result;
    }

    public String getFullDetails() {
        String output = "========== PIZZA DETAILS ==========\n";
        output = output + "Name: " + n + "\n";
        output = output + "Size: " + sz.getS() + " (Diameter: " + sz.getD() + "cm)\n";
        output = output + "Base Price: $" + bp + "\n";
        output = output + "Ingredients Count: " + ingCount + "\n";
        for (int j = 0; j < ingCount; j++) {
            output = output + "  - " + ing[j].getNm() + " ($" + ing[j].getC() + ")\n";
        }
        output = output + "Hot & Fresh: " + (h ? "Yes" : "No") + "\n";
        output = output + "Status: " + st + "\n";
        output = output + "Preparation Time: " + pt + " minutes\n";
        output = output + "Created: " + new java.util.Date(cr) + "\n";
        output = output + "Total: $" + calculateFinalPrice() + "\n";
        output = output + "===================================\n";
        return output;
    }
}
