# Bingo Points Found in Codebase

This document lists all the bingo points (code quality issues) found in the codebase according to the bingo card.

## 1. SRP (Single Responsibility Principle) Violations ✅

### RestaurantSystem.java
- **Lines 5-248**: Handles multiple responsibilities:
  - Menu management (initializeMenuPizzas, displayMenu)
  - Order placement (placeOrder, placeOrderWithOptions)
  - Customer management (customers map)
  - Workflow orchestration (processWorkflow)
  - Interactive UI (startInteractiveMode)

### OrderManager.java
- **Lines 4-200**: Handles multiple responsibilities:
  - Order storage and retrieval (newOrder, getOrder)
  - Price calculation (getTotalWithTax)
  - Validation (validate)
  - Formatting (formatOrder, printAllOrders)
  - Status management (updateStatus, prepareOrder, bakeOrder, etc.)

### PaymentProcessor.java
- **Lines 2-222**: Handles multiple responsibilities:
  - Payment processing (processPayment)
  - Total calculation (calculateTotal)
  - Receipt generation (generateReceipt)
  - Transaction history (getTransactionHistory)
  - Refund processing (refundPayment)

## 2. OCP (Open/Closed Principle) Violations ✅

### RestaurantSystem.java
- **Lines 42-70**: Adding new pizza types requires modifying `initializeMenuPizzas()` method directly
- **Lines 154-157**: Hardcoded pizza type array - must modify to add new types

### PaymentProcessor.java
- **Lines 34-68**: Adding new payment methods requires modifying the huge if-else chain in `processPayment()`
  - Must add new `else if` branch for each payment method
  - Violates open/closed principle

### OrderManager.java
- **Lines 137-148**: Status codes are hardcoded in `getStatusString()` - adding new statuses requires modification
- **Line 54**: Magic numbers for status codes (0=received, 1=preparing, etc.) - should use enums

## 3. LSP (Liskov Substitution Principle) Violations ✅

### PaymentMethod Subclasses
- **CreditCardPayment.java (line 12-19)**: `processPayment()` returns 0.0 for negative amounts
- **PayPalPayment.java (line 12-20)**: `processPayment()` allows negative amounts (for refunds)
- **CashPayment.java (line 9-16)**: `processPayment()` throws exception for negative/zero amounts
- **Inconsistent behavior**: Subclasses cannot be substituted without breaking behavior

### PaymentMethod.java
- **Line 13**: `validateAmount()` returns `amount >= 0` but subclasses override with different logic
- CashPayment requires `amount > 0`, PayPalPayment accepts any amount

## 4. ISP (Interface Segregation Principle) Violations ✅

### PaymentHandler.java
- **Lines 1-24**: Interface has 13 methods - too many responsibilities
- Clients must implement all methods even if they don't use them:
  - `sendEmailReceipt()` (line 16)
  - `sendPaymentConfirmation()` (line 17)
  - `exportToCSV()` (line 19)
  - `exportTransactionsToFile()` (line 20)
  - `generateMonthlyReport()` (line 22)

### PaymentProcessor.java
- **Lines 189-210**: Implements many methods with empty/default implementations:
  - `sendEmailReceipt()` returns false (line 191)
  - `sendPaymentConfirmation()` empty (line 195)
  - `exportToCSV()` returns empty string (line 200)
  - `exportTransactionsToFile()` empty (line 204)
  - `generateMonthlyReport()` returns empty string (line 209)

## 5. Dependency Inversion Principle Violations ✅

### Direct Singleton Dependencies
- **RestaurantSystem.java (line 13)**: `this.om = OrderManager.getInstance();` - direct dependency on concrete class
- **DeliveryHandler.java (line 10)**: `this.om = OrderManager.getInstance();` - direct dependency
- **PaymentProcessor.java (lines 28, 99, 120)**: `OrderManager.getInstance();` - direct dependency multiple times

### No Abstraction Layer
- All classes depend directly on `OrderManager` singleton instead of an interface
- High-level modules (RestaurantSystem, PaymentProcessor) depend on low-level details (OrderManager)
- Tight coupling throughout the system

## 6. Schlechte Namen (Bad Names) ✅

