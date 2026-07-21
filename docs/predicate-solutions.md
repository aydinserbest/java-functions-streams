# Predicate Alıştırmaları — Örnek Çözümler

Bu dosya aynı 10 requirement'ın iki farklı kod organizasyonuyla çözümünü gösterir.
Önce kendin denemen, sonra buradaki çözümlerle karşılaştırman daha faydalıdır.

## Yaklaşım 1 — Tek sınıfta ayrı metotlar

Bu yaklaşımda bütün alıştırmalar tek demo sınıfındadır. `main`, örnekleri sırayla
çağırır; her requirement'ın kodu ayrı bir metotta tutulur. Küçük bir öğrenme
projesinde bütün Predicate örneklerini yan yana görmek için uygundur.

```java
package org.practice.javacore.lambda.exercises;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

public class PredicateExercisesSingleClass {

    public static void main(String[] args) {
        usernameValidation();
        numberChecks();
        activeUserFilter();
        expensiveInStockProducts();
        dynamicOrderFilter();
        emailValidation();
        candidateFilter();
        customerSearch();
        overdueInvoiceFilter();
        dynamicProductSearch();
    }

    // 1. Predicate tek bir kullanıcı adını test eder ve boolean döndürür.
    private static void usernameValidation() {
        Predicate<String> isValidUsername = username ->
                username != null
                && !username.isBlank()
                && username.length() >= 5;

        System.out.println(isValidUsername.test("john"));   // false
        System.out.println(isValidUsername.test("mehmet")); // true
        System.out.println(isValidUsername.test(""));       // false
        System.out.println(isValidUsername.test(null));     // false
    }

    // 2. Metot koşulu bilmez; kendisine gönderilen davranışı test() ile çalıştırır.
    private static boolean checkNumber(
            int number,
            Predicate<Integer> condition
    ) {
        return condition.test(number);
    }

    private static void numberChecks() {
        System.out.println(checkNumber(10, number -> number > 0));
        System.out.println(checkNumber(10, number -> number % 2 == 0));
        System.out.println(checkNumber(10, number -> number >= 10 && number <= 100));
        System.out.println(checkNumber(10, number -> number % 3 == 0));
    }

    // 3. filter, Predicate'i listedeki her User için çalıştırır.
    private static void activeUserFilter() {
        List<User> users = List.of(
                new User("Alice", true),
                new User("Bob", false),
                new User("Charlie", true),
                new User("David", false),
                new User("Eva", true)
        );

        Predicate<User> isActive = User::active;
        List<User> activeUsers = users.stream().filter(isActive).toList();
        System.out.println(activeUsers);
    }

    // 4. Her Predicate tek bir kural taşır; and() iki kuralı birleştirir.
    private static void expensiveInStockProducts() {
        List<StockProduct> products = List.of(
                new StockProduct("Laptop", 1200, 5),
                new StockProduct("Mouse", 40, 20),
                new StockProduct("Monitor", 300, 0),
                new StockProduct("Keyboard", 150, 3)
        );

        Predicate<StockProduct> isExpensive = product -> product.price() > 100;
        Predicate<StockProduct> isInStock = product -> product.stock() > 0;

        List<StockProduct> result = products.stream()
                .filter(isExpensive.and(isInStock))
                .toList();

        System.out.println(result);
    }

    // 5. Aynı filtreleme metodu farklı sipariş koşullarıyla tekrar kullanılır.
    private static List<Order> filterOrders(
            List<Order> orders,
            Predicate<Order> condition
    ) {
        return orders.stream().filter(condition).toList();
    }

    private static void dynamicOrderFilter() {
        List<Order> orders = List.of(
                new Order(1, "PAID", 250),
                new Order(2, "CANCELLED", 80),
                new Order(3, "SHIPPED", 650),
                new Order(4, "NEW", 900),
                new Order(5, "SHIPPED", 120)
        );

        Predicate<Order> isPaid = order -> order.status().equals("PAID");
        Predicate<Order> isCancelled = order -> order.status().equals("CANCELLED");
        Predicate<Order> amountOver500 = order -> order.totalAmount() > 500;
        Predicate<Order> isShipped = order -> order.status().equals("SHIPPED");
        Predicate<Order> amountOver100 = order -> order.totalAmount() > 100;

        System.out.println(filterOrders(orders, isPaid));
        System.out.println(filterOrders(orders, isCancelled));
        System.out.println(filterOrders(orders, amountOver500));
        System.out.println(filterOrders(orders, isShipped.and(amountOver100)));
    }

    // 6. indexOf('@') ile @ işaretinin konumunu bulup noktayı ondan sonra arıyoruz.
    private static void emailValidation() {
        Predicate<String> isValidEmail = email -> {
            if (email == null || email.isBlank() || email.contains(" ")) {
                return false;
            }

            int atIndex = email.indexOf('@');
            return atIndex > 0 && email.indexOf('.', atIndex + 1) > atIndex + 1;
        };

        System.out.println(isValidEmail.test("john@example.com")); // true
        System.out.println(isValidEmail.test("johnexample.com")); // false
        System.out.println(isValidEmail.test("john@example"));     // false
        System.out.println(isValidEmail.test("john @example.com")); // false
        System.out.println(isValidEmail.test(null));               // false
    }

    // 7. negate(), mevcut Predicate'in boolean sonucunu tersine çevirir.
    private static void candidateFilter() {
        List<Candidate> candidates = List.of(
                new Candidate("Alice", 3, true, true),
                new Candidate("Bob", 1, true, true),
                new Candidate("Charlie", 5, false, true),
                new Candidate("David", 4, true, false)
        );

        Predicate<Candidate> hasExperience = candidate ->
                candidate.yearsOfExperience() >= 2;
        Predicate<Candidate> knowsJava = Candidate::knowsJava;
        Predicate<Candidate> isFullTimeAvailable = Candidate::availableForFullTime;

        Predicate<Candidate> isSuitable = hasExperience
                .and(knowsJava)
                .and(isFullTimeAvailable);

        System.out.println(candidates.stream().filter(isSuitable).toList());
        System.out.println(candidates.stream().filter(knowsJava.negate()).toList());
    }

    // 8. search değişmez; arama davranışı Predicate parametresiyle değişir.
    private static List<Customer> search(
            List<Customer> customers,
            Predicate<Customer> criteria
    ) {
        return customers.stream().filter(criteria).toList();
    }

    private static void customerSearch() {
        List<Customer> customers = List.of(
                new Customer("Alice", "Netherlands", 30, true),
                new Customer("Bob", "Belgium", 17, false),
                new Customer("Charlie", "Netherlands", 55, false),
                new Customer("David", "Germany", 60, true)
        );

        Predicate<Customer> livesInNetherlands = customer ->
                customer.country().equals("Netherlands");
        Predicate<Customer> isAdult = customer -> customer.age() > 18;
        Predicate<Customer> isPremium = Customer::premium;
        Predicate<Customer> isOlderThan50 = customer -> customer.age() > 50;

        System.out.println(search(customers, livesInNetherlands));
        System.out.println(search(customers, isAdult));
        System.out.println(search(customers, isPremium));
        System.out.println(search(customers, livesInNetherlands.and(isPremium)));
        System.out.println(search(customers, isPremium.or(isOlderThan50)));
    }

    // 9. isBefore(today), yalnızca bugünden önceki tarihleri geçmiş kabul eder.
    private static void overdueInvoiceFilter() {
        LocalDate today = LocalDate.now();
        List<Invoice> invoices = List.of(
                new Invoice(1, today.minusDays(10), false, 1500),
                new Invoice(2, today.minusDays(2), true, 300),
                new Invoice(3, today.plusDays(5), false, 2000),
                new Invoice(4, today.minusDays(1), false, 200)
        );

        Predicate<Invoice> isOverdue = invoice -> invoice.dueDate().isBefore(today);
        Predicate<Invoice> isUnpaid = invoice -> !invoice.paid();
        Predicate<Invoice> amountOver1000 = invoice -> invoice.amount() > 1000;

        Predicate<Invoice> needsPaymentReminder = isOverdue.and(isUnpaid);

        System.out.println(invoices.stream()
                .filter(needsPaymentReminder)
                .toList());
        System.out.println(invoices.stream()
                .filter(needsPaymentReminder.and(amountOver1000))
                .toList());
    }

    // 10. Küçük ve isimlendirilmiş Predicate'ler karmaşık aramayı okunur tutar.
    private static List<SearchProduct> filterProducts(
            List<SearchProduct> products,
            Predicate<SearchProduct> criteria
    ) {
        return products.stream().filter(criteria).toList();
    }

    private static void dynamicProductSearch() {
        List<SearchProduct> products = List.of(
                new SearchProduct("Laptop", "Electronics", 900, 4.6, true),
                new SearchProduct("Monitor", "Electronics", 300, 3.8, true),
                new SearchProduct("Phone", "Electronics", 1100, 4.8, true),
                new SearchProduct("Headphones", "Electronics", 150, 4.2, false),
                new SearchProduct("Desk", "Furniture", 500, 4.5, true)
        );

        Predicate<SearchProduct> categoryMatches = product ->
                product.category().equals("Electronics");
        Predicate<SearchProduct> meetsMinimumPrice = product -> product.price() >= 100;
        Predicate<SearchProduct> meetsMaximumPrice = product -> product.price() <= 1000;
        Predicate<SearchProduct> meetsMinimumRating = product -> product.rating() >= 4;
        Predicate<SearchProduct> isInStock = SearchProduct::inStock;

        Predicate<SearchProduct> criteria = categoryMatches
                .and(meetsMinimumPrice)
                .and(meetsMaximumPrice)
                .and(meetsMinimumRating)
                .and(isInStock);

        System.out.println(filterProducts(products, criteria));
    }

    private record User(String username, boolean active) {}
    private record StockProduct(String name, double price, int stock) {}
    private record Order(long id, String status, double totalAmount) {}
    private record Candidate(
            String name,
            int yearsOfExperience,
            boolean knowsJava,
            boolean availableForFullTime
    ) {}
    private record Customer(String name, String country, int age, boolean premium) {}
    private record Invoice(long id, LocalDate dueDate, boolean paid, double amount) {}
    private record SearchProduct(
            String name,
            String category,
            double price,
            double rating,
            boolean inStock
    ) {}
}
```

