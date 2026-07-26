# Method Reference Alıştırmaları — Örnek Çözümler

## 1. Fiyat ayrıştırma

```java
List<Double> prices = texts.stream().map(Double::parseDouble).toList();
```

## 2. Statik loglama

```java
class OrderLogger {
    static void log(long id) { System.out.println("Order: " + id); }
}
Consumer<Long> logger = OrderLogger::log;
orderIds.forEach(logger);
```

## 3. Belirli servis nesnesi

```java
EmailService service = new EmailService();
Consumer<String> sender = service::send;
emails.forEach(sender);
```

`service::send`, belirli instance'ın metodunu temsil eder.

## 4. Sınıf üzerinden instance metodu

```java
List<String> cleaned = values.stream()
        .map(String::trim)
        .map(String::toUpperCase)
        .toList();
```

## 5. Customer constructor

```java
Function<String, Customer> factory = Customer::new;
List<Customer> customers = names.stream().map(factory).toList();
```

## 6. Parametresiz factory

```java
Supplier<List<String>> factory = ArrayList::new;
List<String> result = factory.get();
```

## 7. Comparator

```java
Comparator<Product> byPrice =
        Comparator.comparingInt(Product::getPrice);
List<Product> sorted = products.stream().sorted(byPrice).toList();
```

## 8. Predicate

```java
List<User> active = users.stream().filter(User::isActive).toList();
```

## 9. Hedef tip

```java
Function<String, Integer> boxedLength = String::length;
ToIntFunction<String> primitiveLength = String::length;
```

İlki `Integer`, ikincisi primitive `int` sözleşmesidir.

## 10. Okunabilir seçim

```java
emails.forEach(emailService::send); // yalnızca send çağrısı

emails.forEach(email -> {
    if (email.contains("@")) {
        emailService.send(email);
    }
}); // ek business kontrolü var, lambda daha açık
```

