# Stream `limit()` Alıştırmaları — Örnek Çözümler

`limit()` intermediate, `toList()` terminal operation'dır. “En iyi/son/en eski”
gibi bir business ifadesi varsa `limit()` öncesindeki sıralama sonucu belirler.

## 1. Son üç haber

```java
List<News> latest = news.stream()
        .sorted(Comparator.comparing(News::publishedAt).reversed())
        .limit(3)
        .toList();
```

## 2. En pahalı beş ürün

```java
List<Product> premium = products.stream()
        .sorted(Comparator.comparingDouble(Product::price).reversed())
        .limit(5)
        .toList();
```

## 3. Sekiz arama önerisi

```java
List<String> suggestions(List<String> names, String query) {
    String normalized = query.toLowerCase(Locale.ROOT);
    return names.stream()
            .filter(name -> name.toLowerCase(Locale.ROOT).contains(normalized))
            .limit(8)
            .toList();
}
```

## 4. İlk on destek talebi

```java
List<Ticket> batch = tickets.stream()
        .filter(ticket -> "OPEN".equals(ticket.status()))
        .sorted(Comparator.comparing(Ticket::createdAt))
        .limit(10)
        .toList();
```

## 5. İlk 100 uygun müşteri

```java
List<Customer> recipients = customers.stream()
        .filter(customer -> customer.active() && customer.consent())
        .limit(100)
        .toList();
```

Filtre limitten önce olduğu için 100 uygun müşteri aranır; ilk 100 kaydın uygun
olanları aranmaz.

## 6. İlk 20 doğal sayı

```java
List<Integer> firstTwenty = Stream.iterate(1, number -> number + 1)
        .limit(20)
        .toList();
```

Sonsuz Stream, `limit()` sayesinde sonlu hale gelir.

## 7. Beş doğrulama kodu

```java
Random random = new Random();
List<Integer> codes = Stream.generate(
                () -> 100000 + random.nextInt(900000))
        .limit(5)
        .toList();
```

## 8. En faydalı üç yorum

```java
List<Review> featured = reviews.stream()
        .filter(Review::approved)
        .sorted(Comparator.comparingInt(Review::helpfulVotes).reversed())
        .limit(3)
        .toList();
```

## 9. İlk beş başarısız ödeme

```java
List<Payment> failures = payments.stream()
        .filter(payment -> !payment.successful())
        .sorted(Comparator.comparing(Payment::attemptedAt))
        .limit(5)
        .toList();
```

## 10. Genel ön izleme

```java
static <T> List<T> preview(List<T> values, long maximum) {
    if (maximum < 0) {
        throw new IllegalArgumentException("maximum negatif olamaz");
    }
    return values.stream().limit(maximum).toList();
}
```

Generic metot, liste elemanlarının türünden bağımsızdır.

