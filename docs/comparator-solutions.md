# Comparable ve Comparator Alıştırmaları — Örnek Çözümler

Bu dosya [Comparator requirement'larının](comparator-requirements.md) örnek
çözümlerini içerir. Önce requirement'ı kendin çözmeye çalışıp ardından buradaki
yaklaşımla karşılaştır.

## 1. SupportTicket doğal sırası

```java
class SupportTicket implements Comparable<SupportTicket> {
    private final long id;
    private final LocalDateTime createdAt;
    private final String subject;

    SupportTicket(long id, LocalDateTime createdAt, String subject) {
        this.id = id;
        this.createdAt = createdAt;
        this.subject = subject;
    }

    @Override
    public int compareTo(SupportTicket other) {
        return this.createdAt.compareTo(other.createdAt);
    }
}

List<SupportTicket> sorted = tickets.stream()
        .sorted()
        .toList();
```

`SupportTicket`, `Comparable` interface'ini implement ettiği için parametresiz
`sorted()` doğrudan `compareTo()` metodunu kullanır. Eski ticket doğal olarak
önce gelir.

## 2. Product için iki Comparator

```java
record Product(String name, int price) {}

Comparator<Product> byName =
        Comparator.comparing(Product::name);

Comparator<Product> byPrice =
        Comparator.comparingInt(Product::price);

List<Product> alphabetical = products.stream()
        .sorted(byName)
        .toList();

List<Product> cheapestFirst = products.stream()
        .sorted(byPrice)
        .toList();
```

Product hiçbir interface implement etmez. Aynı nesneler, seçilen ekranın
business ihtiyacına göre farklı Comparator'larla sıralanır.

## 3. Adayları yüksek puandan düşüğe sıralama

```java
record Candidate(String name, int interviewScore) {}

Comparator<Candidate> highestScoreFirst =
        Comparator.comparingInt(Candidate::interviewScore)
                .reversed();

List<Candidate> ranking = candidates.stream()
        .sorted(highestScoreFirst)
        .toList();
```

`comparingInt()` önce artan puan kuralı üretir; `reversed()` Comparator'ın
yönünü tersine çevirir.

## 4. Departman, sonra isim

```java
record Employee(String name, String department) {}

Comparator<Employee> byDepartmentThenName =
        Comparator.comparing(Employee::department)
                .thenComparing(Employee::name);

List<Employee> directory = employees.stream()
        .sorted(byDepartmentThenName)
        .toList();
```

İlk Comparator farklı departmanları sıralar. Departman karşılaştırması `0`
verirse `thenComparing()` isimleri karşılaştırır.

## 5. Üç seviyeli teslimat sırası

```java
record DeliveryOption(
        String company,
        int estimatedDays,
        int price
) {}

Comparator<DeliveryOption> preferredOrder =
        Comparator.comparingInt(DeliveryOption::estimatedDays)
                .thenComparingInt(DeliveryOption::price)
                .thenComparing(DeliveryOption::company);

List<DeliveryOption> optionsForCheckout = options.stream()
        .sorted(preferredOrder)
        .toList();
```

Comparator zincirinin yazım sırası business öncelik sırasıdır: hız, fiyat,
şirket adı.

## 6. Aynı Comparator ile `min()` ve `max()`

```java
Comparator<Product> byPrice =
        Comparator.comparingInt(Product::price);

Optional<Product> cheapest = products.stream().min(byPrice);
Optional<Product> mostExpensive = products.stream().max(byPrice);

String cheapestText = cheapest
        .map(Product::name)
        .orElse("No products found");

String mostExpensiveText = mostExpensive
        .map(Product::name)
        .orElse("No products found");
```

`min()` ve `max()` terminal operation'dır. Boş Stream'de eleman bulunamayacağı
için ikisi de `Optional<Product>` döndürür.

## 7. `null` soyadlarını sona koyma

```java
record Customer(String firstName, String lastName) {}

Comparator<Customer> byLastNameThenFirstName =
        Comparator.comparing(
                Customer::lastName,
                Comparator.nullsLast(Comparator.naturalOrder())
        ).thenComparing(Customer::firstName);

List<Customer> result = customers.stream()
        .sorted(byLastNameThenFirstName)
        .toList();
```

`nullsLast()` eksik soyadını hata yerine açık bir sıralama politikasıyla ele
alır. Soyadı dolu ve aynı olan kayıtlarda isim karşılaştırılır.

## 8. Klasik ve modern fiyat Comparator'ı

```java
class Invoice {
    private final long number;
    private final int amount;

    Invoice(long number, int amount) {
        this.number = number;
        this.amount = amount;
    }

    int getAmount() {
        return amount;
    }
}

class InvoiceAmountComparator
        implements Comparator<Invoice> {

    @Override
    public int compare(Invoice first, Invoice second) {
        return Integer.compare(
                first.getAmount(),
                second.getAmount()
        );
    }
}
```

Klasik kullanım:

```java
Comparator<Invoice> classic = new InvoiceAmountComparator();
List<Invoice> firstResult =
        invoices.stream().sorted(classic).toList();
```

Modern karşılığı:

```java
Comparator<Invoice> modern =
        Comparator.comparingInt(Invoice::getAmount);
List<Invoice> secondResult =
        invoices.stream().sorted(modern).toList();
```

İki Comparator aynı business davranışını taşır.

## 9. Özel sipariş önceliği

```java
record Order(
        long id,
        String priority,
        LocalDateTime createdAt
) {}

Map<String, Integer> priorityRank = Map.of(
        "HIGH", 1,
        "NORMAL", 2,
        "LOW", 3
);

Comparator<Order> operationalOrder =
        Comparator.comparingInt(
                order -> priorityRank.get(order.priority())
        ).thenComparing(Order::createdAt);

List<Order> queue = orders.stream()
        .sorted(operationalOrder)
        .toList();
```

`priorityRank`, metinlerin alfabetik sırası yerine işletmenin açık öncelik
sırasını sayısal değerlere dönüştürür. Aynı öncelikte eski sipariş önce gelir.
Üretim kodunda priority alanı için `enum` kullanmak daha güvenlidir.

## 10. Generic sıralama metodu

```java
static <T> List<T> sort(
        List<T> values,
        Comparator<? super T> comparator
) {
    return values.stream()
            .sorted(comparator)
            .toList();
}
```

Kullanımlar:

```java
List<Product> productsByPrice = sort(
        products,
        Comparator.comparingInt(Product::price)
);

List<Employee> employeesByName = sort(
        employees,
        Comparator.comparing(Employee::name)
);

List<Order> ordersByDate = sort(
        orders,
        Comparator.comparing(Order::createdAt)
);
```

Metot sıralama algoritmasını tekrar kullanır; karşılaştırma davranışı dışarıdan
verilir. Stream yaklaşımı kaynak listeyi değiştirmez ve yeni liste üretir.