### Yaklaşım 1 için önemli not

Buradaki `record` tanımları yalnızca örneği tek dosyada tutmak içindir. Gerçek bir
projede `User`, `Order`, `Invoice` gibi modeller çoğunlukla kendi dosyalarında olur.

## Yaklaşım 2 — Her requirement için ayrı demo sınıfı

Bu yaklaşımda her alıştırma bağımsız bir dosyada bulunur. Her sınıfın kendi
`main` metodu vardır; böylece bir örneği diğerlerinden bağımsız çalıştırabilirsin.
Kod bloklarının her biri ayrı `.java` dosyasıdır.

### 1. `UsernameValidationDemo.java`

```java
package org.practice.javacore.lambda.exercises.predicate;

import java.util.function.Predicate;

public class UsernameValidationDemo {
    public static void main(String[] args) {
        Predicate<String> isValidUsername = username ->
                username != null
                && !username.isBlank()
                && username.length() >= 5;

        // test(), Predicate'i tek bir değer üzerinde çalıştırır.
        System.out.println(isValidUsername.test("john"));
        System.out.println(isValidUsername.test("mehmet"));
        System.out.println(isValidUsername.test(""));
        System.out.println(isValidUsername.test(null));
    }
}
```

### 2. `NumberCheckDemo.java`

