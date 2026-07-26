# Java `Comparable` ve `Comparator` — Hatırlatma Rehberi

Bu dokümanın amacı bir nesnenin nasıl sıralandığını, `Comparable` ile
`Comparator` farkını ve aynı sınıf için birden fazla sıralama kuralının nasıl
yazıldığını yeniden hatırlatmaktır.

## 1. Önce business sorusu: “Neye göre?”

Bir sayı listesinde sıralama çoğunlukla açıktır:

```text
5, 10, 20, 40
```

Fakat bir ürün listesinde “ürünleri sırala” tek başına yeterli değildir:

```text
Ada göre mi?
Fiyata göre mi?
Stok miktarına göre mi?
Önce kategoriye, aynı kategoride fiyata göre mi?
```

Java'nın iki Product nesnesini karşılaştırabilmesi için bu business kuralının
tanımlanması gerekir.

## 2. Karşılaştırma sonucundaki `int` ne anlatır?

Hem `Comparable.compareTo()` hem `Comparator.compare()` bir `int` döndürür.
Sayının tam değeri değil, işareti önemlidir:

```text
negatif → birinci değer önce gelir
0       → sıralama bakımından eşittir
pozitif → birinci değer sonra gelir
```

Örneğin:

```java
int result = Integer.compare(100, 300); // negatif
```

Bu, `100` değerinin `300` değerinden önce gelmesi anlamına gelir.

## 3. `Comparable`: sınıfın doğal sırası

`Comparable<T>`, sınıfın kendi varsayılan veya doğal sırasını tanımlar:

```java
class Person implements Comparable<Person> {
    private final String name;

    @Override
    public int compareTo(Person other) {
        return this.name.compareTo(other.name);
    }
}
```

Burada verilen karar:

> Person nesnelerinin doğal sırası ada göredir.

Artık parametresiz `sorted()` kullanılabilir:

```java
List<Person> sorted = people.stream()
        .sorted()
        .toList();
```

`sorted()`, her Person için sınıftaki `compareTo()` kuralından yararlanır.

### `extends` mi, `implements` mi?

`Comparable` bir interface olduğu için:

```java
implements Comparable<Person>
```

yazılır. Şu ifade yanlıştır:

```java
extends Comparable<Person> // yanlış: Comparable bir class değildir
```

Bir class başka bir class'ı `extends`, bir interface'i `implements` eder.

## 4. `Integer` ve `String` neden doğrudan sıralanabiliyor?

`Integer` ve `String` sınıfları `Comparable` interface'ini Java tarafından zaten
implement eder:

```java
public final class Integer implements Comparable<Integer>
public final class String implements Comparable<String>
```

Bu nedenle şunlar doğrudan çalışır:

```java
List<Integer> numbers = List.of(40, 5, 20, 10);
List<Integer> sortedNumbers = numbers.stream().sorted().toList();

List<String> cities = List.of("Paris", "Amsterdam", "Berlin");
List<String> sortedCities = cities.stream().sorted().toList();
```

Java:

- `Integer` için sayısal doğal sırayı,
- `String` için karakterlerin doğal sırasını

zaten bilir. Biz yeniden karşılaştırma metodu yazmayız.

## 5. Product veya Person için her zaman `Comparable` gerekli mi?

Hayır. İki seçeneğimiz vardır.

### Seçenek A: Sınıfın açık bir doğal sırası varsa `Comparable`

Örneğin çalışan numarası bir çalışanın sistemdeki kalıcı doğal sırası kabul
ediliyorsa:

```java
class Employee implements Comparable<Employee> {
    private long employeeNumber;

    @Override
    public int compareTo(Employee other) {
        return Long.compare(this.employeeNumber, other.employeeNumber);
    }
}
```

### Seçenek B: Business ihtiyacına göre dışarıdan `Comparator`

```java
Comparator<Product> byPrice =
        Comparator.comparingInt(Product::getPrice);
```

Bu durumda Product'ın `Comparable` implement etmesi gerekmez:

```java
List<Product> result = products.stream()
        .sorted(byPrice)
        .toList();
```

Gerçek uygulamalarda bir domain nesnesinin tartışmasız tek bir doğal sırası yoksa
dış `Comparator` kullanmak çoğunlukla daha açıktır.

## 6. `Comparator`: dışarıdan verilen sıralama davranışı

`Comparator<T>` bir functional interface'tir. Temel abstract metodu:

```java
int compare(T first, T second);
```

Lambda ile yazılabilir:

