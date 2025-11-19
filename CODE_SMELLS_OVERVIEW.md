# Code Smells Overview

This document provides a comprehensive overview of all code smells present in the Pizza Ordering System project, organized by category with specific examples and locations.

---

## 1. Bad Naming Conventions

### Single-Letter Variables
Variables with names that don't convey meaning make code hard to understand.

| Variable | Location | Class | Meaning |
|----------|----------|-------|---------|
| `n` | Line 8+ | Pizza, Customer, Ingredient | name |
| `sz` | Line 3+ | Pizza | pizzaSize |
| `bp` | Line 4+ | Pizza | basePrice |
| `ing` | Line 5+ | Pizza | ingredientList |
| `cr` | Line 8+ | Pizza | createdTime |
| `st` | Line 9+ | Pizza | pizzaStatus |
| `pt` | Line 10+ | Pizza | prepTime |
| `m` | Line 5+ | PizzaSize | priceMultiplier |
| `d` | Line 6+ | PizzaSize | diameterCm |
| `a` | Line 4+ | Ingredient | isAvailable |
| `s` | Line 3-5+ | Ingredient, PizzaSize | stockLevel/sizeLabel |
| `p` | Line 3+ | Customer | phoneNum |
| `e` | Line 4+ | Customer | emailAddr |
| `lc` | Line 5+ | Customer | loyaltyCount |
| `tpr` | Line 6+ | Customer | totalSpent |
| `tc` | Line 2 | PaymentProcessor | transactionCount |

**Impact:** Code becomes cryptic and requires constant mental mapping of variables to their meanings.

---

## 2. Abbreviated Method Names

Methods with abbreviated names that don't match field names create inconsistency.

```java
// Pizza.java
public String getN() { return pizzaName; }      // Abbreviated method, descriptive field
public void setN(String pizzaName) { ... }      // Inconsistency
public PizzaSize getSz() { return pizzaSize; }  // Another abbreviation
public double getBp() { return basePrice; }     // Yet another
```

**Impact:** Creates cognitive load - developers must remember both the abbreviated method name and the actual field name.

---

## 3. Deep Nesting

### OrderManager.java - validate() method (Lines 88-110)

```java
if (p == null || c == null) return false;
if (p.getN() == null || p.getN().trim().isEmpty()) return false;
if (p.getSz() == null) return false;
if (p.getBp() <= 0) return false;
if (c.getN() == null || c.getN().trim().isEmpty()) return false;
if (c.getA() == null || c.getA().trim().isEmpty()) return false;
if (c.getP() == null || c.getP().trim().isEmpty()) return false;

if (!p.verifyIngredients()) return false;

if (orderStatuses[id] == 0) {           // Level 1
    if (p.isH()) {                      // Level 2
        if (p.getSz().getD() > 30) {    // Level 3
            if (timestampMap.get(id) > 0) { // Level 4
                return true;
            }
        }
    } else {
        return true;
    }
}
```

**Nesting Depth:** 4 levels  
**Issue:** Logic is buried deep, making it hard to understand the condition flow.

### DeliveryHandler.java - processDeliveries() method (Lines 72-88)

```java
for (int i = 0; i < deliveryCount; i++) {         // Level 1
    if (deliveryStatuses[i] != null) {             // Level 2
        if (deliveryStatuses[i].equals("ASSIGNED")) { // Level 3
            for (int j = 0; j < deliveryMinutes[i]; j++) { // Level 4
                if (j == deliveryMinutes[i] - 1) { // Level 5
                    deliveryStatuses[i] = "IN_TRANSIT";
                    if (orderMgr.validate(deliveryOrderIds[i])) { // Level 6
                        if (orderMgr.getStatus(deliveryOrderIds[i]) == 4) { // Level 7
                            orderMgr.deliver(deliveryOrderIds[i]);
                            deliveryStatuses[i] = "DELIVERED";
                        }
                    }
                }
            }
        }
    }
}
```

**Nesting Depth:** 7 levels  
**Issue:** Extremely hard to follow logic; contains nested loop with further conditionals.