| Name |  Class | Meaning |
|----------|----------|-------|---------|
| `n`  | Pizza, Customer, Ingredient | name |
| `sz` | Pizza | pizzaSize |
| `bp` | Pizza | basePrice |
| `ing` | Pizza | ingredientList |
| `cr` | Pizza | createdTime |
| `st` | Pizza | pizzaStatus |
| `pt` | Pizza | prepTime |
| `m` | PizzaSize | priceMultiplier |
| `d` | PizzaSize | diameterCm |
| `a` | Ingredient | isAvailable |
| `s` | Ingredient, PizzaSize | stockLevel/sizeLabel |
| `p` | Customer | phoneNum |
| `e` | Customer | emailAddr |
| `lc`| Customer | loyaltyCount |
| `tpr` | Customer | totalSpent |
| `tc`| PaymentProcessor | transactionCount |

## 7. Schlechte Kommentare (Bad Comments) ✅

### Ingredient.java
- **Line 44**: `// checks if ingredient is available` - too obvious, method name already says this

### RestaurantSystem.java
- **Line 21**: `// Initialize ingredients and menu` - too obvious
- **Line 27**: `// Setup ingredients with costs` - too obvious
- **Line 41**: `// Build pizza menu with ingredients` - too obvious
- **Line 72**: `// Helper to create menu items` - too obvious
- **Line 151**: `// Display menu with item codes` - too obvious
- **Line 168**: `// Show orders with formatting` - too obvious
- **Line 175**: `// Interactive mode` - too obvious

### OrderManager.java
- **Line 52**: `// Process order status` - too obvious
- **Line 70**: `// Calculate total with tax` - too obvious
- **Line 78**: `// Apply loyalty discount if customer has more than 5 orders` - states what, not why

### Pizza.java
- **Line 83**: `// Calculate total price with ingredients and size multiplier` - too obvious
- **Line 87**: `// Apply size multiplier` - too obvious
- **Line 96**: `// Add ingredient costs` - too obvious
- **Line 101**: `// Hot pizza surcharge` - too obvious
- **Line 109**: `// Check if all ingredients are available` - too obvious
- **Line 121**: `// Reduce ingredient stock` - too obvious

### DeliveryHandler.java
- **Line 49**: `// Base fee` - too obvious
- **Line 52**: `// Add surcharge for large pizzas` - too obvious
- **Line 57**: `// Additional fee if hot pizza` - too obvious
- **Line 62**: `// Hidden business rule: discount for loyal customers` - good comment but reveals hidden logic issue
- **Line 75**: `// simulate delivery time passing` - too obvious

### PaymentProcessor.java
- **Line 19**: `// PayPal fee` - too obvious
- **Line 20**: `// Normal payment` - vague and unhelpful

## 8. DRY (Don't Repeat Yourself) Violations ✅

### Price Calculation Repeated
- **Pizza.java (lines 84-107)**: `calculateFinalPrice()` - calculates price with size multiplier and ingredients
- **OrderManager.java (lines 71-87)**: `getTotalWithTax()` - calculates subtotal using `pizza.calculateFinalPrice()` then adds tax
- **PaymentProcessor.java (lines 98-117)**: `calculateTotal()` - calculates subtotal using `pizza.calculateFinalPrice()` then adds tax and delivery
- **DeliveryHandler.java (lines 41-68)**: `calculateDeliveryFee()` - has similar fee calculation logic

### Validation Logic Repeated
- **OrderManager.java (lines 89-116)**: `validate()` - validates pizza and customer
- **DeliveryHandler.java (lines 111-121)**: `validateForDelivery()` - almost identical validation logic
  - Both check: `p == null || c == null`
  - Both check: `p.getN() == null || p.getN().trim().isEmpty()`
  - Both check: `c.getN() == null || c.getN().trim().isEmpty()`
  - Both check: `c.getA() == null || c.getA().trim().isEmpty()`

### Pizza Creation Duplicated
- **RestaurantSystem.java (lines 73-81)**: `createMenuPizza()` - creates pizza and adds ingredients
- **RestaurantSystem.java (lines 97-102)**: `placeOrderWithOptions()` - duplicates ingredient copying logic
  ```java
  for (Ingredient ing : menuPizza.getIng()) {
      orderPizza.addIng(ing);
  }
  ```

