# `Collectors.partitioningBy()` — Örnek Çözümler

## 1. Aktiflik
```java
Map<Boolean,List<User>> result = users.stream()
        .collect(Collectors.partitioningBy(User::active));
```

## 2. Sınav
```java
Map<Boolean,List<Student>> result = students.stream()
        .collect(Collectors.partitioningBy(s -> s.grade() >= 60));
```

## 3. Faturalar
```java
Map<Boolean,List<Invoice>> result = invoices.stream()
        .collect(Collectors.partitioningBy(Invoice::paid));
```

## 4. Stok
```java
Map<Boolean,List<Product>> result = products.stream()
        .collect(Collectors.partitioningBy(p -> p.stock() > 0));
```

## 5. Yaş
```java
Map<Boolean,List<Participant>> result = participants.stream()
        .collect(Collectors.partitioningBy(p -> p.age() >= 18));
```

## 6. İletişim izni
```java
Map<Boolean,List<Customer>> result = customers.stream()
        .collect(Collectors.partitioningBy(Customer::consent));
```

## 7. Grup sayıları
```java
Map<Boolean,Long> counts = products.stream().collect(
        Collectors.partitioningBy(Product::active, Collectors.counting()));
```

## 8. İsim değerleri
```java
Map<Boolean,List<String>> names = users.stream().collect(
        Collectors.partitioningBy(User::active,
                Collectors.mapping(User::name, Collectors.toList())));
```

## 9. Segment özeti
```java
Map<Boolean,DoubleSummaryStatistics> stats = products.stream().collect(
        Collectors.partitioningBy(
                p -> p.price() > 500,
                Collectors.summarizingDouble(Product::price)));
```

## 10. Generic metot
```java
static <T> Map<Boolean,List<T>> partition(
        List<T> values, Predicate<T> condition) {
    return values.stream().collect(Collectors.partitioningBy(condition));
}
```

