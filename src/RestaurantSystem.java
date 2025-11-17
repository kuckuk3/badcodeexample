public class RestaurantSystem {
    // this is the main class for handling everything
    private OrderManager om;
    private Pizza[] p_array = new Pizza[100];
    private int p_count = 0;

    public RestaurantSystem() {
        this.om = OrderManager.getInstance();
        initializePizzas();
    }

    // initialize some pizzas
    private void initializePizzas() {
        // add some default pizzas to menu
        Pizza p1 = new Pizza("Margherita", "MEDIUM", 8.99);
        p1.var4 = "tomato,cheese,basil";
        p_array[0] = p1;
        p_count++;

        Pizza p2 = new Pizza("Pepperoni", "MEDIUM", 10.99);
        p2.var4 = "tomato,cheese,pepperoni";
        p_array[1] = p2;
        p_count++;

        Pizza p3 = new Pizza("Spicy", "MEDIUM", 11.99);
        p3.var4 = "tomato,cheese,jalapeno,chili";
        p3.var5 = true;
        p_array[2] = p3;
        p_count++;

        Pizza p4 = new Pizza("Hawaiian", "MEDIUM", 10.99);
        p4.var4 = "tomato,cheese,pineapple,ham";
        p_array[3] = p4;
        p_count++;

        Pizza p5 = new Pizza("Margherita", "LARGE", 11.99);
        p5.var4 = "tomato,cheese,basil";
        p_array[4] = p5;
        p_count++;

        Pizza p6 = new Pizza("Pepperoni", "LARGE", 13.99);
        p6.var4 = "tomato,cheese,pepperoni";
        p_array[5] = p6;
        p_count++;

        Pizza p7 = new Pizza("Spicy", "LARGE", 14.99);
        p7.var4 = "tomato,cheese,jalapeno,chili";
        p7.var5 = true;
        p_array[6] = p7;
        p_count++;

        Pizza p8 = new Pizza("Hawaiian", "LARGE", 13.99);
        p8.var4 = "tomato,cheese,pineapple,ham";
        p_array[7] = p8;
        p_count++;

        Pizza p9 = new Pizza("Margherita", "SMALL", 5.99);
        p9.var4 = "tomato,cheese,basil";
        p_array[8] = p9;
        p_count++;

        Pizza p10 = new Pizza("Pepperoni", "SMALL", 6.99);
        p10.var4 = "tomato,cheese,pepperoni";
        p_array[9] = p10;
        p_count++;
    }

    public void createOrder(String pizzaName, String size, String customerName, String address) {
        // find pizza in menu
        Pizza foundPizza = null;
        for (int i = 0; i < p_count; i++) {
            if (p_array[i].var1.equals(pizzaName) && p_array[i].var2.equals(size)) {
                foundPizza = p_array[i];
                break;
            }
        }

        if (foundPizza != null) {
            Pizza newPizza = new Pizza(foundPizza.var1, foundPizza.var2, foundPizza.var3);
            newPizza.var4 = foundPizza.var4;
            newPizza.var5 = foundPizza.var5;
            newPizza.var7 = customerName;
            newPizza.var6 = address;
            newPizza.var8 = "RECEIVED";
            om.addOrder(newPizza);
            System.out.println("Order created successfully");
        } else {
            System.out.println("Pizza not found");
        }
    }

    public void createOrderWithCheese(String pizzaName, String size, String customerName, String address) {
        // find pizza in menu
        Pizza foundPizza = null;
        for (int i = 0; i < p_count; i++) {
            if (p_array[i].var1.equals(pizzaName) && p_array[i].var2.equals(size)) {
                foundPizza = p_array[i];
                break;
            }
        }

        if (foundPizza != null) {
            Pizza newPizza = new Pizza(foundPizza.var1, foundPizza.var2, foundPizza.var3);
            newPizza.var4 = foundPizza.var4;
            newPizza.var5 = foundPizza.var5;
            newPizza.var7 = customerName;
            newPizza.var6 = address;
            newPizza.var10 = true; // extra cheese
            newPizza.var8 = "RECEIVED";
            om.addOrder(newPizza);
            System.out.println("Order with extra cheese created successfully");
        } else {
            System.out.println("Pizza not found");
        }
    }

    public void createSpicyOrder(String pizzaName, String size, String customerName, String address) {
        // find pizza in menu
        Pizza foundPizza = null;
        for (int i = 0; i < p_count; i++) {
            if (p_array[i].var1.equals(pizzaName) && p_array[i].var2.equals(size)) {
                foundPizza = p_array[i];
                break;
            }
        }

        if (foundPizza != null) {
            Pizza newPizza = new Pizza(foundPizza.var1, foundPizza.var2, foundPizza.var3);
            newPizza.var4 = foundPizza.var4;
            newPizza.var5 = true; // spicy
            newPizza.var7 = customerName;
            newPizza.var6 = address;
            newPizza.var8 = "RECEIVED";
            om.addOrder(newPizza);
            System.out.println("Spicy order created successfully");
        } else {
            System.out.println("Pizza not found");
        }
    }

    public void processOrder(int orderIndex) {
        // check if order is valid
        if (om.validateOrder(orderIndex)) {
            om.updateOrderStatus(orderIndex, "PROCESSING");
            System.out.println("Order is being processed");
        } else {
            System.out.println("Order validation failed");
        }
    }

    public void deliverOrder(int orderIndex) {
        // TODO: implement delivery logic
        om.updateOrderStatus(orderIndex, "DELIVERED");
    }

    public void showMenu() {
        System.out.println("=== PIZZA MENU ===");
        for (int i = 0; i < p_count; i++) {
            System.out.println(p_array[i].var1 + " (" + p_array[i].var2 + ") - $" + p_array[i].var3);
        }
    }

    public void showOrders() {
        om.printAllOrders();
    }

    public double getOrderTotal(int orderIndex) {
        return om.getTotalPrice(orderIndex);
    }

    public static void main(String[] args) {
        RestaurantSystem system = new RestaurantSystem();

        system.showMenu();
        System.out.println();

        // create some orders
        system.createOrder("Margherita", "MEDIUM", "John Doe", "123 Main St");
        system.createOrderWithCheese("Pepperoni", "LARGE", "Jane Smith", "456 Oak Ave");
        system.createSpicyOrder("Spicy", "MEDIUM", "Bob Johnson", "789 Pine Rd");

        System.out.println();
        system.showOrders();

        // process an order
        system.processOrder(0);
        System.out.println("Total for order 0: $" + system.getOrderTotal(0));
    }
}
