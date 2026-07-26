# Karışık Stream Pipeline Alıştırmaları — Örnek Çözümler

Kod parçaları modellerin `record` olarak tanımlandığını varsayar. Bazı
senaryolarda aynı seçilmiş veriden birden fazla terminal sonuç gerekir; Stream
yeniden kullanılamadığı için ya ara liste oluşturulur ya da kaynaktan yeni
pipeline başlatılır.

## 1. Premium vitrin

```java
List<String> showcase = products.stream()
        .filter(p -> p.active() && p.stock() > 0 && p.price() > 100)
        .sorted(Comparator.comparingDouble(Product::price).reversed())
        .limit(4)
        .map(p -> "%s - €%.2f".formatted(p.name(), p.price()))
        .toList();
```

## 2. Sevkiyat ürün adedi

```java
int quantity = orders.stream()
        .filter(order -> "PAID".equals(order.status()))
        .flatMap(order -> order.lines().stream())
        .mapToInt(OrderLine::quantity)
        .sum();
```

## 3. Departman aktif çalışan sayısı

```java
record DepartmentEmployee(String department, Employee employee) {}

Map<String, Long> counts = departments.stream()
        .flatMap(d -> d.employees().stream()
                .map(e -> new DepartmentEmployee(d.name(), e)))
        .filter(item -> item.employee().active())
        .collect(Collectors.groupingBy(
                DepartmentEmployee::department,
                Collectors.counting()));
```

## 4. Üçüncü arama sayfası

```java
List<Product> page = products.stream()
        .filter(p -> p.active()
                && p.name().toLowerCase(Locale.ROOT).contains(query))
        .sorted(Comparator.comparing(Product::name))
        .skip(20)
        .limit(10)
        .toList();
```

## 5. En çok gelir getiren kategoriler

```java
Map<String, Double> totals = sales.stream()
        .filter(sale -> !sale.refunded())
        .collect(Collectors.groupingBy(
                Sale::category,
                Collectors.summingDouble(
                        sale -> sale.quantity() * sale.unitPrice())));

List<Map.Entry<String, Double>> topThree = totals.entrySet().stream()
        .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
        .limit(3)
        .toList();
```

## 6. Kampanya alıcıları

```java
List<String> recipients = customers.stream()
        .filter(c -> c.active() && c.consent()
                && c.email() != null && c.email().contains("@"))
        .map(c -> c.email().trim().toLowerCase(Locale.ROOT))
        .distinct()
        .limit(100)
        .collect(Collectors.toCollection(ArrayList::new));
```

## 7. Değerlendirme özeti

```java
List<Review> approved = product.reviews().stream()
        .filter(Review::approved)
        .toList();

long count = approved.size();
double average = approved.stream()
        .mapToInt(Review::rating)
        .average()
        .orElse(0.0);
```

Ara liste aynı seçilmiş kayıtların iki metrikte tutarlı kullanılmasını sağlar.

## 8. Yedek teslimatlar

```java
List<DeliveryOption> alternatives = options.stream()
        .filter(DeliveryOption::available)
        .sorted(Comparator.comparingInt(DeliveryOption::days)
                .thenComparingDouble(DeliveryOption::price))
        .skip(1)
        .limit(2)
        .toList();
```

## 9. Acil görevler

```java
record ProjectTask(String project, Task task) {}

List<String> urgent = projects.stream()
        .flatMap(p -> p.tasks().stream()
                .map(t -> new ProjectTask(p.name(), t)))
        .filter(item -> !item.task().completed())
        .sorted(Comparator.comparingInt(
                (ProjectTask item) -> item.task().priority()).reversed())
        .limit(5)
        .map(item -> item.project() + ": " + item.task().title())
        .toList();
```

## 10. Riskli banka hareketleri

```java
List<Transaction> risky = accounts.stream()
        .flatMap(account -> account.transactions().stream())
        .filter(t -> t.successful()
                && !t.country().equals("Netherlands")
                && t.amount() > 1000)
        .toList();

long count = risky.size();
double total = risky.stream().mapToDouble(Transaction::amount).sum();
```

## 11. İkinci destek partisi

