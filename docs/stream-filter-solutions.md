# Stream `filter()` Alıştırmaları — Örnek Çözümler

Önce requirement'ları kendin çöz. Aşağıdaki parçalar ilgili modellerin `record`
olarak tanımlandığı bir sınıf içinde çalıştırılabilir. Her örnekte `filter()`
intermediate, `toList()` terminal operation'dır.

## 1. Satışa açık ürünler

```java
record Product(String name, boolean active, int stock) {}

List<Product> result = products.stream()
        .filter(product -> product.active() && product.stock() > 0)
        .toList();
```

Lambda bir `Predicate<Product>` taşır. Her ürün için iki kural da doğruysa ürün
katalog sonucunda kalır.

## 2. Ödenmemiş yüksek faturalar

```java
Predicate<Invoice> isUnpaid = invoice -> !invoice.paid();
Predicate<Invoice> isHighValue = invoice -> invoice.amount() > 500;

List<Invoice> result = invoices.stream()
        .filter(isUnpaid.and(isHighValue))
        .toList();
```

Finans raporuna yalnızca iki koşulu birlikte sağlayan kayıtlar girer.

## 3. Teslimata hazır siparişler

```java
List<Order> ready = orders.stream()
        .filter(order -> "PAID".equals(order.status())
                && order.addressVerified())
        .toList();
```

Sabit metni solda kullanmak, `status` yanlışlıkla `null` olsa bile
`NullPointerException` oluşmasını engeller.

## 4. Etkinlik katılımcıları

```java
List<String> names = participants.stream()
        .filter(person -> person.age() >= 18
                && person.city().equals("Amsterdam"))
        .map(Participant::name)
        .toList();
```

`filter()` kişiyi seçer, `map()` seçilen kişiyi gösterilecek isme dönüştürür.

## 5. Geçerli e-postalar

```java
List<String> validEmails = emails.stream()
        .filter(email -> email != null
                && !email.isBlank()
                && !email.contains(" ")
                && email.contains("@"))
        .toList();
```

`&&` kısa devre yaptığı için `null` değerlerde sonraki String metotları çalışmaz.

## 6. Kritik stok

```java
List<StockItem> critical = items.stream()
        .filter(item -> item.stock() >= 0
                && item.stock() <= item.reorderLevel())
        .toList();
```

Her ürün kendi yeniden sipariş eşiğiyle karşılaştırılır.

## 7. Uygun iş ilanları

```java
List<Job> suitable = jobs.stream()
        .filter(job -> job.remote() && job.minimumExperience() <= 3)
        .toList();
```

Sonuç, adayın hem çalışma biçimine hem deneyim seviyesine uyan ilanlardan oluşur.

## 8. Geçerli kuponlar

```java
List<Coupon> availableCoupons(List<Coupon> coupons, LocalDate today) {
    return coupons.stream()
            .filter(coupon -> coupon.enabled()
                    && !coupon.expiryDate().isBefore(today))
            .toList();
}
```

`!isBefore(today)` bugünü ve gelecekteki tarihleri kabul eder.

## 9. Moderasyon kuyruğu

```java
Predicate<Review> isNotApproved = review -> !review.approved();
Predicate<Review> isFlagged = Review::flagged;

List<Review> queue = reviews.stream()
        .filter(isNotApproved.or(isFlagged))
        .toList();
```

`or()` nedeniyle koşullardan yalnızca birinin doğru olması yeterlidir.

## 10. Genel müşteri araması

```java
List<Customer> search(
        List<Customer> customers,
        Predicate<Customer> criteria
) {
    return customers.stream().filter(criteria).toList();
}

List<Customer> dutch = search(customers,
        customer -> customer.country().equals("Netherlands"));
List<Customer> premium = search(customers, Customer::premium);
List<Customer> valuablePremium = search(customers,
        customer -> customer.premium() && customer.balance() > 1000);
```

Algoritma sabit kalır; değişen business davranışı metoda Predicate olarak gider.