### RestaurantSystem.java - processWorkflow() method (Lines 119-142)

```java
if (orderMgr.validate(orderId)) {                // Level 1
    if (orderMgr.getOrder(orderId) != null) {    // Level 2
        if (orderMgr.getCustomer(orderId) != null) { // Level 3
            System.out.println("Starting preparation...");
            orderMgr.prepareOrder(orderId);
            
            if (orderMgr.getStatus(orderId) == 1) { // Level 4
                orderMgr.bakeOrder(orderId);
                
                if (orderMgr.getStatus(orderId) == 2) { // Level 5
                    orderMgr.qualityCheck(orderId);
                    
                    if (orderMgr.getStatus(orderId) == 3) { // Level 6
                        orderMgr.markReady(orderId);
                        System.out.println("Pizza ready for delivery");
                    }
                }
            }
        }
    }
}
```

**Nesting Depth:** 6 levels  
**Issue:** Validation and execution logic intertwined in deeply nested if-statements.

---

## 4. Magic Numbers

### DeliveryHandler.java

```java
// Line 64-67: calculateDeliveryFee()
double fee = 3.00;  // What is this base fee? Why 3.00?
if (p.getSz().getS().equals("LARGE")) {
    fee = fee + 1.50;  // Undocumented surcharge
}
if (p.isH()) {
    fee = fee + 2.00;  // Hot pizza surcharge - unexplained
}
if (c.getLc() > 10) {  // What is 10? Why this threshold?
    fee = fee * 0.8;   // Why 0.8? 20% discount?
}

// Line 134-145: estimateDeliveryTime()
if (p.getSz().getD() > 25) {     // Why 25 cm?
    finalTime = finalTime + 5;   // 5 what? Minutes
}
if (p.isH()) {
    finalTime = finalTime + 3;   // Why 3?
}
if (p.getIng().length > 6) {     // Why 6 ingredients?
    finalTime = finalTime + 2;   // Why 2?
}
```

**Impact:** Business rules hidden in magic numbers are hard to maintain and test.

### PaymentProcessor.java

```java
// Line 77: validateCreditCard()
return amount > 0 && amount < 1000;  // Why 1000? Max transaction limit?

// Line 2-4: Fields
private int tc = 0;           // What does 'tc' mean? transactionCount
private double[] amounts = new double[1000];  // Why 1000? Fixed capacity
```

### Pizza.java

```java
// Line 90-100: calculateFinalPrice()
if (pizzaSize.getS().equals("LARGE")) {
    result = result * pizzaSize.getM();
} else if (pizzaSize.getS().equals("MEDIUM")) {
    result = result * 1.0;    // Why not just: no change?
} else if (pizzaSize.getS().equals("SMALL")) {
    result = result * 0.75;   // 25% discount for small - undocumented
}
// ...
if (isHot) {
    result = result * 1.1;    // 10% hot pizza surcharge
}
```

---

## 5. DRY Violations (Don't Repeat Yourself)

### Price Calculation Logic (Repeated in 3+ places)

**Pizza.java - calculateFinalPrice()**
```java
public double calculateFinalPrice() {
    double result = basePrice;
    if (pizzaSize.getS().equals("LARGE")) {
        result = result * pizzaSize.getM();
    } else if (pizzaSize.getS().equals("MEDIUM")) {
        result = result * 1.0;
    } else if (pizzaSize.getS().equals("SMALL")) {
        result = result * 0.75;
    }
    for (int i = 0; i < ingCount; i++) {
        result = result + ingredientList[i].getC();
    }
    if (isHot) {
        result = result * 1.1;
    }
    return Math.round(result * 100.0) / 100.0;
}
```

**OrderManager.java - getTotalWithTax()**
```java
public double getTotalWithTax(int id) {
    Pizza pizza = orderMap.get(id);
    if (pizza == null) return 0;
    
    double subtotal = pizza.calculateFinalPrice();  // Calls Pizza calculation
    double tax = subtotal * 0.085;
    
    if (customerMap.get(id).getLc() >= 5) {
        double discount = subtotal * 0.1;
        customerMap.get(id).addTpr(subtotal + tax - discount);
        return subtotal + tax - discount;
    }
    
    customerMap.get(id).addTpr(subtotal + tax);
    return subtotal + tax;
}
```

