# Stream `reduce()` ve Sayısal İndirgeme — Örnek Çözümler

`reduce`, `sum`, `min`, `max`, `average` ve `summaryStatistics` terminal
operation'dır. `mapToInt` ve `mapToDouble` ise nesne Stream'ini primitive sayısal
Stream'e dönüştüren intermediate operation'lardır.

## 1. Sipariş toplamı

```java
double total = lines.stream()
        .map(line -> line.quantity() * line.unitPrice())
        .reduce(0.0, Double::sum);
```

`0.0` toplamanın identity değeridir; boş siparişin toplamı sıfır olur.

## 2. Başarılı işlem toplamı

```java
double revenue = transactions.stream()
        .filter(Transaction::successful)
        .mapToDouble(Transaction::amount)
        .sum();
```

`mapToDouble` sonrasında hazır sayısal `sum()` kullanılabilir.

## 3. Sepet adedi

```java
int itemCount = cart.stream()
        .mapToInt(CartItem::quantity)
        .sum();
```

`sum()` boş `IntStream` için `0` döndürür.

## 4. En yüksek teklif

```java
OptionalDouble maximum = bids.stream()
        .mapToDouble(Bid::amount)
        .max();

System.out.println(maximum.isPresent()
        ? "En yüksek teklif: " + maximum.getAsDouble()
        : "Henüz teklif yok");
```

## 5. En kısa teslimat

```java
OptionalInt fastestDays = options.stream()
        .mapToInt(DeliveryOption::estimatedDays)
        .min();
```

Şirket bilgisi de gerekiyorsa nesneyi kaybetmeden
`options.stream().min(Comparator.comparingInt(...))` kullanılmalıdır.

## 6. Ortalama puan

```java
double averageRating = reviews.stream()
        .mapToInt(Review::rating)
        .average()
        .orElse(0.0);
```

`average()` boş akışta `OptionalDouble.empty()` üretir.

## 7. Maaş özeti

```java
IntSummaryStatistics stats = employees.stream()
        .mapToInt(Employee::salary)
        .summaryStatistics();

System.out.println(stats.getCount());
System.out.println(stats.getSum());
System.out.println(stats.getMin());
System.out.println(stats.getMax());
System.out.println(stats.getAverage());
```

Tek traversal ile beş gösterge hesaplanır.

## 8. Ardışık indirim

```java
double combinedFactor = discounts.stream()
        .map(rate -> 1 - rate)
        .reduce(1.0, (left, right) -> left * right);

double finalPrice = 200.0 * combinedFactor;
```

Çarpan akışı `0.90 × 0.80 = 0.72`, fiyat ise `200 × 0.72 = 144` olur. `1.0`
çarpmanın identity değeridir ve çarpma associative olduğu için `reduce`
sözleşmesine uygundur.

## 9. Etiket metni

```java
String tags = values.stream()
        .reduce((left, right) -> left + ", " + right)
        .orElse("");
```

Başlangıç değersiz overload `Optional<String>` döndürür; boş liste boş metne
çevrilir. Üretim kodunda çok metin için `Collectors.joining(", ")` daha uygundur.

## 10. Genel birleştirme

```java
static <T> Optional<T> combine(
        List<T> values,
        BinaryOperator<T> operation
) {
    return values.stream().reduce(operation);
}

Optional<Integer> sum = combine(numbers, Integer::sum);
Optional<Integer> product = combine(numbers, (a, b) -> a * b);
Optional<String> text = combine(words, (a, b) -> a + " " + b);
```

`BinaryOperator<T>`, aynı tipte iki değer alıp aynı tipte tek değer döndürür.
