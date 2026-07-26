# Özel Functional Interface ve Lambda — Örnek Çözümler

Her lambda, hedef interface'in tek abstract metodunu uygular; davranış interface
metodu çağrılana kadar çalışmaz.

## 1. Bildirim davranışı

```java
@FunctionalInterface
interface NotificationAction { void send(String customer); }

NotificationAction sms = name -> System.out.println("SMS: " + name);
NotificationAction email = name -> System.out.println("E-mail: " + name);
sms.send("Alice");
email.send("Mehmet");
```

## 2. Fiyat hesaplayıcı

```java
interface PriceCalculator { double calculate(double amount); }
PriceCalculator standard = amount -> amount;
PriceCalculator discount = amount -> amount * 0.90;
PriceCalculator tax = amount -> amount * 1.20;
```

## 3. Aritmetik işlemler

```java
interface ArithmeticOperation { int apply(int a, int b); }
ArithmeticOperation add = (a, b) -> a + b;
ArithmeticOperation subtract = (a, b) -> a - b;
ArithmeticOperation multiply = (a, b) -> a * b;
ArithmeticOperation divide = (a, b) -> {
    if (b == 0) throw new IllegalArgumentException("Sıfıra bölünemez");
    return a / b;
};
```

## 4. Generic doğrulama

```java
interface ValidationRule<T> { boolean isValid(T value); }
ValidationRule<String> username =
        value -> value != null && value.length() >= 5;
ValidationRule<Integer> quantity = value -> value != null && value > 0;
```

## 5. DTO dönüşümü

```java
interface Converter<T, R> { R convert(T source); }
record Customer(String name, String email) {}
record CustomerSummary(String displayName) {}

Converter<Customer, CustomerSummary> converter =
        customer -> new CustomerSummary(customer.name().toUpperCase());
```

## 6. Callback

```java
interface OrderCompletedCallback { void onCompleted(long orderId); }
OrderCompletedCallback log =
        id -> System.out.println("Completed: " + id);
OrderCompletedCallback notify =
        id -> System.out.println("Notification for: " + id);
```

## 7. Teslimat stratejisi

```java
interface ShippingStrategy { double fee(double weight); }
ShippingStrategy standard = weight -> weight * 1.5;
ShippingStrategy express = weight -> weight * 3.0;
ShippingStrategy free = weight -> 0;
```

## 8. Factory

```java
interface Factory<T> { T create(); }
Factory<List<String>> cartFactory = ArrayList::new;
Factory<StringBuilder> reportFactory = StringBuilder::new;
```

## 9. Genel çalıştırıcı

```java
interface Operation<T, R> { R apply(T value); }
static <T, R> R execute(T value, Operation<T, R> operation) {
    return operation.apply(value);
}

int length = execute("Amsterdam", String::length);
double price = execute(100.0, value -> value * 0.80);
```

## 10. Anonymous class ve lambda

```java
interface AuditAction { void record(String event); }

AuditAction oldStyle = new AuditAction() {
    @Override public void record(String event) {
        System.out.println("Audit: " + event);
    }
};
AuditAction lambda = event -> System.out.println("Audit: " + event);
```