**PaymentProcessor.java - calculateTotal()**
```java
public double calculateTotal(int orderId) {
    OrderManager om = OrderManager.getInstance();
    Pizza p = om.getOrder(orderId);
    Customer c = om.getCustomer(orderId);
    
    if (p == null || c == null) return 0;
    
    double subtotal = p.calculateFinalPrice();  // Same calculation again
    double tax = subtotal * TAX_RATE;
    double delivery = DELIVERY_SURCHARGE;
    double discount = 0;
    
    if (c.getLc() >= LOYALTY_DISCOUNT_THRESHOLD) {
        discount = subtotal * LOYALTY_DISCOUNT_PERCENT;
    }
    
    return subtotal + tax + delivery - discount;
}
```

**Issue:** Price calculation logic appears in multiple places with slight variations. Changes to pricing logic require updating multiple methods.

### Null Check Repetition

```java
// DeliveryHandler.java - validateForDelivery()
if (p == null || c == null) return false;
if (p.getN() == null || p.getN().trim().isEmpty()) return false;
if (c.getN() == null || c.getN().trim().isEmpty()) return false;
if (c.getA() == null || c.getA().trim().isEmpty()) return false;

// OrderManager.java - validate()
if (p == null || c == null) return false;
if (p.getN() == null || p.getN().trim().isEmpty()) return false;
if (p.getSz() == null) return false;
if (c.getN() == null || c.getN().trim().isEmpty()) return false;
if (c.getA() == null || c.getA().trim().isEmpty()) return false;
```

**Issue:** Almost identical null-checking logic in multiple classes.

### Ingredient Copying Logic

```java
// RestaurantSystem.java - placeOrderWithOptions()
for (Ingredient ing : menuPizza.getIng()) {
    orderPizza.addIng(ing);
}
```

This pattern appears whenever pizzas are created from menu items.

---

## 6. KISS Violations (Keep It Simple, Stupid)

### Complex Conditional Logic

**OrderManager.java - validate() method**
Contains multiple layers of validation mixed with business logic decisions. The status check (lines 108-118) buries the actual business rule in nested if statements.

**PaymentProcessor.java - processPayment() method (Lines 23-73)**
Multiple if-else chains returning different status codes (-2, -1, 0, 1, 2, 3, 4) instead of using enums or exception handling.

```java
if (paymentMethod.equalsIgnoreCase("CREDIT_CARD")) {
    currentPaymentMethod = new CreditCardPayment();
    if (validateCreditCard(amount)) {
        double result = currentPaymentMethod.processPayment(amount);
        if (result > 0 && processCardPayment(amount)) {
            recordPayment(orderId, result, paymentMethod);
            return 1; // SUCCESS
        } else {
            return 2; // CARD_DECLINED
        }
    } else {
        return 3; // INVALID_AMOUNT
    }
} else if (paymentMethod.equalsIgnoreCase("CASH")) {
    // ... more logic
} else if (paymentMethod.equalsIgnoreCase("PAYPAL")) {
    // ... more logic
}
return -2;  // UNKNOWN_METHOD
```

**Issue:** Mix of polymorphism, if-else chains, and magic return codes.

---

## 7. Primitive Obsession

### Status Codes as Integers

**OrderManager.java**
```java
private int[] orderStatuses;  // 0=received, 1=preparing, 2=baking, etc.

// Usage throughout code:
orderStatuses[id] = 0;  // What does 0 mean?
if (orderStatuses[id] == 0) { ... }
if (orderStatuses[id] == 4) { ... }
```

**Better Approach:** Use an Enum
```java
public enum OrderStatus {
    RECEIVED(0), PREPARING(1), BAKING(2), 
    QUALITY_CHECK(3), READY(4), DELIVERED(5), CANCELLED(6);
    
    private final int code;
    OrderStatus(int code) { this.code = code; }
}
```

