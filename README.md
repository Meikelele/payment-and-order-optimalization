<div align="center">
<h1 align="center">Payment Order Optimalization</h1>

  <p align="center">
    A CLI app for maximizing payment discounts by choosing between loyalty points (prioritize) and credit-card promotions for a list of orders. 
  </p>
</div>


## Schedule
  - [Project Goal](#project-goal)
  - [Tech Stack](#tech-stack)
  - [Installation](#installation)
  - [Necessary files](#necessary-files)
  - [Requirements](#requirements)
  - [How it works](#how-it-works)
  - [Tests](#tests)

## Project Goal
<p>The goal of this project is to develop an algorithm that, given a list of orders, available promotions, and a customer wallet (payment methods with limits and discounts), selects the optimal way to pay for each order to maximize the total discount while fully covering all orders. The algorithm should minimize credit-card spending by preferring loyalty points when it does not reduce the applicable discount.</p>



## Tech Stack
![Java17](https://img.shields.io/badge/-Java17-ffffff?style=flat-square&logo=openjdk&logoColor=000000) 
![Maven](https://img.shields.io/badge/-ApacheMaven-C71A36?style=flat-square&logo=apachemaven&logoColor=000000)
![Lombok](https://img.shields.io/badge/-Lombok-FFFF66?style=flat-square)
![Jackson](https://img.shields.io/badge/-JacksonDatabind-FF9900?style=flat-square) 
![JUnit5](https://img.shields.io/badge/-JUnit5-25A162?style=flat-square&logo=junit5&logoColor=000000)



## Installation

Clone the repository 
```
git clone https://github.com/Meikelele/payment-and-order-optimalization
```
Go to the proper directory
```
cd junior-optimalization-task
```
Build fat-jar
```
mvn clean package
```
Run a fat-jar
```
java -jar target/juniorPaymentAndOrderOptimize-1.0.1.jar orders.json paymentmethods.json
```


## Requirements
- [ ] Pay each order by **one** of:
  - full loyalty points  
  - full single card  
  - partial (≥10 % points + one card)
- [ ] Apply **card discount** only for cards in the order’s promotions list  
- [ ] Apply **10 % discount** if ≥10 % of the order is paid in points  
- [ ] **Track and decrement** global points balance and each card’s limit  
- [ ] **Maximize total discount**; on ties, prefer scenarios using more points (less card usage)  



## Necessary files
<table>
  <tr>
    <td valign="top">
      
## orders.json
      
JSON file containing the list of orders.

![orders.json](https://github.com/user-attachments/assets/11690193-55be-478e-be08-a454ca1382ea)

**Fields:**
- <mark>**id**</mark>: unique order identifier
- <mark>**value**</mark>: order amount as string, two decimals
- <mark>**promotions**</mark>: (_optional_ array) of card IDs eligible for a discount
    </td>
    <td valign="top">
    
## paymentmethods.json

JSON file containing the list of payment methods.

![paymentmethods.json](https://github.com/user-attachments/assets/e50e24cc-dfc9-4de5-9590-6e32b83c1a61)

**Fields:**
- <mark>**id**</mark>: payment method identifier
- <mark>**discount**</mark>: discount percentage as string
- <mark>**limit**</mark>: available points or card spending limit as string
    </td>
  </tr>
</table>



## How It Works
1. **JSON parsing** 
    - Jackson `ObjectMapper` reads `List<Order>` and `List<PaymentMethod>` from the two input files.  
2. **Init**
    - Init points balance and cards limits.  
3. **`optimizeOrder(...)`**  by trying:
    - full points
    - partial (10 % points + one card)
    - full card  
Pick the option with the highest discount (tie? loyal points are prioritize)  
4. **Update**
    -  decrement global points and the chosen card’s limit, and accumulate usage
6. **Output** totals, e.g.:
   ```
     PUNKTY 100.00
     mZysk  165.00
     BosBankrut 190.00
     ```
   


## Tests

- **Approach:** AAA (Arrange – Act – Assert)  
- **AllocationTest**  
  - verifies both constructors and all getters  
- **OrderAndPayOptAppTest** covers `optimizeOrder` scenarios:  
  - full payment with points  
  - full payment with promo card  
  - partial points + card  
  - tie-breaker on equal discounts  
  - no valid option → returns `null`
