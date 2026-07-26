# Stream `collect()` Alıştırmaları — Örnek Çözümler

`collect()` terminal operation'dır. `Collectors` sınıfındaki hazır tarifler,
sonuç kabının nasıl oluşturulacağını ve elemanların nasıl biriktirileceğini
belirler.

## 1. Değiştirilebilir aktif ürün listesi

```java
List<Product> active = products.stream()
        .filter(Product::active)
        .collect(Collectors.toCollection(ArrayList::new));

active.add(new Product("Keyboard", true));
```

`ArrayList::new` sonuç kabını üreten constructor reference'tır.

## 2. Benzersiz şehirler

```java
Set<String> cities = customers.stream()
        .map(Customer::city)
        .collect(Collectors.toSet());
```

`Set` tekrarları kaldırır; sıralama garantisi istenmemiştir.

## 3. Departmana göre çalışanlar

```java
Map<String, List<Employee>> byDepartment = employees.stream()
        .collect(Collectors.groupingBy(Employee::department));
```

Classifier fonksiyonu her çalışanın hangi anahtar grubuna gireceğini belirler.

## 4. Durum başına sipariş sayısı

```java
Map<String, Long> countByStatus = orders.stream()
        .collect(Collectors.groupingBy(
                Order::status,
                Collectors.counting()
        ));
```

`counting()` her grup içinde çalışan downstream collector'dır.

## 5. SKU haritası

```java
Map<String, Product> bySku = products.stream()
        .collect(Collectors.toMap(Product::sku, Function.identity()));
```

`Function.identity()` ürünün kendisini map değeri yapar.

## 6. En güncel kullanıcı kaydı

```java
Map<String, User> latestByUsername = users.stream()
        .collect(Collectors.toMap(
                User::username,
                Function.identity(),
                (first, second) -> first.lastLogin().isAfter(second.lastLogin())
                        ? first : second
        ));
```

Üçüncü argüman duplicate key oluştuğunda hangi değerin kalacağını belirler.

## 7. Fatura metni

```java
String invoiceText = invoiceNumbers.stream()
        .collect(Collectors.joining(", ", "[", "]"));
```

Boş Stream sonucu `"[]"` olur.

## 8. Geçti/kaldı grupları

```java
Map<Boolean, List<Student>> result = students.stream()
        .collect(Collectors.partitioningBy(student -> student.grade() >= 60));
```

`partitioningBy` her zaman boolean anahtarlı iki bölümü temsil eder.

## 9. Departman maaşları

```java
Map<String, Double> payrollByDepartment = employees.stream()
        .collect(Collectors.groupingBy(
                Employee::department,
                Collectors.summingDouble(Employee::salary)
        ));
```

## 10. Sıralı benzersiz sipariş ID'leri

```java
Set<Long> ids = orders.stream()
        .map(Order::id)
        .collect(Collectors.toCollection(TreeSet::new));
```

Çalışma zamanındaki sonuç nesnesi `TreeSet<Long>` olur.

