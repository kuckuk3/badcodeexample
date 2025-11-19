# Pizza Ordering System - Bad Code Example

## Overview
This is an intentionally poorly written pizza ordering system that demonstrates various code smell violations and anti-patterns. It's designed as a teaching tool to show what NOT to do.

## Structure

### Files Created:
1. **Pizza.java** - Main pizza class (refactored naming)
2. **PizzaSize.java** - Pizza size information (refactored naming)
3. **Customer.java** - Customer data (refactored naming)
4. **Ingredient.java** - Ingredient details (refactored naming)
5. **OrderManager.java** - Order management (singleton, refactored naming)
6. **DeliveryHandler.java** - Delivery scheduling (singleton, refactored naming)
7. **PaymentMethod.java** - Abstract base class for payment types
8. **PaymentHandler.java** - Interface for payment processing
9. **PaymentProcessor.java** - Payment processing (implements PaymentHandler)
10. **CashPayment.java** - Concrete implementation for cash payments
11. **CreditCardPayment.java** - Concrete implementation for credit card payments
12. **PayPalPayment.java** - Concrete implementation for PayPal payments
13. **RestaurantSystem.java** - Main system orchestrator (refactored naming)

## Anti-Patterns Demonstrated

### 1. SOLID Violations

#### Single Responsibility Principle (SRP) ❌
- **RestaurantSystem**: Handles menus, orders, customer management, and workflow
- **OrderManager**: Manages orders AND calculates prices AND validates AND formats
- **PaymentProcessor**: Calculates totals AND processes payments AND generates receipts

#### Open/Closed Principle (OCP) ❌
- Adding new pizza types requires modifying menu initialization
- Adding new payment methods requires modifying huge if-else chains in `processPayment()`
- New delivery statuses mean modifying hardcoded status strings everywhere

#### Liskov Substitution Principle (LSP) ❌
- No interfaces/abstract classes - everything is concrete
- Can't substitute implementations

#### Interface Segregation Principle (ISP) ❌
- Classes expose too many methods
- Clients depend on methods they don't use

#### Dependency Inversion Principle (DIP) ❌
- **Direct dependencies**: RestaurantSystem → OrderManager → specific classes
- **No abstraction layer**: High-level modules depend on low-level details
- Tight coupling throughout

### 2. Bad Naming Conventions

| Bad Name | What it Actually Is | Class |
|----------|-------------------|-------|
| `n` | name | Pizza, Customer, Ingredient |
| `sz` | size | Pizza |
| `bp` | basePrice | Pizza |
| `ing` | ingredients | Pizza |
| `cr` | createdAt | Pizza |
| `st` | status | Pizza |
| `pt` | preparationTime | Pizza |
| `m` | multiplier | PizzaSize |
| `d` | diameter | PizzaSize |
| `a` | available | Ingredient |
| `s` | stock/size | Ingredient, PizzaSize |
| `p` | phone/pizza | Customer |
| `e` | email | Customer |
| `lc` | loyaltyCount | Customer |
| `tpr` | totalPriceRun | Customer |
| `om` | orderManager | OrderManager, RestaurantSystem |
| `cm` | customerMap | OrderManager |
| `tm` | timestampMap | OrderManager |
| `odc` | orderCounter | OrderManager |
| `dIds` | deliveryIds | DeliveryHandler |
| `dTimes` | deliveryTimes | DeliveryHandler |
| `dStatuses` | deliveryStatuses | DeliveryHandler |
| `dc` | deliveryCount | DeliveryHandler |
| `tc` | transactionCount | PaymentProcessor |

**Anti-Pattern:** Method names like `getN()`, `setN()`, `getA()`, `setA()` with abbreviated method names combined with abbreviated field names make code hard to understand.

### 3. Bad Comments

```java
// These comments are either too obvious or not helpful:

// variable1 - this is for the name          ← Too obvious
// add a new pizza order to the array        ← States what code does, not WHY
// checks if ingredient is available         ← Obvious from method name
// TODO: implement delivery logic            ← Incomplete information
// Do something here                         ← Meaningless
// Method to do stuff with price             ← Vague and unhelpful
```

### 4. DRY Violations (Don't Repeat Yourself)

