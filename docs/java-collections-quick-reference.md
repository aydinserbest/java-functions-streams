# Java Collections — Hızlı Oluşturma ve Kullanım Rehberi

Bu rehber, requirement veya mevcut bir Java kodunda koleksiyon gördüğünde yapıya
yabancı kalmaman ve küçük örnek verileri hızlıca oluşturabilmen için hazırlanmıştır.

## Önce temel ayrım: Interface ve gerçek sınıf

Java'da değişkenin sol tarafında çoğunlukla interface kullanılır:

```java
List<String> names;
Set<String> cities;
Map<String, Integer> ages;
Queue<String> tasks;
Deque<String> history;
```

Sağ tarafta ise koleksiyonu oluşturan yöntem veya gerçek sınıf bulunur:

```java
List<String> names = new ArrayList<>();
Set<String> cities = new HashSet<>();
Map<String, Integer> ages = new HashMap<>();
Queue<String> tasks = new ArrayDeque<>();
```

Kısa zihinsel model:

```text
Sol taraf  → Nasıl kullanılacağını belirten genel tür
Sağ taraf → Gerçekte oluşturulan koleksiyon veya fabrika metodu
```

## 1. `List` — Sıralı ve tekrar eden elemanlar

`List`, eleman sırasını korur ve aynı değerin birden fazla kez bulunmasına izin
verir.

```java
List<String> names = List.of("Alice", "Bob", "Alice");
```

Sonuç:

```text
[Alice, Bob, Alice]
```

### `List.of()` — Sabit örnek liste

```java
List<String> names = List.of(
        "Alice",
        "Bob",
        "Charlie"
);
```

Sayı listesi:

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5);
```

Nesne listesi:

```java
List<Person> persons = List.of(
        new Person("Alice", 30),
        new Person("Bob", 25)
);
```

`List.of()` ile oluşturulan liste değiştirilemez:

```java
names.add("David");    // UnsupportedOperationException
names.remove("Alice"); // UnsupportedOperationException
names.set(0, "Ayşe");  // UnsupportedOperationException
```

Ayrıca `null` eleman kabul etmez:

```java
List.of("Alice", null); // NullPointerException
```

Şu durumlarda kullan:

- Alıştırma için hazır veri oluştururken
- Listenin sonradan değişmeyeceği belliyse
- Kısa ve modern bir yazım istiyorsan

### `Arrays.asList()` — Sabit boyutlu, elemanları değiştirilebilir liste

```java
List<String> names = Arrays.asList(
        "Alice",
        "Bob",
        "Charlie"
);
```

Mevcut eleman değiştirilebilir:

```java
names.set(0, "Ayşe");
System.out.println(names); // [Ayşe, Bob, Charlie]
```

Fakat eleman eklenemez veya silinemez:

```java
names.add("David");  // UnsupportedOperationException
names.remove("Bob"); // UnsupportedOperationException
```

Bir array'i liste görünümünde kullanmak için yaygındır:

```java
String[] nameArray = {"Alice", "Bob", "Charlie"};
List<String> names = Arrays.asList(nameArray);
```

Array ile liste birbirine bağlıdır:

```java
nameArray[0] = "Ayşe";
System.out.println(names); // [Ayşe, Bob, Charlie]
```

### `new ArrayList<>()` — Tam değiştirilebilir liste

Boş liste oluşturup sonradan doldurmak:

```java
List<String> names = new ArrayList<>();

names.add("Alice");
names.add("Bob");
names.add("Charlie");
```

Hazır değerlerle değiştirilebilir liste oluşturmak:

```java
List<String> names = new ArrayList<>(
        List.of("Alice", "Bob", "Charlie")
);
```

Artık bütün temel değişiklikler yapılabilir:

```java
names.add("David");
names.remove("Bob");
names.set(0, "Ayşe");

System.out.println(names); // [Ayşe, Charlie, David]
```

### Yaygın `List` işlemleri

```java
List<String> names = new ArrayList<>(List.of("Alice", "Bob", "Charlie"));