```java
package org.practice.javacore.lambda.exercises.predicate;

import java.util.function.Predicate;

public class NumberCheckDemo {
    public static void main(String[] args) {
        // Aynı metoda her çağrıda farklı bir davranış gönderiyoruz.
        System.out.println(checkNumber(10, number -> number > 0));
        System.out.println(checkNumber(10, number -> number % 2 == 0));
        System.out.println(checkNumber(10, number -> number >= 10 && number <= 100));
        System.out.println(checkNumber(10, number -> number % 3 == 0));
    }

    private static boolean checkNumber(
            int number,
            Predicate<Integer> condition
    ) {
        return condition.test(number);
    }
}
```

### 3. `ActiveUserDemo.java`

```java
package org.practice.javacore.lambda.exercises.predicate;

import java.util.List;
import java.util.function.Predicate;

public class ActiveUserDemo {
    public static void main(String[] args) {
        List<User> users = List.of(
                new User("Alice", true),
                new User("Bob", false),
                new User("Charlie", true),
                new User("David", false),
                new User("Eva", true)
        );

        System.out.println(getActiveUsers(users));
    }

    private static List<User> getActiveUsers(List<User> users) {
        Predicate<User> isActive = user -> user.active();
        // filter(), test() çağrısını listedeki her eleman için yapar.
        return users.stream().filter(isActive).toList();
    }

    private record User(String username, boolean active) {}
}
```

### 4. `ProductStockDemo.java`

```java
package org.practice.javacore.lambda.exercises.predicate;

import java.util.List;
import java.util.function.Predicate;

public class ProductStockDemo {
    public static void main(String[] args) {
        List<Product> products = List.of(
                new Product("Laptop", 1200, 5),
                new Product("Mouse", 40, 20),
                new Product("Monitor", 300, 0),
                new Product("Keyboard", 150, 3)
        );

        Predicate<Product> isExpensive = product -> product.price() > 100;
        Predicate<Product> isInStock = product -> product.stock() > 0;

        // and(): ürünün iki koşulu da sağlaması gerekir.
        List<Product> result = products.stream()
                .filter(isExpensive.and(isInStock))
                .toList();

        System.out.println(result);
    }

    private record Product(String name, double price, int stock) {}
}
```

