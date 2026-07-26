# Consumer Alıştırmaları — Örnek Çözümler

Bu dosya [Consumer requirement'larının](consumer-requirements.md) kısa örnek
çözümleridir. `Consumer<T>` değer alır, yan etki yapar ve sonuç döndürmez.

## 1. Hoş geldin mesajı

```java
Consumer<String> welcome =
        name -> System.out.println("Hoş geldin, " + name + "!");
welcome.accept("Alice");
welcome.accept("Mehmet");
```

## 2. Sipariş logları

```java
Consumer<Long> logOrder =
        id -> System.out.println("Sipariş işlendi: " + id);
List.of(1001L, 1002L, 1003L).forEach(logOrder);
```

## 3. Kampanya e-postası

```java
record Customer(String name, String email) {}
Consumer<Customer> sendCampaign = customer ->
        System.out.println("Kampanya e-postası gönderildi: " + customer.email());
customers.forEach(sendCampaign);
```

## 4. Stok azaltma

```java
Consumer<Product> sellOne = product -> {
    if (product.getStock() > 0) {
        product.setStock(product.getStock() - 1);
    }
};
```

Consumer yeni Product döndürmez; verilen nesnenin durumunu günceller.

## 5. Fatura satırı

```java
Consumer<InvoiceLine> printLine = line -> System.out.printf(
        "%s | %d adet | €%.2f | Satır toplamı: €%.2f%n",
        line.description(), line.quantity(), line.unitPrice(),
        line.quantity() * line.unitPrice());
lines.forEach(printLine);
```

## 6. Generic process

```java
static <T> void process(T value, Consumer<T> action) {
    action.accept(value);
}
process("java", text -> System.out.println(text.toUpperCase()));
process(5, number -> System.out.println(number * number));
```

## 7. `andThen()`

```java
Consumer<Order> save =
        order -> System.out.println("Sipariş kaydedildi: " + order.id());
Consumer<Order> notify =
        order -> System.out.println("Onay gönderildi: " + order.email());
save.andThen(notify).accept(order);
```

İki Consumer aynı Order'ı sırayla alır.

## 8. Denetim kaydı

```java
Consumer<AuditEvent> audit = event -> System.out.printf(
        "[%s] %s - %s%n",
        event.time(), event.username(), event.action());
events.forEach(audit);
```

## 9. Method reference ile kuyruk

```java
NotificationQueue queue = new NotificationQueue();
Consumer<Notification> enqueue = queue::add;
notifications.forEach(enqueue);
```

## 10. Başarılı ödemeler ve makbuz

```java
payments.stream()
        .filter(Payment::successful)
        .forEach(payment ->
                System.out.println("Makbuz: " + payment.id()));
```

`filter()` Predicate, `forEach()` Consumer davranışını çalıştırır.