**Price Calculation repeated 3+ times:**
```java
// In Pizza.java: calculateFinalPrice()
// In OrderManager.java: getTotalWithTax()
// In PaymentProcessor.java: calculateTotal()
// In DeliveryHandler.java: calculateDeliveryFee() includes similar logic
```

**Pizza creation duplicated:**
```java
// RestaurantSystem.createMenuPizza() creates pizzas
// RestaurantSystem.placeOrderWithOptions() duplicates ingredients copy logic
```

**Validation logic repeated:**
```java
// OrderManager.validate()
// DeliveryHandler.validateForDelivery() - almost identical copy
```

**Status checking:**
```java
// Multiple places check status like: st[id] == 0, st[i] == 1, etc.
// Magic numbers used instead of constants or enums
```

### 5. KISS Violations (Keep It Simple, Stupid)

**Deep nesting in processDeliveries():**
```java
for (int i = 0; i < dc; i++) {
    if (dStatuses[i] != null) {
        if (dStatuses[i].equals("ASSIGNED")) {
            for (int j = 0; j < dTimes[i]; j++) {
                if (j == dTimes[i] - 1) {
                    if (om.validate(dIds[i])) {
                        if (om.getStatus(dIds[i]) == 4) {
                            // Finally do something
                        }
                    }
                }
            }
        }
    }
}
```

**Complex conditionals in validate():**
```java
if (st[id] == 0) {
    if (p.isH()) {
        if (p.getSz().getD() > 30) {
            if (tm.get(id) > 0) {
                return true;  // Hidden logic, hard to understand
            }
        }
    } else {
        return true;
    }
}
```

**Magic numbers everywhere:**
```java
- 0.085 (tax rate) - what does this mean?
- 2.5 (delivery surcharge) - hardcoded
- 25, 5, 3, 2 - unclear thresholds for time estimates
- Status codes (0=received, 1=preparing, etc.) - should be enums
```

### 6. Other Code Smells

#### Primitive Obsession
- Using `int` for status instead of Enum
- Using `String` for status instead of Enum
- Array indices for data access

#### Long Method
- `processPayment()` method is 35+ lines with multiple responsibilities
- `calculateTotal()` has hidden business rules scattered throughout

#### Dead Code
```java
private int ocount;  // Declared but never used
int order2 = system.placeOrder(...);  // Assigned but never used
int order3 = system.placeOrderWithHot(...);  // Assigned but never used
```

#### Tight Coupling
- OrderManager is a Singleton - hard to test
- RestaurantSystem directly instantiates OrderManager
- Dependencies flow downward instead of being injected

#### Feature Envy
- `RestaurantSystem.placeOrderWithOptions()` accesses too many Pizza and Customer internals
- Methods know too much about each other's data

#### Null Checks Everywhere
```java
if (p == null || c == null) return 0;
if (p.getN() == null || p.getN().trim().isEmpty()) return false;
// Repeated in multiple classes
```

#### Inconsistent Code Style
- Mixed naming patterns (camelCase vs underscores)
- Inconsistent method formatting
- Sometimes using maps, sometimes arrays, no consistency

#### Hard to Test
- Singleton pattern
- Static references
- No interfaces
- Deep nesting and dependencies

## How to Improve This Code

1. **Apply SOLID Principles**
   - Extract interfaces for each responsibility
   - Use dependency injection
   - Replace Singleton with proper DI container

2. **Use Meaningful Names**
   - `price` instead of `bp`
   - `pizzaSize` instead of `sz`
   - `deliveryId` instead of `dIds`

3. **Replace Magic Numbers with Constants**
   ```java
   private static final double TAX_RATE = 0.085;
   private static final int READY_STATUS = 4;
   ```

4. **Use Enums instead of String/int status codes**
   ```java
   public enum OrderStatus {
       RECEIVED, PREPARING, BAKING, QUALITY_CHECK, READY, DELIVERED, CANCELLED
   }
   ```

5. **Extract Helper Methods**
   - Break down long methods
   - Reduce nesting depth
   - Create reusable validation methods

6. **Use Collections properly**
   - Replace arrays with List, Map
   - Use proper data structures

7. **Remove Duplicated Code**
   - Create shared validation utility
   - Centralize price calculation
   - DRY principle

8. **Add Proper Comments**
   - Explain WHY, not WHAT
   - Document business rules
   - Add examples for complex logic

## Compilation Status
✓ All files compile successfully (with expected unused variable warnings for `ocount`)