names.get(0);              // Alice
names.size();              // 3
names.contains("Bob");     // true
names.indexOf("Charlie");  // 2
names.add("David");
names.remove("Bob");
names.isEmpty();            // false
```

Listedeki her eleman üzerinde işlem yapmak:

```java
names.forEach(System.out::println);
```

## 2. `Set` — Tekrarsız elemanlar

`Set`, aynı değeri yalnızca bir kez tutar. Requirement'ta "benzersiz", "tekil"
veya "duplicate olmasın" deniyorsa akla Set gelir.

### `Set.of()` — Değiştirilemez Set

```java
Set<String> roles = Set.of(
        "ADMIN",
        "USER",
        "SUPPORT"
);
```

```java
roles.contains("ADMIN"); // true
roles.size();             // 3
```

Değiştirilemez ve tekrar eden başlangıç değeri kabul etmez:

```java
Set.of("ADMIN", "ADMIN"); // IllegalArgumentException
roles.add("MANAGER");      // UnsupportedOperationException
```

### `HashSet` — Değiştirilebilir, tekrarsız koleksiyon

```java
Set<String> cities = new HashSet<>();

cities.add("Amsterdam");
cities.add("Rotterdam");
cities.add("Amsterdam");

System.out.println(cities);
// Amsterdam yalnızca bir kez bulunur; sıra garanti edilmez.
```

Hazır verilerle:

```java
Set<String> cities = new HashSet<>(
        List.of("Amsterdam", "Rotterdam", "Utrecht")
);
```

### `LinkedHashSet` — Ekleme sırasını koruyan Set

```java
Set<String> cities = new LinkedHashSet<>();

cities.add("Amsterdam");
cities.add("Rotterdam");
cities.add("Utrecht");
```

Gezme sırası ekleme sırasıdır:

```text
Amsterdam, Rotterdam, Utrecht
```

### `TreeSet` — Sıralı Set

```java
Set<Integer> scores = new TreeSet<>();

scores.add(90);
scores.add(70);
scores.add(85);

System.out.println(scores); // [70, 85, 90]
```

### Yaygın `Set` işlemleri

```java
Set<String> tags = new HashSet<>();

tags.add("java");
tags.add("lambda");
tags.contains("java"); // true
tags.remove("lambda");
tags.size();
tags.isEmpty();
```

## 3. `Map` — Key-value eşleşmeleri

`Map`, her key'i bir value ile eşleştirir. Requirement'ta "isme göre yaş",
"ürün koduna göre ürün" veya "ülkeye göre müşteri sayısı" deniyorsa Map uygun
olabilir.

```text
Key    → Value
Alice  → 30
Bob    → 25
```

### `Map.of()` — Küçük, değiştirilemez Map

```java
Map<String, Integer> ages = Map.of(
        "Alice", 30,
        "Bob", 25
);
```

Değere key ile erişilir:

```java
ages.get("Alice");         // 30
ages.containsKey("Bob");  // true
ages.containsValue(25);    // true
```

Değiştirilemez:

```java
ages.put("Charlie", 40); // UnsupportedOperationException
ages.remove("Alice");    // UnsupportedOperationException
```

Aynı key birden fazla kez bulunamaz ve `null` kabul edilmez.

### `Map.ofEntries()` — Daha okunabilir çoklu kayıt

Kayıt sayısı arttığında:

```java
Map<String, Integer> productStocks = Map.ofEntries(
        Map.entry("LAPTOP", 5),
        Map.entry("MOUSE", 20),
        Map.entry("KEYBOARD", 8),
        Map.entry("MONITOR", 3)
);
```

Bu Map de değiştirilemez.

### `HashMap` — Değiştirilebilir Map

```java
Map<String, Integer> ages = new HashMap<>();

ages.put("Alice", 30);
ages.put("Bob", 25);
ages.put("Charlie", 40);
```

Aynı key tekrar verilirse eski value değiştirilir:

```java
ages.put("Alice", 31);
System.out.println(ages.get("Alice")); // 31
```

Hazır Map'ten değiştirilebilir kopya:

```java
Map<String, Integer> mutableAges = new HashMap<>(
        Map.of("Alice", 30, "Bob", 25)
);