### Status Codes as Strings

**DeliveryHandler.java**
```java
deliveryStatuses[deliveryCount] = "SCHEDULED";
if (deliveryStatuses[i].equals("ASSIGNED")) { ... }
```

**Issue:** String comparisons are error-prone; typos won't be caught at compile time.

### Magic Status Code Returns

**PaymentProcessor.java - processPayment()**
```java
return -1;  // ERROR
return 0;   // INVALID_ORDER
return 1;   // SUCCESS
return 2;   // CARD_DECLINED
return 3;   // INVALID_AMOUNT
return 4;   // PAYPAL_ERROR
return -2;  // UNKNOWN_METHOD
```

**Better Approach:** Use enums or exceptions
```java
public enum PaymentResult {
    SUCCESS, CARD_DECLINED, INVALID_AMOUNT, 
    PAYPAL_ERROR, INVALID_ORDER, UNKNOWN_METHOD, ERROR
}
```

---

## 8. Long Methods

### OrderManager.java - validate() method
**Lines 88-118** - 31 lines  
Combines validation logic for multiple fields with complex nested conditions.

### PaymentProcessor.java - processPayment() method
**Lines 23-73** - 51 lines  
Handles multiple payment types with different logic branches and error codes.

### RestaurantSystem.java - processWorkflow() method
**Lines 119-142** - 24 lines  
Orchestrates multiple status changes with deeply nested conditions.

**Impact:** Long methods are harder to understand, test, and maintain.

---

## 9. Tight Coupling

### OrderManager (Singleton) Dependencies

**OrderManager.java**
```java
private static OrderManager instance;

public static OrderManager getInstance() {
    if (instance == null) {
        synchronized(OrderManager.class) {
            if (instance == null) {
                instance = new OrderManager();
            }
        }
    }
    return instance;
}
```

### Hard-coded Dependencies

**RestaurantSystem.java - processWorkflow()**
```java
if (orderMgr.validate(orderId)) {
    if (orderMgr.getOrder(orderId) != null) {
        if (orderMgr.getCustomer(orderId) != null) {
            // ...
        }
    }
}
```

Classes directly access each other's methods instead of using interfaces.

### PaymentProcessor Dependencies

```java
OrderManager om = OrderManager.getInstance();  // Direct Singleton access
Pizza p = om.getOrder(orderId);                 // Feature envy
Customer c = om.getCustomer(orderId);           // Multiple data dependencies
```