### Status Checking Repeated
- **OrderManager.java (lines 104-114)**: Complex nested status checking with magic numbers
- **DeliveryHandler.java (lines 78-82)**: Similar status checking: `om.getStatus(dIds[i]) == 4`
- **RestaurantSystem.java (lines 126-138)**: Multiple status checks: `om.getStatus(orderId) == 1`, `== 2`, `== 3`

### String Concatenation Pattern Repeated
- **PaymentProcessor.java (lines 128-150)**: Multiple `receipt = receipt + ...` statements
- **DeliveryHandler.java (lines 100-104)**: Multiple `result = result + ...` statements
- **Pizza.java (lines 130-142, 145-160)**: Multiple string concatenations

### Tax Calculation Repeated
- **OrderManager.java (line 76)**: `double tax = subtotal * 0.085;` - magic number
- **PaymentProcessor.java (line 108)**: `double tax = subtotal * TAX_RATE;` - uses constant but same calculation
- **PaymentProcessor.java (line 140)**: `p.calculateFinalPrice() * TAX_RATE` - repeated calculation

## 9. KISS (Keep It Simple, Stupid) Violations ✅

### Deep Nesting in DeliveryHandler.java
- **Lines 70-89**: `processDeliveries()` has 5 levels of nesting:
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

### Complex Conditionals in OrderManager.java
- **Lines 104-114**: Deeply nested validation logic:
  ```java
  if (st[id] == 0) {
      if (p.isH()) {
          if (p.getSz().getD() > 30) {
              if (tm.get(id) > 0) {
                  return true;
              }
          }
      } else {
          return true;
      }
  }
  ```

### Deep Nesting in RestaurantSystem.java
- **Lines 119-149**: `processWorkflow()` has 4 levels of nesting with multiple status checks

### Magic Numbers Everywhere
- **OrderManager.java**:
  - `0.085` (line 76) - tax rate, should be constant
  - `0.1` (line 80) - discount percentage, should be constant
  - `5` (line 79) - loyalty threshold, should be constant
  - `0, 1, 2, 3, 4, 5, 6` - status codes, should be enum
- **PaymentProcessor.java**:
  - `1000` (line 74) - max amount, should be constant
  - `0.05` (line 78) - random threshold, should be constant
  - `1.02` (CreditCardPayment line 19) - fee multiplier
  - `1.03` (PayPalPayment line 20) - fee multiplier
- **DeliveryHandler.java**:
  - `3.00` (line 50) - base fee
  - `1.50` (line 54) - large pizza surcharge
  - `2.00` (line 59) - hot pizza surcharge
  - `0.8` (line 64) - discount multiplier
  - `25, 5, 3, 2` (lines 135-145) - time estimate thresholds
- **Pizza.java**:
  - `1.0, 0.75, 1.3` (lines 88-93) - size multipliers
  - `1.1` (line 103) - hot pizza multiplier
- **RestaurantSystem.java**:
  - `1, 2, 3, 4, 5` (lines 193-220) - menu choice numbers

### Unnecessary Complexity
- **PaymentProcessor.java (lines 23-71)**: `processPayment()` method is 48 lines with complex nested if-else logic
- **OrderManager.java (lines 118-135)**: `formatOrder()` builds string with multiple concatenations instead of using StringBuilder
- **Pizza.java (lines 130-142, 145-160)**: String building with repeated concatenation instead of StringBuilder

### Primitive Obsession
- Using `int` for status instead of Enum (OrderManager.java line 10)
- Using `String` for status instead of Enum (DeliveryHandler.java line 5, Pizza.java line 9)
- Using array indices for data access instead of proper data structures

---

## Summary

All 9 bingo points have been found in the codebase:
1. ✅ SRP Violations - Multiple classes with multiple responsibilities
2. ✅ OCP Violations - Adding features requires modifying existing code
3. ✅ LSP Violations - Inconsistent behavior in PaymentMethod subclasses
4. ✅ ISP Violations - PaymentHandler interface too large
5. ✅ Dependency Inversion Violations - Direct singleton dependencies
6. ✅ Bad Names - Abbreviated variable names throughout
7. ✅ Bad Comments - Obvious comments that don't add value
8. ✅ DRY Violations - Repeated code in multiple places
9. ✅ KISS Violations - Complex nested logic and magic numbers

**BINGO!** 🎉