mutableAges.put("Charlie", 40);
```

### `LinkedHashMap` — Ekleme sırasını koruyan Map

```java
Map<Long, String> orders = new LinkedHashMap<>();

orders.put(1001L, "NEW");
orders.put(1002L, "PAID");
orders.put(1003L, "SHIPPED");
```

Kayıtlar eklenme sırasıyla gezilir.

### `TreeMap` — Key'e göre sıralı Map

```java
Map<Integer, String> students = new TreeMap<>();

students.put(3, "Charlie");
students.put(1, "Alice");
students.put(2, "Bob");

System.out.println(students);
// {1=Alice, 2=Bob, 3=Charlie}
```

### Map üzerinde gezinme

Yalnızca key'ler:

```java
for (String name : ages.keySet()) {
    System.out.println(name);
}
```

Yalnızca value'lar:

```java
for (Integer age : ages.values()) {
    System.out.println(age);
}
```

Key ve value birlikte:

```java
for (Map.Entry<String, Integer> entry : ages.entrySet()) {
    System.out.println(entry.getKey() + " -> " + entry.getValue());
}
```

`forEach()` ile:

```java
ages.forEach((name, age) ->
        System.out.println(name + " -> " + age)
);
```

## 4. `Queue` — İlk giren ilk çıkar

`Queue`, çoğunlukla sırayla işlenecek görevler için kullanılır. FIFO mantığı:

```text
First In, First Out
İlk giren, ilk çıkar
```

Yaygın oluşturma şekli:

```java
Queue<String> supportTickets = new ArrayDeque<>();
```

Kuyruğa eklemek:

```java
supportTickets.offer("Ticket-1001");
supportTickets.offer("Ticket-1002");
supportTickets.offer("Ticket-1003");
```

Baştaki değeri silmeden görmek:

```java
String nextTicket = supportTickets.peek();
// Ticket-1001
```

Baştaki değeri alıp kuyruktan çıkarmak:

```java
String processedTicket = supportTickets.poll();
// Ticket-1001
```

Kalan kuyruk:

```text
[Ticket-1002, Ticket-1003]
```

Business örneği:

- Müşteri destek talepleri
- Yazdırma işleri
- Arka plan görevleri
- İşlenecek siparişler

## 5. `Deque` — İki uçtan ekleme ve çıkarma

`Deque`, hem baştan hem sondan işlem yapılabilen kuyruktur:

```java
Deque<String> navigationHistory = new ArrayDeque<>();
```

Sona eklemek:

```java
navigationHistory.addLast("Home");
navigationHistory.addLast("Products");
navigationHistory.addLast("Product Detail");
```

Son elemanı görmek veya çıkarmak:

```java
navigationHistory.peekLast(); // Product Detail
navigationHistory.pollLast(); // Product Detail çıkarılır
```

Başa eklemek:

```java
navigationHistory.addFirst("Login");
```

Stack gibi de kullanılabilir:

```java
Deque<String> undoStack = new ArrayDeque<>();

undoStack.push("Text typed");
undoStack.push("Image inserted");

