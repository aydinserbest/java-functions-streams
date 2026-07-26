# Java `Map` Alıştırmaları — Örnek Çözümler

## 1. Stok Map'i
```java
Map<String,Integer> stock = new HashMap<>();
stock.put("LAPTOP", 5);
stock.put("LAPTOP", 6);
int value = stock.get("LAPTOP");
```

## 2. Varsayılan stok
```java
int value = stock.getOrDefault("MONITOR", 0);
```

## 3. Dolaşma
```java
stock.keySet().forEach(System.out::println);
stock.values().forEach(System.out::println);
for (Map.Entry<String,Integer> entry : stock.entrySet()) {
    System.out.println(entry.getKey() + " -> " + entry.getValue());
}
```

## 4. Product lookup
```java
Map<Integer,Product> byId = new HashMap<>();
byId.put(product.id(), product);
Product result = byId.getOrDefault(999, UNKNOWN_PRODUCT);
```

## 5. Depo listeleri
```java
Map<Integer,List<Product>> byWarehouse = new HashMap<>();
byWarehouse.put(1, new ArrayList<>(List.of(laptop, mouse)));
List<Product> firstWarehouse = byWarehouse.get(1);
```

## 6. Otomatik grup
```java
productsByCategory
        .computeIfAbsent("Electronics", key -> new ArrayList<>())
        .add(laptop);
```

## 7. Frekans
```java
Map<String,Integer> frequency = new HashMap<>();
words.forEach(word -> frequency.merge(word, 1, Integer::sum));
```

## 8. Eski value
```java
String oldValue = settings.put("theme", "dark");
oldValue = settings.put("theme", "light"); // "dark"
```

## 9. Implementasyonlar
```java
Map<String,Integer> fastLookup = new HashMap<>();
Map<String,Integer> insertionOrder = new LinkedHashMap<>();
Map<String,Integer> keyOrder = new TreeMap<>();
```

## 10. Kontrollü güncelleme
```java
stock.putIfAbsent("MOUSE", 20);
stock.replace("MOUSE", 18);
stock.computeIfPresent("MOUSE", (code, amount) -> Math.max(0, amount - 1));
```

