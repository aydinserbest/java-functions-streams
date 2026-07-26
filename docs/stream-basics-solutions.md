# Stream Oluşturma, Pipeline ve Tek Kullanım — Örnek Çözümler

## 1. List kaynağı
```java
departments.stream().forEach(System.out::println);
```

## 2. Array kaynağı
```java
Arrays.stream(productCodes).forEach(System.out::println);
```

## 3. `Stream.of`
```java
Stream.of("NEW", "PAID", "SHIPPED").forEach(System.out::println);
```

## 4. Boş Stream
```java
long count = Stream.empty().count(); // 0
```

## 5. Lazy çalışma
```java
Stream<String> pipeline = names.stream()
        .map(name -> {
            System.out.println("Mapping: " + name);
            return name.toUpperCase();
        });
// Henüz çıktı yok.
List<String> result = pipeline.toList(); // map şimdi çalışır.
```

## 6. Tek kullanım
```java
Stream<String> stream = names.stream();
stream.count();
stream.forEach(System.out::println); // IllegalStateException
```

## 7. İki rapor
```java
long activeCount = users.stream().filter(User::active).count();
List<String> names = users.stream().map(User::name).toList();
```

## 8. Kaynak değişmez
```java
List<String> uppercase = cities.stream().map(String::toUpperCase).toList();
System.out.println(cities);    // orijinal
System.out.println(uppercase); // yeni liste
```

## 9. Builder
```java
Stream.Builder<String> builder = Stream.builder();
builder.add("EMAIL").add("SMS").add("PUSH");
List<String> channels = builder.build().toList();
```

## 10. Pipeline türleri
```java
List<String> result = customers.stream()
        .filter(Customer::active)   // intermediate
        .map(Customer::name)        // intermediate
        .sorted()                   // intermediate
        .toList();                  // terminal
```

