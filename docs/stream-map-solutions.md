# Stream `map()` Alıştırmaları — Örnek Çözümler

## 1. Katalog etiketi
```java
List<String> labels = products.stream()
        .map(p -> p.name() + " - €" + p.price()).toList();
```

## 2. DTO
```java
List<EmployeeSummary> result = employees.stream()
        .map(e -> new EmployeeSummary(e.name(), e.department())).toList();
```

## 3. Fiyat ayrıştırma
```java
List<Double> prices = texts.stream()
        .map(String::trim).map(Double::parseDouble).toList();
```

## 4. Satır toplamı
```java
List<Double> totals = lines.stream()
        .map(line -> line.quantity() * line.unitPrice()).toList();
```

## 5. Normalizasyon
```java
List<String> names = rawNames.stream()
        .map(String::trim)
        .map(name -> name.toUpperCase(Locale.ROOT))
        .toList();
```

## 6. ID lookup
```java
List<Product> products = ids.stream()
        .map(productById::get)
        .filter(Objects::nonNull)
        .toList();
```

## 7. Optional map
```java
String name = products.stream().max(byPrice)
        .map(Product::name)
        .orElse("No products found");
```

Bu `Optional.map()` çağrısıdır: `Optional<Product> → Optional<String>`.

## 8. İç listeler
```java
List<List<String>> skillsByPerson = people.stream()
        .map(Person::skills).toList();
```

## 9. Kur dönüşümü
```java
List<Double> dollarPrices = euroPrices.stream()
        .map(price -> price * exchangeRate).toList();
```

## 10. Generic transform
```java
static <T,R> List<R> transform(List<T> values, Function<T,R> mapper) {
    return values.stream().map(mapper).toList();
}
```