```java
Comparator<Product> byPrice =
        (first, second) -> Integer.compare(
                first.getPrice(),
                second.getPrice()
        );
```

Hazır factory metotla daha okunaklı biçimi:

```java
Comparator<Product> byPrice =
        Comparator.comparingInt(Product::getPrice);
```

İkisi de şu davranışı taşır:

```text
İki Product al
      ↓
price alanlarını karşılaştır
      ↓
negatif, 0 veya pozitif int döndür
```

`Comparator`, Product sınıfının dışında tanımlandığı için sınıfı değiştirmeden
farklı ekranlarda farklı sıralamalar kullanılabilir.

## 7. Bir sınıf için yalnızca bir Comparator mı olur?

Hayır. Hatırlamaya çalıştığın önemli ayrım budur:

```text
Comparable → sınıfın genellikle bir doğal sırası
Comparator → ihtiyaca göre birçok farklı sıra
```

Aynı Product sınıfı için istediğimiz kadar Comparator oluşturabiliriz:

```java
Comparator<Product> byName =
        Comparator.comparing(Product::getName);

Comparator<Product> byPrice =
        Comparator.comparingInt(Product::getPrice);

Comparator<Product> byPriceDescending =
        Comparator.comparingInt(Product::getPrice).reversed();
```

Business kullanım örnekleri:

```text
Katalog ekranı      → ada göre
Fiyat karşılaştırma → ucuzdan pahalıya
Premium vitrin      → pahalıdan ucuza
Stok raporu         → stok miktarına göre
```

Product sınıfı aynı kalır; dışarıdan verilen karşılaştırma davranışı değişir.

## 8. `comparing`, `comparingInt` ve benzerleri

Nesneden karşılaştırılacak alanı çıkaran hazır metotlar vardır:

```java
Comparator.comparing(Product::getName)
Comparator.comparingInt(Product::getPrice)
Comparator.comparingLong(Order::getId)
Comparator.comparingDouble(Product::getWeight)
```

`comparingInt`, primitive `int` alanlar için özel sürümdür ve gereksiz boxing'i
engeller:

```java
Comparator<Product> byPrice =
        Comparator.comparingInt(Product::getPrice);
```

Tip akışı:

```text
Product
   ↓ Product::getPrice
int
   ↓
Karşılaştırma
```

## 9. Sıralama yönünü tersine çevirme

```java
Comparator<Product> expensiveFirst =
        Comparator.comparingInt(Product::getPrice)
                .reversed();
```

```text
Normal sıra   → 100, 300, 600, 1200
reversed()    → 1200, 600, 300, 100
```

Comparator tekrar yazılmaz; mevcut davranış tersine çevrilir.

## 10. İkinci karşılaştırma kuralı: `thenComparing`

Hatırladığın “ikinciyi farklı mı yazıyorduk?” konusu büyük ihtimalle
`thenComparing()` olabilir.

Önce departmana, departman aynıysa isme göre sıralama:

```java
Comparator<Employee> byDepartmentThenName =
        Comparator.comparing(Employee::getDepartment)
                .thenComparing(Employee::getName);
```

Akış:

```text
Departmanlar farklıysa
→ departman sonucu kullanılır

Departmanlar aynıysa
→ isim karşılaştırması çalışır
```

Başka örnek:

```java
Comparator<Product> byPriceThenName =
        Comparator.comparingInt(Product::getPrice)
                .thenComparing(Product::getName);
```

Fiyatları aynı olan ürünlerde isim, kararlı bir ikinci business kuralı olur.

## 11. `Comparable` ve `Comparator` kısa karşılaştırması

| Özellik | `Comparable<T>` | `Comparator<T>` |
|---|---|---|
| Metot | `compareTo(T other)` | `compare(T a, T b)` |
| Nerede tanımlanır? | Karşılaştırılan sınıfın içinde | Genellikle sınıfın dışında |
| Amaç | Doğal/varsayılan sıra | Belirli business sırası |
| Kaç sıra? | Genellikle bir | İhtiyaç kadar |
| Kullanım | `sorted()` | `sorted(comparator)` |
| Sınıf değişmeli mi? | Evet | Hayır |

## 12. `sorted`, `min` ve `max` ile kullanım

Aynı Comparator farklı Stream işlemlerine gönderilebilir:

```java
Comparator<Product> byPrice =
        Comparator.comparingInt(Product::getPrice);

List<Product> sorted = products.stream()
        .sorted(byPrice)
        .toList();

Optional<Product> cheapest = products.stream()
        .min(byPrice);

Optional<Product> mostExpensive = products.stream()
        .max(byPrice);
```