### 5. `OrderFilterDemo.java`

```java
package org.practice.javacore.lambda.exercises.predicate;

import java.util.List;
import java.util.function.Predicate;

public class OrderFilterDemo {
    public static void main(String[] args) {
        List<Order> orders = List.of(
                new Order(1, "PAID", 250),
                new Order(2, "CANCELLED", 80),
                new Order(3, "SHIPPED", 650),
                new Order(4, "NEW", 900),
                new Order(5, "SHIPPED", 120)
        );

        Predicate<Order> isPaid = order -> order.status().equals("PAID");
        Predicate<Order> isCancelled = order -> order.status().equals("CANCELLED");
        Predicate<Order> amountOver500 = order -> order.totalAmount() > 500;
        Predicate<Order> isShipped = order -> order.status().equals("SHIPPED");
        Predicate<Order> amountOver100 = order -> order.totalAmount() > 100;

        System.out.println(filterOrders(orders, isPaid));
        System.out.println(filterOrders(orders, isCancelled));
        System.out.println(filterOrders(orders, amountOver500));
        System.out.println(filterOrders(orders, isShipped.and(amountOver100)));
    }

    private static List<Order> filterOrders(
            List<Order> orders,
            Predicate<Order> condition
    ) {
        // Metot PAID veya SHIPPED kavramlarını bilmez; yalnızca koşulu çalıştırır.
        return orders.stream().filter(condition).toList();
    }

    private record Order(long id, String status, double totalAmount) {}
}
```

### 6. `EmailValidationDemo.java`

```java
package org.practice.javacore.lambda.exercises.predicate;

import java.util.List;
import java.util.function.Predicate;

public class EmailValidationDemo {
    public static void main(String[] args) {
        Predicate<String> isValidEmail = email -> {
            // Çok satırlı lambda gövdesinde açıkça return yazılır.
            if (email == null || email.isBlank() || email.contains(" ")) {
                return false;
            }

            int atIndex = email.indexOf('@');
            return atIndex > 0 && email.indexOf('.', atIndex + 1) > atIndex + 1;
        };

        List<String> emails = List.of(
                "john@example.com",
                "johnexample.com",
                "john@example",
                "john @example.com"
        );

        emails.forEach(email -> System.out.println(isValidEmail.test(email)));
        System.out.println(isValidEmail.test(null));
    }
}
```

### 7. `CandidateFilterDemo.java`

```java
package org.practice.javacore.lambda.exercises.predicate;

import java.util.List;
import java.util.function.Predicate;

public class CandidateFilterDemo {
    public static void main(String[] args) {
        List<Candidate> candidates = List.of(
                new Candidate("Alice", 3, true, true),
                new Candidate("Bob", 1, true, true),
                new Candidate("Charlie", 5, false, true),
                new Candidate("David", 4, true, false)
        );

        Predicate<Candidate> hasExperience = candidate ->
                candidate.yearsOfExperience() >= 2;
        Predicate<Candidate> knowsJava = candidate -> candidate.knowsJava();
        Predicate<Candidate> isFullTimeAvailable = candidate ->
                candidate.availableForFullTime();

        Predicate<Candidate> isSuitable = hasExperience
                .and(knowsJava)
                .and(isFullTimeAvailable);

        System.out.println(candidates.stream().filter(isSuitable).toList());
        // Yeni ters koşul yazmak yerine mevcut koşulu negate() ile ters çeviririz.
        System.out.println(candidates.stream().filter(knowsJava.negate()).toList());
    }

    private record Candidate(
            String name,
            int yearsOfExperience,
            boolean knowsJava,
            boolean availableForFullTime
    ) {}
}
```

### 8. `CustomerSearchDemo.java`

```java
package org.practice.javacore.lambda.exercises.predicate;

import java.util.List;
import java.util.function.Predicate;

public class CustomerSearchDemo {
    public static void main(String[] args) {
        List<Customer> customers = List.of(
                new Customer("Alice", "Netherlands", 30, true),
                new Customer("Bob", "Belgium", 17, false),
                new Customer("Charlie", "Netherlands", 55, false),
                new Customer("David", "Germany", 60, true)
        );

        Predicate<Customer> livesInNetherlands = customer ->
                customer.country().equals("Netherlands");
        Predicate<Customer> isAdult = customer -> customer.age() > 18;
        Predicate<Customer> isPremium = customer -> customer.premium();
        Predicate<Customer> isOlderThan50 = customer -> customer.age() > 50;

        System.out.println(search(customers, livesInNetherlands));
        System.out.println(search(customers, isAdult));
        System.out.println(search(customers, isPremium));
        System.out.println(search(customers, livesInNetherlands.and(isPremium)));
        System.out.println(search(customers, isPremium.or(isOlderThan50)));
    }

    private static List<Customer> search(
            List<Customer> customers,
            Predicate<Customer> criteria
    ) {
        return customers.stream().filter(criteria).toList();
    }

    private record Customer(String name, String country, int age, boolean premium) {}
}
```

