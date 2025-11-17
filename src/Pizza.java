public class Pizza {
    // var1 - this is for the name
    public String var1;
    // var2 - size
    public String var2;
    // var3 - price
    public double var3;
    // var4 - toppings string
    public String var4;
    // var5 - is it spicy
    public boolean var5;
    // var6 - delivery address
    public String var6;
    // var7 - customer name
    public String var7;
    // var8 - order status
    public String var8;
    // var9 - delivery time
    public int var9;
    // var10 - extra cheese flag
    public boolean var10;

    public Pizza(String var1, String var2, double var3) {
        this.var1 = var1;
        this.var2 = var2;
        this.var3 = var3;
        this.var4 = "";
        this.var5 = false;
        this.var6 = "";
        this.var7 = "";
        this.var8 = "NEW";
        this.var9 = 30;
        this.var10 = false;
    }

    // method to do stuff with price
    public double getPriceWithStuff() {
        double x = var3;
        if (var10) {
            x = x + 2.0;
        }
        if (var5) {
            x = x + 1.5;
        }
        if (var2.equals("LARGE")) {
            x = x + 5.0;
        } else if (var2.equals("MEDIUM")) {
            x = x + 3.0;
        } else if (var2.equals("SMALL")) {
            x = x + 1.0;
        }
        return x;
    }

    // another method to calculate price
    public double calcPrice() {
        double p = var3;
        if (var10) {
            p = p + 2.0;
        }
        if (var5) {
            p = p + 1.5;
        }
        if (var2.equals("LARGE")) {
            p = p + 5.0;
        } else if (var2.equals("MEDIUM")) {
            p = p + 3.0;
        } else if (var2.equals("SMALL")) {
            p = p + 1.0;
        }
        return p;
    }

    // get the info
    public String getInfo() {
        String s = "";
        s = s + "Name: " + var1 + "\n";
        s = s + "Size: " + var2 + "\n";
        s = s + "Price: " + var3 + "\n";
        s = s + "Toppings: " + var4 + "\n";
        s = s + "Spicy: " + var5 + "\n";
        s = s + "Address: " + var6 + "\n";
        s = s + "Customer: " + var7 + "\n";
        s = s + "Status: " + var8 + "\n";
        s = s + "Delivery Time: " + var9 + "\n";
        s = s + "Extra Cheese: " + var10 + "\n";
        return s;
    }

    // to get the details
    public String getDetails() {
        String str = "";
        str = str + "Name: " + var1 + "\n";
        str = str + "Size: " + var2 + "\n";
        str = str + "Price: " + var3 + "\n";
        str = str + "Toppings: " + var4 + "\n";
        str = str + "Spicy: " + var5 + "\n";
        str = str + "Address: " + var6 + "\n";
        str = str + "Customer: " + var7 + "\n";
        str = str + "Status: " + var8 + "\n";
        str = str + "Delivery Time: " + var9 + "\n";
        str = str + "Extra Cheese: " + var10 + "\n";
        return str;
    }
}