Comparator yalnızca karşılaştırma davranışını taşır:

```text
sorted → bütün elemanları sıraya koyar
min    → bu kurala göre en küçük elemanı bulur
max    → bu kurala göre en büyük elemanı bulur
```

## 13. Yaygın hatalar

### Çıkarma yaparak karşılaştırma

Şu kod küçük sayılarda çalışıyor gibi görünse de taşma riski taşır:

```java
(first, second) -> first.getPrice() - second.getPrice()
```

Bunun yerine:

```java
Comparator.comparingInt(Product::getPrice)
```

veya:

```java
(first, second) -> Integer.compare(
        first.getPrice(),
        second.getPrice()
)
```

kullanılır.

### Comparator sonucunu boolean sanmak

Comparator `true/false` döndürmez:

```text
Predicate  → boolean
Comparator → negatif, 0 veya pozitif int
```

### `null` değerleri unutmamak

Gerçek veride karşılaştırılan alan `null` olabiliyorsa politika açıkça
belirlenmelidir:

```java
Comparator<Product> byNullableName =
        Comparator.comparing(
                Product::getName,
                Comparator.nullsLast(Comparator.naturalOrder())
        );
```

Bu örnekte adı `null` olan ürünler listenin sonuna gider.

## 14. Bu projedeki örnekler

- `NaturalOrderDemo`: Integer ve String'in doğal sırası
- `ProductComparatorDemo`: aynı Product için ada ve fiyata göre sıralama
- `PersonComparableDemo`: `Comparable<Person>` ve alternatif yaş Comparator'ı
- `MultipleComparatorDemo`: bir sınıf için birçok Comparator ve
  `thenComparing()`

Kaynak paket:

```text
src/main/java/org/practice/javacore/comparator
```

## Akılda kalacak kısa formül

```text
Nesnenin tek ve doğal bir sırası var
→ Comparable<T> implement et
→ compareTo() yaz

Business ihtiyacına göre farklı sıralamalar var
→ Comparator<T> oluştur
→ comparing(), reversed(), thenComparing() kullan

Integer ve String
→ Comparable'ı zaten implement eder
→ doğal sıralama için ekstra kod gerekmez
```

## 15. Klasik dersteki yazım ile modern yazımın karşılaştırması

Derste hatırlanan “POJO bir interface'i implement ediyordu” yapısı iki farklı
biçimde görülmüş olabilir.

### A. Product sınıfı `Comparable<Product>` implement eder

```java
public class Product implements Comparable<Product> {
    private String name;
    private int price;

    @Override
    public int compareTo(Product other) {
        return Integer.compare(this.price, other.price);
    }
}
```

Burada fiyat sırası Product'ın doğal sırasıdır:

```java
Collections.sort(products); // Comparator vermek gerekmez
```

### B. Ayrı bir sınıf `Comparator<Product>` implement eder

Product yalnızca POJO olarak kalır:

```java
public class Product {
    private String name;
    private int price;
}
```

Karşılaştırma ayrı sınıftadır:

```java
public class ProductPriceComparator
        implements Comparator<Product> {

    @Override
    public int compare(Product first, Product second) {
        return Integer.compare(
                first.getPrice(),
                second.getPrice()
        );
    }
}
```

Kullanımı:

```java
products.sort(new ProductPriceComparator());
```

Bu yaklaşımda Product sınıfını değiştirmeden farklı Comparator sınıfları
yazılabilir:

```text
ProductPriceComparator
ProductNameComparator
ProductStockComparator
```

### C. Modern factory/lambda yaklaşımı

Modern Java'da ayrı Comparator sınıfının davranışı tek ifadeyle oluşturulabilir:

```java
Comparator<Product> byPrice =
        Comparator.comparingInt(Product::getPrice);

Comparator<Product> byName =
        Comparator.comparing(Product::getName);
```

Burada Product:

- `Comparable` implement etmek zorunda değildir.
- `Comparator` implement etmek zorunda değildir.
- Yalnızca verisini ve normal domain davranışını taşır.

Klasik ve modern karşılık:

```java
// Klasik:
Comparator<Product> byPrice = new ProductPriceComparator();

// Modern:
Comparator<Product> byPrice =
        Comparator.comparingInt(Product::getPrice);
```

İki ifade aynı business davranışını temsil eder:

> İki Product nesnesini fiyatlarına göre karşılaştır.

Bu üç yaklaşımın çalışan örnekleri:

```text
src/main/java/org/practice/javacore/comparator/styles
```

