# `Collectors.groupingBy()` — Örnek Çözümler

## 1. Departman
```java
Map<String,List<Employee>> result = employees.stream()
        .collect(Collectors.groupingBy(Employee::department));
```

## 2. Kategori
```java
Map<String,List<Product>> result = products.stream()
        .collect(Collectors.groupingBy(Product::category));
```

## 3. Durum sayıları
```java
Map<String,Long> counts = orders.stream()
        .collect(Collectors.groupingBy(Order::status, Collectors.counting()));
```

## 4. Şehir ortalaması
```java
Map<String,Double> averages = employees.stream().collect(
        Collectors.groupingBy(Employee::city,
                Collectors.averagingDouble(Employee::salary)));
```

## 5. İç içe coğrafya
```java
Map<String,Map<String,List<Customer>>> result = customers.stream().collect(
        Collectors.groupingBy(Customer::country,
                Collectors.groupingBy(Customer::city)));
```

## 6. İsim değerleri
```java
Map<Integer,List<String>> result = products.stream().collect(
        Collectors.groupingBy(Product::price,
                Collectors.mapping(Product::name, Collectors.toList())));
```

## 7. Stok toplamı
```java
Map<String,Integer> stock = products.stream().collect(
        Collectors.groupingBy(Product::category,
                Collectors.summingInt(Product::stock)));
```

## 8. Departman maksimumu
```java
Map<String,Optional<Employee>> result = employees.stream().collect(
        Collectors.groupingBy(Employee::department,
                Collectors.maxBy(Comparator.comparingDouble(Employee::salary))));
```

## 9. TreeMap sonucu
```java
Map<String,List<Employee>> result = employees.stream().collect(
        Collectors.groupingBy(
                Employee::department, TreeMap::new, Collectors.toList()));
```

## 10. Fiyat bandı
```java
Map<String,List<Product>> bands = products.stream().collect(
        Collectors.groupingBy(product -> {
            if (product.price() < 100) return "BUDGET";
            if (product.price() <= 500) return "STANDARD";
            return "PREMIUM";
        }));
```

