# Stream `skip()` Alıştırmaları — Örnek Çözümler

`skip()` intermediate operation'dır. Negatif değer kabul etmez; atlanacak miktar
eleman sayısından büyükse boş Stream kalır.

## 1. İkinci ürün sayfası

```java
List<String> secondPage = products.stream().skip(5).limit(5).toList();
```

## 2. Kalan eğitim videoları

```java
List<String> remaining = videos.stream().skip(3).toList();
```

## 3. CSV başlığını atlama

```java
List<String> dataRows = csvLines.stream().skip(1).toList();
```

## 4. Yedek adaylar

```java
List<Candidate> reserve = candidates.stream()
        .sorted(Comparator.comparingInt(Candidate::score).reversed())
        .skip(2)
        .limit(3)
        .toList();
```

## 5. Checkpoint sonrası işlemler

```java
List<Transaction> remaining(
        List<Transaction> transactions,
        long processedCount
) {
    return transactions.stream().skip(processedCount).toList();
}
```

## 6. Ücretli bölümler

```java
List<Chapter> paidChapters = chapters.stream().skip(2).toList();
```

## 7. Yeni log bölümü

```java
List<String> newLogs = logs.stream().skip(100).toList();
```

100'den az eleman varsa sonuç boş listedir.

## 8. Alternatif müşteriler

```java
List<Customer> alternatives = customers.stream()
        .filter(Customer::active)
        .sorted(Comparator.comparing(Customer::name))
        .skip(1)
        .limit(3)
        .toList();
```

## 9. Genel sayfalama

```java
static <T> List<T> page(
        List<T> values,
        int pageNumber,
        int pageSize
) {
    if (pageNumber < 0 || pageSize < 0) {
        throw new IllegalArgumentException("Sayfa değerleri negatif olamaz");
    }
    long offset = (long) pageNumber * pageSize;
    return values.stream().skip(offset).limit(pageSize).toList();
}
```

## 10. En yeni olay hariç geçmiş

```java
List<AccountEvent> history = events.stream()
        .sorted(Comparator.comparing(AccountEvent::occurredAt).reversed())
        .skip(1)
        .toList();
```