### 9. `InvoiceFilterDemo.java`

```java
package org.practice.javacore.lambda.exercises.predicate;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

public class InvoiceFilterDemo {
    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        List<Invoice> invoices = List.of(
                new Invoice(1, today.minusDays(10), false, 1500),
                new Invoice(2, today.minusDays(2), true, 300),
                new Invoice(3, today.plusDays(5), false, 2000),
                new Invoice(4, today.minusDays(1), false, 200)
        );

        Predicate<Invoice> isOverdue = invoice -> invoice.dueDate().isBefore(today);
        Predicate<Invoice> isUnpaid = invoice -> !invoice.paid();
        Predicate<Invoice> amountOver1000 = invoice -> invoice.amount() > 1000;

        Predicate<Invoice> reminderRequired = isOverdue.and(isUnpaid);

        System.out.println(invoices.stream().filter(reminderRequired).toList());
        System.out.println(invoices.stream()
                .filter(reminderRequired.and(amountOver1000))
                .toList());
    }

    private record Invoice(long id, LocalDate dueDate, boolean paid, double amount) {}
}
```

### 10. `DynamicProductSearchDemo.java`

```java
package org.practice.javacore.lambda.exercises.predicate;

import java.util.List;
import java.util.function.Predicate;

public class DynamicProductSearchDemo {
    public static void main(String[] args) {
        List<Product> products = List.of(
                new Product("Laptop", "Electronics", 900, 4.6, true),
                new Product("Monitor", "Electronics", 300, 3.8, true),
                new Product("Phone", "Electronics", 1100, 4.8, true),
                new Product("Headphones", "Electronics", 150, 4.2, false),
                new Product("Desk", "Furniture", 500, 4.5, true)
        );

        Predicate<Product> categoryMatches = product ->
                product.category().equals("Electronics");
        Predicate<Product> meetsMinimumPrice = product -> product.price() >= 100;
        Predicate<Product> meetsMaximumPrice = product -> product.price() <= 1000;
        Predicate<Product> meetsMinimumRating = product -> product.rating() >= 4;
        Predicate<Product> isInStock = product -> product.inStock();

        Predicate<Product> criteria = categoryMatches
                .and(meetsMinimumPrice)
                .and(meetsMaximumPrice)
                .and(meetsMinimumRating)
                .and(isInStock);

        System.out.println(filterProducts(products, criteria));
    }

    private static List<Product> filterProducts(
            List<Product> products,
            Predicate<Product> criteria
    ) {
        return products.stream().filter(criteria).toList();
    }

    private record Product(
            String name,
            String category,
            double price,
            double rating,
            boolean inStock
    ) {}
}
```

### 10. alıştırmanın opsiyonel filtre geliştirmesi

Aşağıdaki metot, kullanıcıdan bazı filtrelerin gelmeyebileceği daha gerçekçi bir
arama senaryosunu gösterir. Başlangıçtaki `product -> true` Predicate'i bütün
ürünleri kabul eder; yalnızca değeri verilen alanların koşulları buna eklenir.

```java
private static Predicate<Product> buildCriteria(
        String category,
        Double minimumPrice,
        Double maximumPrice,
        Double minimumRating,
        boolean onlyInStock
) {
    Predicate<Product> criteria = product -> true;

    if (category != null && !category.isBlank()) {
        criteria = criteria.and(product -> product.category().equals(category));
    }
    if (minimumPrice != null) {
        criteria = criteria.and(product -> product.price() >= minimumPrice);
    }
    if (maximumPrice != null) {
        criteria = criteria.and(product -> product.price() <= maximumPrice);
    }
    if (minimumRating != null) {
        criteria = criteria.and(product -> product.rating() >= minimumRating);
    }
    if (onlyInStock) {
        criteria = criteria.and(product -> product.inStock());
    }

    return criteria;
}
```

Örnek çağrı:

```java
Predicate<Product> criteria = buildCriteria(
        "Electronics",
        100.0,
        1000.0,
        4.0,
        true
);

List<Product> result = filterProducts(products, criteria);
```