String lastAction = undoStack.pop();
// Image inserted
```

Business örneği:

- Geri alma geçmişi
- Tarayıcı gezinme geçmişi
- İki öncelik ucundan işlenen görevler

## 6. Nesne koleksiyonları

Gerçek requirement'larda çoğunlukla String veya Integer yerine kendi nesnelerin
kullanılır.

Model:

```java
record Product(String name, double price, boolean inStock) {}
```

Product listesi:

```java
List<Product> products = List.of(
        new Product("Laptop", 1200, true),
        new Product("Mouse", 40, true),
        new Product("Monitor", 300, false)
);
```

Product Set'i:

```java
Set<Product> uniqueProducts = new HashSet<>(products);
```

Ürün koduna göre Product Map'i:

```java
Map<String, Product> productsByCode = Map.of(
        "P-100", new Product("Laptop", 1200, true),
        "P-200", new Product("Mouse", 40, true)
);
```

Key ile ürüne ulaşma:

```java
Product laptop = productsByCode.get("P-100");
```

## 7. Bir koleksiyondan diğerine kopyalama

Sabit List'ten değiştirilebilir List:

```java
List<String> mutableNames = new ArrayList<>(
        List.of("Alice", "Bob")
);
```

List'ten Set'e geçip tekrarları kaldırma:

```java
List<String> names = List.of("Alice", "Bob", "Alice");
Set<String> uniqueNames = new HashSet<>(names);
```

Set'ten List'e dönüştürme:

```java
List<String> nameList = new ArrayList<>(uniqueNames);
```

Map'ten değiştirilebilir kopya:

```java
Map<String, Integer> mutableAges = new HashMap<>(
        Map.of("Alice", 30, "Bob", 25)
);
```

Değiştirilemez kopya oluşturma:

```java
List<String> immutableNames = List.copyOf(mutableNames);
Set<String> immutableCities = Set.copyOf(uniqueNames);
Map<String, Integer> immutableAges = Map.copyOf(mutableAges);
```

## 8. Requirement cümlesinden koleksiyon seçmek

### “Kayıtları sırayla tut”

Genellikle `List`:

```java
List<Order> orders = new ArrayList<>();
```

### “Tekrarsız şehirleri bul”

Genellikle `Set`:

```java
Set<String> cities = new HashSet<>();
```

### “Her ürün kodunu bir ürünle eşleştir”

Genellikle `Map`:

```java
Map<String, Product> productsByCode = new HashMap<>();
```

### “Talepleri geliş sırasına göre işle”

Genellikle `Queue`:

```java
Queue<Ticket> tickets = new ArrayDeque<>();
```

### “Son işlemi geri al”

Stack davranışı için `Deque`:

```java
Deque<Action> undoHistory = new ArrayDeque<>();
```

## 9. Hızlı tercih tablosu

| İhtiyaç | Yaygın tercih |
|---|---|
| Değişmeyecek küçük liste | `List.of(...)` |
| Mevcut array'i liste olarak görmek | `Arrays.asList(array)` |
| Eleman eklenecek/silinecek liste | `new ArrayList<>()` |
| Tekrarsız, sırası önemsiz değerler | `new HashSet<>()` |
| Tekrarsız, ekleme sırası önemli değerler | `new LinkedHashSet<>()` |
| Tekrarsız ve sıralı değerler | `new TreeSet<>()` |
| Değişmeyecek küçük key-value verisi | `Map.of(...)` |
| Değiştirilebilir key-value verisi | `new HashMap<>()` |
| Ekleme sırası önemli Map | `new LinkedHashMap<>()` |
| Key'e göre sıralı Map | `new TreeMap<>()` |
| İlk giren ilk çıksın | `Queue` + `ArrayDeque` |
| Baştan ve sondan işlem yapılsın | `Deque` + `ArrayDeque` |

## 10. En sık kullanılacak başlangıç kalıpları

Sabit String listesi:

```java
List<String> names = List.of("Alice", "Bob", "Charlie");
```

Değiştirilebilir String listesi:

```java
List<String> names = new ArrayList<>();
names.add("Alice");
names.add("Bob");
```

Sabit nesne listesi:

```java
List<Person> persons = List.of(
        new Person("Alice", 30),
        new Person("Bob", 25)
);
```

Değiştirilebilir Set:

```java
Set<String> cities = new HashSet<>();
cities.add("Amsterdam");
```

Sabit Map:

```java
Map<String, Integer> ages = Map.of(
        "Alice", 30,
        "Bob", 25
);
```

Değiştirilebilir Map:

```java
Map<String, Integer> ages = new HashMap<>();
ages.put("Alice", 30);
ages.put("Bob", 25);
```

Queue:

```java
Queue<String> tasks = new ArrayDeque<>();
tasks.offer("First task");
tasks.offer("Second task");
```

Deque/stack:

```java
Deque<String> history = new ArrayDeque<>();
history.push("First action");
history.push("Second action");
```

Bu kalıpların başında ihtiyaç duyulan import'lar genellikle şunlardır:

```java
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
```

