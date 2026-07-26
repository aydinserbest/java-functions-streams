# Stream `count()` Alıştırmaları — Örnek Çözümler

`count()` terminal operation'dır ve `long` döndürür. Çağrıldığında pipeline
çalışır ve Stream tüketilir.

## 1. Aktif kullanıcılar

```java
long activeCount = users.stream().filter(User::active).count();
```

## 2. Tükenen ürünler

```java
long outOfStockCount = products.stream()
        .filter(product -> product.stock() == 0)
        .count();
```

## 3. Bugünkü siparişler

```java
long todaysOrders = orders.stream()
        .filter(order -> order.createdAt().toLocalDate().equals(today))
        .count();
```

## 4. Amsterdam premium müşteri sayısı

```java
long count = customers.stream()
        .filter(customer -> customer.premium()
                && customer.city().equals("Amsterdam"))
        .count();
```

## 5. Toplam sipariş satırı

```java
long lineCount = orders.stream()
        .flatMap(order -> order.lines().stream())
        .count();
```

Burada `orders.size()` sipariş sayısını verir; satır sayısını vermez.

## 6. Benzersiz ziyaretçiler

```java
long uniqueVisitors = userIds.stream().distinct().count();
```

## 7. Geçerli e-postalar

```java
long validCount = emails.stream()
        .filter(email -> email != null && email.contains("@"))
        .count();
```

`&&` kısa devre sayesinde `null` üzerinde `contains()` çalışmaz.

## 8. Başarılı öğrenciler

```java
long successful = students.stream()
        .filter(student -> student.grade() >= 70
                && student.grade() <= 100)
        .count();
```

## 9. İki sayma yöntemi

```java
long direct = tasks.stream()
        .filter(task -> !task.completed())
        .count();

long collected = tasks.stream()
        .filter(task -> !task.completed())
        .collect(Collectors.counting());

System.out.println(direct == collected); // true
```

Gruplu kullanım:

```java
Map<String, Long> byProject = tasks.stream()
        .collect(Collectors.groupingBy(
                Task::project,
                Collectors.counting()));
```

## 10. `size()` ve `count()`

```java
int totalWithSize = products.size();
long totalWithCount = products.stream().count();

long expensiveCount = products.stream()
        .filter(product -> product.price() > 100)
        .count();
```

Toplam koleksiyon boyutu için `size()` daha doğrudandır; filtrelenmiş sonuç için
`count()` anlamlıdır. `(long) products.size()` yalnızca tipi açıkça `long` yapmak
istenirse kullanılabilir, çoğu atama bağlamında otomatik widening yeterlidir.

