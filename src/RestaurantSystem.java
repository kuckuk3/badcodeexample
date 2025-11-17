import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class RestaurantSystem {
    private OrderManager om;
    private Map<String, Pizza> menu;
    private Map<String, Ingredient> ingredients;
    private Map<String, Customer> customers;
    private int ocount;

    public RestaurantSystem() {
        this.om = OrderManager.getInstance();
        this.menu = new HashMap<>();
        this.ingredients = new HashMap<>();
        this.customers = new HashMap<>();
        this.ocount = 0;
        initializeSystem();
    }

    // Initialize ingredients and menu
    private void initializeSystem() {
        initializeIngredients();
        initializeMenuPizzas();
    }

    // Setup ingredients with costs
    private void initializeIngredients() {
        ingredients.put("tomato", new Ingredient("Tomato Sauce", 0.5));
        ingredients.put("cheese", new Ingredient("Mozzarella", 1.0));
        ingredients.put("basil", new Ingredient("Fresh Basil", 0.75));
        ingredients.put("pepperoni", new Ingredient("Pepperoni", 1.5));
        ingredients.put("pineapple", new Ingredient("Pineapple", 0.8));
        ingredients.put("ham", new Ingredient("Ham", 1.2));
        ingredients.put("jalapeno", new Ingredient("Jalapeno", 0.6));
        ingredients.put("olive", new Ingredient("Black Olives", 0.9));
        ingredients.put("mushroom", new Ingredient("Mushrooms", 0.7));
        ingredients.put("onion", new Ingredient("Onions", 0.4));
    }

    // Build pizza menu with ingredients
    private void initializeMenuPizzas() {
        // Margherita variations
        createMenuPizza("margherita_small", "Margherita", new PizzaSize("SMALL", 1.0, 20), 7.99,
                new String[]{"tomato", "cheese", "basil"});
        createMenuPizza("margherita_medium", "Margherita", new PizzaSize("MEDIUM", 1.0, 25), 9.99,
                new String[]{"tomato", "cheese", "basil"});
        createMenuPizza("margherita_large", "Margherita", new PizzaSize("LARGE", 1.3, 30), 12.99,
                new String[]{"tomato", "cheese", "basil"});

        // Pepperoni variations
        createMenuPizza("pepperoni_small", "Pepperoni", new PizzaSize("SMALL", 1.0, 20), 8.99,
                new String[]{"tomato", "cheese", "pepperoni"});
        createMenuPizza("pepperoni_medium", "Pepperoni", new PizzaSize("MEDIUM", 1.0, 25), 10.99,
                new String[]{"tomato", "cheese", "pepperoni"});
        createMenuPizza("pepperoni_large", "Pepperoni", new PizzaSize("LARGE", 1.3, 30), 13.99,
                new String[]{"tomato", "cheese", "pepperoni"});

        // Supreme varieties
        createMenuPizza("supreme_small", "Supreme", new PizzaSize("SMALL", 1.0, 20), 9.99,
                new String[]{"tomato", "cheese", "pepperoni", "mushroom", "onion"});
        createMenuPizza("supreme_medium", "Supreme", new PizzaSize("MEDIUM", 1.0, 25), 12.99,
                new String[]{"tomato", "cheese", "pepperoni", "mushroom", "onion"});
        createMenuPizza("supreme_large", "Supreme", new PizzaSize("LARGE", 1.3, 30), 15.99,
                new String[]{"tomato", "cheese", "pepperoni", "mushroom", "onion"});

        // Special hot pizza
        createMenuPizza("spicy_medium", "Spicy Inferno", new PizzaSize("MEDIUM", 1.0, 25), 11.99,
                new String[]{"tomato", "cheese", "jalapeno", "pepperoni"});
    }

    // Helper to create menu items
    private void createMenuPizza(String key, String name, PizzaSize size, double basePrice, String[] ingList) {
        Pizza p = new Pizza(name, size, basePrice);
        for (String ingKey : ingList) {
            if (ingredients.containsKey(ingKey)) {
                p.addIng(ingredients.get(ingKey));
            }
        }
        menu.put(key, p);
    }

    public int placeOrder(String pizzaKey, String customerName, String address, String phone) {
        return placeOrderWithOptions(pizzaKey, customerName, address, phone, false);
    }

    public int placeOrderWithHot(String pizzaKey, String customerName, String address, String phone) {
        return placeOrderWithOptions(pizzaKey, customerName, address, phone, true);
    }

    private int placeOrderWithOptions(String pizzaKey, String customerName, String address, String phone, boolean makeHot) {
        if (!menu.containsKey(pizzaKey)) {
            System.out.println("Pizza not found in menu");
            return -1;
        }

        Pizza menuPizza = menu.get(pizzaKey);
        Pizza orderPizza = new Pizza(menuPizza.getN(), menuPizza.getSz(), menuPizza.getBp());
        
        for (Ingredient ing : menuPizza.getIng()) {
            orderPizza.addIng(ing);
        }
        
        orderPizza.setH(makeHot);

        Customer cust = new Customer(customerName, address, phone);
        if (customers.containsKey(customerName)) {
            cust = customers.get(customerName);
            cust.setA(address);
        } else {
            customers.put(customerName, cust);
        }

        int orderId = om.newOrder(orderPizza, cust);
        System.out.println("Order placed successfully. Order ID: " + orderId);
        return orderId;
    }

    public void processWorkflow(int orderId) {
        if (om.validate(orderId)) {
            if (om.getOrder(orderId) != null) {
                if (om.getCustomer(orderId) != null) {
                    System.out.println("Starting preparation...");
                    om.prepareOrder(orderId);
                    
                    if (om.getStatus(orderId) == 1) {
                        om.bakeOrder(orderId);
                        
                        if (om.getStatus(orderId) == 2) {
                            om.qualityCheck(orderId);
                            
                            if (om.getStatus(orderId) == 3) {
                                om.markReady(orderId);
                                System.out.println("Pizza ready for delivery");
                            } else {
                                System.out.println("Quality check failed");
                            }
                        }
                    }
                } else {
                    System.out.println("Customer not found");
                }
            } else {
                System.out.println("Order not found");
            }
        } else {
            System.out.println("Order validation failed");
        }
    }

    // Display menu with item codes
    public void displayMenu() {
        System.out.println("\n========== PIZZA MENU ==========");
        String[] pizzaTypes = {"margherita_small", "margherita_medium", "margherita_large",
                              "pepperoni_small", "pepperoni_medium", "pepperoni_large",
                              "supreme_small", "supreme_medium", "supreme_large",
                              "spicy_medium"};
        
        for (String key : pizzaTypes) {
            if (menu.containsKey(key)) {
                Pizza p = menu.get(key);
                System.out.println("[" + key + "] - " + p.getN() + " (" + p.getSz().getS() + ") - $" + p.getBp());
            }
        }
        System.out.println("================================\n");
    }

    // Show orders with formatting
    public void showAllOrders() {
        System.out.println("\n========== ALL ORDERS ==========");
        om.printAllOrders();
        System.out.println("================================\n");
    }

    // Interactive mode
    public void startInteractiveMode() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("Welcome to Pizza Ordering System");

        while (running) {
            System.out.println("\n1. View Menu");
            System.out.println("2. Place Order");
            System.out.println("3. View Orders");
            System.out.println("4. Process Order");
            System.out.println("5. Exit");
            System.out.print("Select option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch(choice) {
                case 1:
                    displayMenu();
                    break;
                case 2:
                    System.out.print("Enter pizza code: ");
                    String pizzaCode = scanner.nextLine();
                    System.out.print("Enter customer name: ");
                    String custName = scanner.nextLine();
                    System.out.print("Enter address: ");
                    String addr = scanner.nextLine();
                    System.out.print("Enter phone: ");
                    String phone = scanner.nextLine();
                    placeOrder(pizzaCode, custName, addr, phone);
                    break;
                case 3:
                    showAllOrders();
                    break;
                case 4:
                    System.out.print("Enter order ID: ");
                    int orderId = scanner.nextInt();
                    processWorkflow(orderId);
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
        scanner.close();
    }

    public static void main(String[] args) {
        RestaurantSystem system = new RestaurantSystem();
        
        // Demo mode
        System.out.println("=== PIZZA ORDERING SYSTEM DEMO ===\n");
        
        system.displayMenu();
        
        int order1 = system.placeOrder("margherita_medium", "John Doe", "123 Main St", "555-0001");
        int order2 = system.placeOrder("pepperoni_large", "Jane Smith", "456 Oak Ave", "555-0002");
        int order3 = system.placeOrderWithHot("spicy_medium", "Bob Johnson", "789 Pine Rd", "555-0003");
        
        system.showAllOrders();
        
        if (order1 >= 0) {
            System.out.println("Processing order " + order1);
            system.processWorkflow(order1);
        }
        
        // Uncomment to start interactive mode
        // system.startInteractiveMode();
    }
}
