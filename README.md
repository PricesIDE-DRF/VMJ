# Variability Modules for Java (VMJ)

## Introduction
Variability Modules for Java (VMJ) is a concept or architectural pattern in software development for implementing delta-oriented software product lines (SPL) in Java source code. There are several key components in developing delta-oriented SPL using VMJ, which are as follows:

### Core Module
- A module that contains **core**/**common** functionality for a variant or feature.
- For a variant or feature, there is only one core module.
- A core module consists of the following components:
    - Interface
    - Abstract component class (implements the interface)
    - Concrete component class
    - Abstract decorator class (extends the abstract component class)
- Name format: `<product-line-name>.<feature-name>.core`.

### Delta Module
- A module that contains unique and specialized functionality
- Used to modify the core module to create different products. This module can make either major or minor changes and has rules for when it can be used based on the features desired by the user. Can add, modify, or delete properties and methods from the core module or other delta modules.
- The process of wrapping other modules (core/delta) is done using the decorator pattern concept.
- Name format: `<product-line-name>.<feature-name>.<delta-name>`.
- Why is it called a **delta**? Recall that the delta symbol, which is shaped like a triangle in the context of physics and chemistry, signifies a difference.
<p align="center">
  <img src="images/delta-symbol.png" alt="Delta Symbol">
</p>

### Factory Class
- Functions to create the appropriate objects during the product derivation phase.
- The product derivation phase of SPLE is implemented by creating Java modules that represent new products.
- Also responsible for handling the application order of deltas.
    - If there are multi-level deltas, then the order becomes very important.
    - In UML, the order in the multi-level delta concept can be visualized using the `<<after>>` stereotype.
- Name format: `<name-interface>`Factory.
    - For example:
        - Interface name: Promo
        - Factory class name: PromoFactory
- There is a template to generate the factory class so that the factory class will be created automatically.

### Java Module
- Java modules can be used as core modules, delta modules, and product modules.
- Name format: `<product-line-name>.<feature-name>.<module-name>`.
- Each Java module will have a module-info.java file.
    - Functions to define the Java packages that are exported and the external modules that are imported (required).
    - Pattern:
        ```
        module <product-line-name>.<feature-name>.<module-name> {
            requires <product-line-name>.<feature-name>.<module-X-name>;
            requires <product-line-name>.<feature-name>.<module-Y-name>;
            requires <external-module-1-name>;  // Specifically for core modules and delta modules
            exports module <product-line-name>.<feature-name>.<module-name>;  // Specifically for core modules and delta modules
        }
        ```

## Structure Directory
- Folder `external`: Contains required external library (jar file).
- Folder `images`: Contains images used in README.md.
- Folder `lib`: Contains generated jar files from the selected core modules or delta modules.
- Folder `paymentgateway.product.<product-module-name>`: Contains generated jar files from the modules required by a product.
- Folder `src`: Contains the implementation of delta-oriented software product lines, including core modules, delta modules, and product modules.
- File `ck.properties`: Contains configuration knowledge.
- File `genfactory.sh`: Contains a script to generate a factory class for a feature with the help of the `TemplateFactory.java` file.
- File `genproduct.sh`: Contains a script to generate jar files from the modules required by a product or jar files from the selected core modules or delta modules.
- File `run.sh`: Contains a script to run the generated product jar files.
- File `TemplateFactory.java`: Template for feature factory class.

## How to Create a Factory Class Using the Factory Class Template
**Requirement**: Java 11 or later
**Factory Class Template**: `TemplateFactory.java`
```
bash genfactory.sh <product-line-name>.<feature-name> <feature-interface-name>
```
Example Feature Payment:
```
bash genfactory.sh paymentgateway.payment Payment
```

## How to Build and Run (Product Generation) Using Bash Script
### Build Product
```
bash genproduct.sh <product-line-name>.product.<product-module-name> <product-main-class>
```
Example Product Basic:
```
bash genproduct.sh paymentgateway.product.basic Basic
```

### Run Product
```
run.sh <product-line-name>.product.<product-module-name>
```
Example Product Basic:
```
bash run.sh paymentgateway.product.basic
```
### Build Module (If you want to build any module (one by one))
```
bash genproduct.sh <product-line-name>.<feature-name>.<module-name>
```
Example Payment Core Module:
```
bash genproduct.sh paymentgateway.payment.core
```

## Example: The "Payment Gateway" Case Study
- There is only one feature, which is payment.
    - There is 1 core module and 3 delta modules.
    - **Core**: Contains the elements needed for payment processing (core payment processing). It is located in the `paymentgateway.payment.core` module.
    - **Delta multi-currency support**: Supports payments in various currencies, modifies behavious the `processPayment` method. Located in the `paymentgateway.payment.multicurrencysupport` module.
    - **Delta integration with local banks**: Supports integration with various local banks in different countries, adds behavior to the `processPayment` method. Therefore, the `delta multi-currency support` must also be applied beforehand. Located in the `paymentgateway.payment.localbank` module.
    - **Delta fraud detection**: Supports the detection and prevention of suspicious transactions, adds behavior to the `processPayment` method. Located in the `paymentgateway.payment.frauddetection` module.
- There are 3 products: basic payment gateway, e-commerce payment gateway, and marketplace payment gateway.
    - **Basic payment gateway**: `Core` + `delta multi-currency support`. Located in `paymentgateway.product.basic` module.
    - **E-commerce payment gateway**: `Core` + `delta multi-currency support` + `delta fraud detection`. Located in `paymentgateway.product.ecommerce` module.
    - **Marketplace payment gateway**: `Core` + `delta multi-currency support` + `delta fraud detection` + `delta integration with local banks`. Located in `paymentgateway.product.marketplace` module.