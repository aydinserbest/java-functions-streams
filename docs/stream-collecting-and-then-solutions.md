# `Collectors.collectingAndThen()` — Örnek Çözümler

## 1. Immutable alıcılar
```java
List<Customer> recipients = customers.stream()
        .filter(Customer::active)
        .collect(Collectors.collectingAndThen(
                Collectors.toList(), List::copyOf));
```

## 2. En pahalı ürün adı
```java
String name = products.stream().collect(Collectors.collectingAndThen(
        Collectors.maxBy(Comparator.comparingDouble(Product::price)),
        optional -> optional.map(Product::name).orElse("No products found")
));
```

## 3. Özet DTO
```java
record OrderTotalSummary(double total) {}
OrderTotalSummary summary = lines.stream().collect(
        Collectors.collectingAndThen(
                Collectors.summingDouble(Line::amount),
                OrderTotalSummary::new));
```

## 4. Başlıklı metin
```java
String report = names.stream().collect(Collectors.collectingAndThen(
        Collectors.joining(", "),
        joined -> "Campaign recipients: " + joined));
```

## 5. Benzersiz sıralı şehirler
```java
List<String> cities = customers.stream().map(Customer::city)
        .collect(Collectors.collectingAndThen(
                Collectors.toSet(),
                set -> set.stream().sorted().toList()));
```

## 6. Immutable departman listeleri
```java
Map<String,List<Employee>> result = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::department,
                Collectors.collectingAndThen(
                        Collectors.toList(), List::copyOf)));
```

## 7. Formatlı ortalama
```java
String result = reviews.stream().collect(Collectors.collectingAndThen(
        Collectors.averagingDouble(Review::rating),
        average -> "Average: %.2f".formatted(average)));
```

## 8. Boş sonucu reddetme
```java
List<Item> approved = items.stream().filter(Item::approved)
        .collect(Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.isEmpty()) throw new IllegalStateException("No items");
                    return list;
                }));
```

## 9. Immutable vitrin
```java
List<Product> showcase = products.stream().sorted(byPrice.reversed()).limit(3)
        .collect(Collectors.collectingAndThen(
                Collectors.toList(), List::copyOf));
```

## 10. Collector factory
```java
static <T,R,RR> Collector<T,?,RR> finishWith(
        Collector<T,?,R> collector, Function<R,RR> finisher) {
    return Collectors.collectingAndThen(collector, finisher);
}
```