```java
List<Ticket> secondBatch = tickets.stream()
        .filter(ticket -> "OPEN".equals(ticket.status()))
        .sorted(Comparator.comparing(Ticket::createdAt))
        .skip(20)
        .limit(20)
        .toList();
```

## 12. Benzersiz eğitmenler

```java
List<Instructor> instructors = courses.stream()
        .filter(Course::published)
        .flatMap(course -> course.instructors().stream())
        .collect(Collectors.toMap(
                Instructor::id,
                Function.identity(),
                (first, duplicate) -> first))
        .values().stream()
        .sorted(Comparator.comparing(Instructor::name))
        .toList();
```

ID, nesne tekilleştirmesinde güvenilir anahtardır.

## 13. Müşteri başına borç

```java
Map<String, Double> debtByCustomer = invoices.stream()
        .filter(invoice -> !invoice.paid())
        .collect(Collectors.groupingBy(
                Invoice::customer,
                Collectors.summingDouble(Invoice::amount)));
```

## 14. Son beş benzersiz arama

```java
List<String> recentQueries = events.stream()
        .filter(event -> event.userId().equals(selectedUserId))
        .sorted(Comparator.comparing(SearchEvent::time).reversed())
        .map(event -> event.query().trim().toLowerCase(Locale.ROOT))
        .distinct()
        .limit(5)
        .toList();
```

Sıralama `distinct()` öncesinde olduğu için en yeni tekrar korunur.

## 15. Şube ürün çeşitleri

```java
Map<String, Long> varietyByBranch = branches.stream()
        .collect(Collectors.toMap(
                Branch::city,
                branch -> branch.products().stream()
                        .map(Product::sku)
                        .distinct()
                        .count()
        ));
```

## 16. Bordro üst sınır raporu

```java
List<Employee> selected = employees.stream()
        .filter(Employee::active)
        .sorted(Comparator.comparingDouble(Employee::monthlySalary).reversed())
        .skip(2)
        .limit(5)
        .toList();

List<String> rows = selected.stream()
        .map(e -> e.name() + " - " + e.monthlySalary() * 12)
        .toList();
double annualTotal = selected.stream()
        .mapToDouble(e -> e.monthlySalary() * 12)
        .sum();
```

## 17. Katılımcı ülke özeti

```java
Map<String, Long> byCountry = events.stream()
        .filter(Event::published)
        .flatMap(event -> event.participants().stream())
        .filter(Participant::approved)
        .collect(Collectors.groupingBy(
                Participant::country,
                Collectors.counting()));
```

## 18. Ekonomik menü bandı

```java
List<Dish> dishes = categories.stream()
        .flatMap(category -> category.dishes().stream())
        .filter(dish -> dish.available() && dish.price() < 20)
        .sorted(Comparator.comparingDouble(Dish::price))
        .skip(3)
        .limit(5)
        .toList();
```

## 19. En sık hata kodları

```java
Map<String, Long> counts = batches.stream()
        .filter(batch -> batch.date().equals(today))
        .flatMap(batch -> batch.entries().stream())
        .filter(entry -> "ERROR".equals(entry.level()))
        .collect(Collectors.groupingBy(
                LogEntry::code,
                Collectors.counting()));

List<Map.Entry<String, Long>> topCodes = counts.entrySet().stream()
        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
        .limit(3)
        .toList();
```

## 20. Sipariş dashboard'u

```java
List<Order> valid = orders.stream()
        .filter(order -> !"CANCELLED".equals(order.status()))
        .toList();

long orderCount = valid.size();
double revenue = valid.stream().mapToDouble(Order::total).sum();
int itemCount = valid.stream()
        .flatMap(order -> order.lines().stream())
        .mapToInt(OrderLine::quantity)
        .sum();
Map<String, Long> countByStatus = valid.stream()
        .collect(Collectors.groupingBy(
                Order::status, Collectors.counting()));
List<Order> topThree = valid.stream()
        .sorted(Comparator.comparingDouble(Order::total).reversed())
        .limit(3)
        .toList();
```

Ortak ara liste, bütün KPI'ların aynı “iptal olmayan sipariş” tanımını
kullanmasını sağlar.

