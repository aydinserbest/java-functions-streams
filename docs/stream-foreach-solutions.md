# `forEach()` ve Consumer — Örnek Çözümler

## 1. Sipariş logları
```java
orderIds.forEach(id -> System.out.println("Processed: " + id));
```

## 2. E-posta
```java
customers.forEach(emailService::send);
```

## 3. Stok güncelleme
```java
Consumer<Product> sellOne = p -> {
    if (p.getStock() > 0) p.setStock(p.getStock() - 1);
};
products.forEach(sellOne);
```

## 4. Makbuz
```java
payments.stream().filter(Payment::successful)
        .forEach(payment -> receiptService.print(payment));
```

## 5. Method reference
```java
values.forEach(value -> System.out.println(value));
values.forEach(System.out::println);
```

## 6. Tekrar kullanılan Consumer
```java
Consumer<Product> logger = p -> System.out.println(p.getName());
electronics.forEach(logger);
furniture.forEach(logger);
```

## 7. Zincir
```java
Consumer<Order> save = repository::save;
Consumer<Order> notify = notificationService::send;
orders.forEach(save.andThen(notify));
```

## 8. Map forEach
```java
stockByCode.forEach((code, stock) ->
        System.out.println(code + " -> " + stock));
```

## 9. Sıralı paralel çıktı
```java
values.parallelStream().forEachOrdered(System.out::println);
```

## 10. Uygun toplam
```java
int total = products.stream().mapToInt(Product::getStock).sum();
```

`forEach()` sonuç döndürmez; `sum()` indirgeme amacını açıkça ifade eder.

