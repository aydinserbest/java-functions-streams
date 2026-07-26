# `Stream.iterate()` ve `Stream.generate()` — Örnek Çözümler

## 1. Sipariş ID'leri
```java
List<Integer> ids = Stream.iterate(1001, id -> id + 1).limit(10).toList();
```

## 2. Haftalık tarihler
```java
List<LocalDate> dates = Stream.iterate(start, date -> date.plusWeeks(1))
        .limit(4).toList();
```

## 3. Kalan borç
```java
List<Integer> balances = Stream.iterate(
        1000, balance -> balance >= 0, balance -> balance - 100
).toList();
```

## 4. Büyüme
```java
List<Integer> forecast = Stream.iterate(10, stock -> stock * 2)
        .limit(6).toList();
```

## 5. Kodlar
```java
Random random = new Random();
List<Integer> codes = Stream.generate(
        () -> 100000 + random.nextInt(900000)
).limit(5).toList();
```

## 6. Sabit cevap
```java
List<String> health = Stream.generate(() -> "SERVICE_OK")
        .limit(3).toList();
```

## 7. UUID
```java
List<UUID> ids = Stream.generate(UUID::randomUUID).limit(3).toList();
```

## 8. Doğru seçim
```java
Stream.iterate(startMonth, month -> month.plusMonths(1)).limit(6);
Stream.generate(codeService::newCoupon).limit(6);
```

`iterate` önceki değere bağlı, `generate` bağımsız Supplier kullanır.

## 9. Güvenli sınır
```java
Stream.iterate(1, n -> n + 1)
        .limit(100)
        .forEach(System.out::println);
```

## 10. İlk beş çift
```java
List<Integer> evens = Stream.iterate(1, n -> n + 1)
        .filter(n -> n % 2 == 0)
        .limit(5)
        .toList();
```