**Impact:** 
- Hard to test (can't mock OrderManager)
- Can't have multiple OrderManager instances
- Changes to one class require changes to many others

---

## 10. Feature Envy

### RestaurantSystem.java - placeOrderWithOptions()

```java
Pizza menuPizza = menu.get(pizzaKey);
Pizza orderPizza = new Pizza(menuPizza.getN(), menuPizza.getSz(), menuPizza.getBp());

for (Ingredient ing : menuPizza.getIng()) {
    orderPizza.addIng(ing);
}

orderPizza.setH(makeHot);

Customer cust = new Customer(customerName, address, phone);
if (customers.containsKey(customerName)) {
    cust = customers.get(customerName);
} else {
    cust.addTpr(0.0);
    customers.put(customerName, cust);
}

int orderId = orderMgr.newOrder(orderPizza, cust);
```

**Issue:** Method knows too much about Pizza and Customer internals; should delegate to these objects.

### PaymentProcessor.java - generateReceipt()

```java
String receipt = "";
receipt = receipt + "Order #" + orderId + "\n";
receipt = receipt + "Customer: " + c.getN() + "\n";          // Accessing Customer fields
receipt = receipt + "Item: " + p.getN() + "\n";              // Accessing Pizza fields
receipt = receipt + "Size: " + p.getSz().getS() + "\n";      // Accessing nested fields
receipt = receipt + "Base Price: $" + String.format("%.2f", p.getBp()) + "\n";
```

**Better Approach:** Create a `Receipt` object or delegate to `Pizza` and `Customer`
```java
receipt += pizza.getReceiptLine();  // Pizza knows how to format itself
receipt += customer.getFormattedInfo();
```

---

## 11. Array Over Collections

### PaymentProcessor.java

```java
private double[] amounts = new double[1000];
private String[] methods = new String[1000];
private String[] stats = new String[1000];
private long[] times = new long[1000];
private int tc = 0;  // Manual counter
```

**Problems:**
- Fixed capacity (1000) - what if we exceed?
- Manual indexing with `tc` counter
- No built-in methods for searching, sorting, filtering

**Better Approach:**
```java
List<Payment> payments = new ArrayList<>();
payments.add(new Payment(orderId, amount, method, timestamp));
```

### DeliveryHandler.java

```java
private int[] deliveryOrderIds = new int[1000];
private int[] deliveryMinutes = new int[1000];
private String[] deliveryStatuses = new String[1000];
private int deliveryCount = 0;
```

**Issue:** Parallel arrays instead of a single `Delivery` object.

```java
// Better:
List<Delivery> deliveries = new ArrayList<>();
deliveries.add(new Delivery(orderId, minutes, status));
```

---

## 12. String Concatenation Instead of StringBuilder

### PaymentProcessor.java - generateReceipt() (Lines 131-155)

```java
String receipt = "";
receipt = receipt + "================================\n";
receipt = receipt + "       PIZZA SHOP RECEIPT       \n";
receipt = receipt + "================================\n";
receipt = receipt + "Order #" + orderId + "\n";
receipt = receipt + "Customer: " + c.getN() + "\n";
// ... many more concatenations
```

**Issue:** Creates multiple intermediate String objects; inefficient for large strings.

**Better Approach:**
```java
StringBuilder receipt = new StringBuilder();
receipt.append("================================\n");
receipt.append("       PIZZA SHOP RECEIPT       \n");
// ... etc
return receipt.toString();
```

### PaymentProcessor.java - getTransactionHistory() (Lines 169-177)

```java
String history = "";
for (int i = 0; i < tc; i++) {
    history = history + "Trans #" + i + ": ";
    history = history + methods[i] + " ";
    history = history + "$" + amounts[i] + " ";
    history = history + stats[i] + "\n";
}
return history;
```

---

## 13. Null Checks Everywhere

### OrderManager.java - validate()

```java
if (p == null || c == null) return false;
if (p.getN() == null || p.getN().trim().isEmpty()) return false;
if (p.getSz() == null) return false;
if (p.getBp() <= 0) return false;
if (c.getN() == null || c.getN().trim().isEmpty()) return false;
if (c.getA() == null || c.getA().trim().isEmpty()) return false;
if (c.getP() == null || c.getP().trim().isEmpty()) return false;
```

### PaymentProcessor.java - calculateTotal()

```java
if (p == null || c == null) {
    return 0;
}
```

### DeliveryHandler.java - calculateDeliveryFee()

```java
if (p == null || c == null) {
    return 0;
}
```

**Better Approach:** Use Optional or Null Object pattern
```java
Pizza pizza = orderMap.getOrDefault(id, Pizza.NULL_PIZZA);
```

---

## 14. Dead Code

### RestaurantSystem.java

```java
private int ocount;  // Declared but never used or incremented
```

### OrderManager.java (implied in demo)

```java
int order2 = system.placeOrder(...);  // Variable assigned but never used
int order3 = system.placeOrderWithHot(...);  // Variable assigned but never used
```

---

## 15. Inconsistent Code Style

### String Comparisons

```java
// Sometimes: equalsIgnoreCase()
if (paymentMethod.equalsIgnoreCase("CREDIT_CARD")) { ... }

// Sometimes: equals()
if (deliveryStatuses[i].equals("ASSIGNED")) { ... }

// Sometimes: ==
if (orderStatuses[id] == 0) { ... }
```

### Collection vs Arrays

```java
// Maps for orders:
private Map<Integer, Pizza> orderMap;

// Arrays for delivery:
private int[] deliveryOrderIds = new int[1000];
```

**Issue:** No consistent pattern choice for storing data.

---

## 16. Violated SOLID Principles

### Single Responsibility Principle (SRP) ❌

**RestaurantSystem.java**
- Menu initialization
- Order placement
- Customer management
- Workflow orchestration

**OrderManager.java**
- Order storage and retrieval
- Price calculation
- Validation logic
- Order status management

**PaymentProcessor.java**
- Payment processing
- Receipt generation
- Transaction history
- Refund handling

### Open/Closed Principle (OCP) ❌

**Adding new pizza types** requires modifying RestaurantSystem.initializeMenuPizzas()

**Adding new payment methods** requires modifying PaymentProcessor.processPayment() with new if-else branch

**Adding new delivery statuses** requires finding and updating all hardcoded strings

### Liskov Substitution Principle (LSP) ❌

Payment types are created with hardcoded class instantiation:
```java
if (paymentMethod.equalsIgnoreCase("CREDIT_CARD")) {
    currentPaymentMethod = new CreditCardPayment();
} else if (paymentMethod.equalsIgnoreCase("CASH")) {
    currentPaymentMethod = new CashPayment();
}
```

Instead of a factory pattern that could support custom implementations.

### Interface Segregation Principle (ISP) ❌

**PaymentHandler.java** has 16+ methods:
```java
int processPayment(int orderId, String paymentMethod, double amount);
double calculateTotal(int orderId);
String generateReceipt(int orderId);
boolean refundPayment(int transactionIndex, String reason);
boolean validateCreditCard(double amount);
boolean validatePayPal(double amount);
boolean validateCash(double amount);
String getTransactionHistory();
int getTotalTransactions();
boolean sendEmailReceipt(int orderId, String emailAddress);
void sendPaymentConfirmation(int orderId);
String exportToCSV();
void exportTransactionsToFile(String filename);
String generateMonthlyReport(int month, int year);
double getTotalRevenue();
```

Clients must implement/depend on all methods even if they only need a subset.


etliche lehre methoden

### Dependency Inversion Principle (DIP) ❌

High-level modules depend on low-level modules:
```java
// RestaurantSystem depends directly on OrderManager
private OrderManager orderMgr = OrderManager.getInstance();

// PaymentProcessor depends directly on OrderManager
OrderManager om = OrderManager.getInstance();

// DeliveryHandler depends directly on OrderManager
private OrderManager orderMgr;
```

No abstraction layer (interfaces) between them.

---

## Summary Table

| Smell | Severity | Location | Count |
|-------|----------|----------|-------|
| Bad Naming | High | All classes | 20+ instances |
| Deep Nesting | High | OrderManager, DeliveryHandler, RestaurantSystem | 3 methods |
| Magic Numbers | High | DeliveryHandler, PaymentProcessor, Pizza | 15+ instances |
| DRY Violations | High | Price calculation, validation, null checks | 3+ locations |
| Primitive Obsession | High | OrderManager, DeliveryHandler, PaymentProcessor | 10+ instances |
| Long Methods | Medium | PaymentProcessor, RestaurantSystem | 3 methods |
| Tight Coupling | High | All classes | Throughout |
| Feature Envy | Medium | RestaurantSystem, PaymentProcessor | 2+ methods |
| Arrays Over Collections | Medium | PaymentProcessor, DeliveryHandler | 2 classes |
| String Concatenation | Low | PaymentProcessor | 2 methods |
| Null Checks | Medium | Multiple classes | 10+ instances |
| SOLID Violations | Critical | All classes | All principles |

---

## Recommendations for Refactoring

1. **Use Enums** for all status codes
2. **Extract Methods** to reduce nesting depth
3. **Replace Magic Numbers** with named constants or enums
4. **Apply Dependency Injection** to reduce coupling
5. **Use Collections** instead of fixed-size arrays
6. **Create Value Objects** (Payment, Delivery, OrderStatus)
7. **Implement Factory Pattern** for payment method creation
8. **Use Builder Pattern** for complex object creation
9. **Extract Price Calculation** to separate service
10. **Use Optional** instead of null checks

