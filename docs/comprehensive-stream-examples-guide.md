# Person ve Product ile Kapsamlı Stream Örnekleri

Bu rehber, daha önce ayrı paketlerde işlenen Stream operation'larının gerçek bir
domain modeli üzerinde nasıl birlikte kullanıldığını anlatır.

İki bağımsız örnek vardır:

```text
Person  → çalışan rehberi ve insan kaynakları raporları
Product → katalog, stok, kampanya ve fiyat raporları
```

Amaç operation'ları ezberlemek değil, bir business ihtiyacını Stream pipeline'ına
dönüştürmektir.

## 1. Neden POJO ile Stream çalışıyoruz?

Basit Stream örneklerinde çoğunlukla doğrudan sayılar veya String değerleri
kullanılır:

```java
List<Integer> numbers = List.of(1, 2, 3);
List<String> cities = List.of("Amsterdam", "Paris");
```

Gerçek uygulamalarda ise koleksiyonların içinde genellikle domain nesneleri
bulunur:

```java
List<Person> people;
List<Product> products;
```

Her nesne birden fazla business bilgisi taşır:

```text
Person  → isim, yaş, şehir, aktiflik, maaş, yetenekler
Product → isim, kategori, fiyat, stok, aktiflik, etiketler
```

Stream operation'ları bu nesneleri:

- İş kurallarına göre seçmek,
- Ekran değerlerine dönüştürmek,
- İç listelerini açmak,
- Sıralamak ve sayfalamak,
- Saymak ve toplamak,
- Liste, Set veya Map raporlarında biriktirmek

için kullanılır.

## 2. Dosya yapısı

### Person örneği

```text
src/main/java/org/practice/javacore/streams/comprehensive/person/
├── Person.java
└── PersonStreamExamples.java
```

### Product örneği

```text
src/main/java/org/practice/javacore/streams/comprehensive/product/
├── Product.java
└── ProductStreamExamples.java
```

İki paket birbirinden bağımsızdır. Person örneğini anlamak için Product örneğini
çalıştırmak veya okumak gerekmez.

## 3. Person modeli

Person aşağıdaki bilgileri taşır:

```java
Person(
    long id,
    String name,
    int age,
    String city,
    boolean active,
    double monthlySalary,
    List<String> skills
)
```

`skills` alanının liste olması özellikle önemlidir:

```text
Alice  → [Java, SQL, Docker]
Mehmet → [Java, Spring, AWS]
```

Bu iç listeler `flatMap()` çalışmak için kullanılır.

## 4. Product modeli

Product aşağıdaki bilgileri taşır:

```java
Product(
    long id,
    String name,
    String category,
    double price,
    int stock,
    boolean active,
    List<String> tags
)
```

`tags` alanı her ürünün kendi etiket listesidir:

```text
Laptop → [computer, portable, premium]
Mouse  → [computer, wireless]
```

Arama servisi için bütün etiketler `flatMap()` ile tek akışta birleştirilebilir.

## 5. Stream pipeline nasıl okunmalı?

Bir pipeline yukarıdan aşağıya business cümlesi gibi okunmalıdır:

```java
List<Product> secondPage = products.stream()
        .filter(Product::isActive)
        .sorted(Comparator.comparing(Product::getName))
        .skip(3)
        .limit(3)
        .toList();
```

Türkçe karşılığı:

```text
Ürünlerden Stream oluştur.
        ↓
Yalnızca aktif ürünleri bırak.
        ↓
İsme göre sırala.
        ↓
İlk sayfanın üç ürününü atla.
        ↓
Sonraki üç ürünü al.
        ↓
Sonucu listeye dönüştür.
```

Operation sırası rastgele değildir. Örneğin `limit(3)` filtre öncesinde
çalıştırılırsa üç aktif ürün değil, kaynak listenin ilk üç kaydından aktif
olanlar elde edilir.

## 6. `filter()` — Domain nesnesine business kuralı uygulama

Person örneği:

```java
List<Person> participants = people.stream()
        .filter(person -> person.isActive()
                && person.getCity().equals("Amsterdam"))
        .toList();
```

Business anlamı:

> Amsterdam ofisindeki aktif çalışanları eğitime dahil et.

Product örneği:

```java
List<Product> sellableProducts = products.stream()
        .filter(product -> product.isActive()
                && product.getStock() > 0)
        .toList();
```

Business anlamı:

> Yalnızca yayında ve stokta bulunan ürünleri müşteriye göster.

`filter()` nesneyi değiştirmez. Predicate sonucu `true` olan nesneyi akışta
bırakır.

## 7. `map()` — POJO'yu başka bir gösterime dönüştürme

Person:

```java
List<String> labels = people.stream()
        .map(person -> person.getName()
                + " - "
                + person.getCity())
        .toList();
```

Tip akışı:

```text
Stream<Person> → Stream<String> → List<String>
```

Product:

```java
List<String> labels = products.stream()
        .map(product -> product.getName()
                + " - €"
                + product.getPrice())
        .toList();
```

Burada tam POJO, ekranın beklediği etikete dönüştürülür. `map()` “nesneyi getir”
değil, “nesneyi başka bir değere dönüştür” davranışıdır.

## 8. `flatMap()` — POJO içindeki listeleri açma

Person yetenekleri:

```java
Set<String> skills = people.stream()
        .flatMap(person -> person.getSkills().stream())
        .collect(Collectors.toSet());
```

Akış:

```text
Stream<Person>
      ↓
Her Person'dan Stream<String> yetenek akışı
      ↓
Tek Stream<String>
      ↓
Set<String>
```

Product etiketlerinde de aynı yapı vardır:

```java
Set<String> tags = products.stream()
        .flatMap(product -> product.getTags().stream())
        .collect(Collectors.toSet());
```

`map()` kullanılsaydı iç içe yapı oluşurdu:

```text
Stream<List<String>>
```

`flatMap()` sonucu:

```text
Stream<String>
```

olur.

## 9. `sorted()` ve Comparator — Business sırası oluşturma

Person maaş raporu:

```java
Comparator<Person> salaryOrder =
        Comparator.comparingDouble(Person::getMonthlySalary)
                .reversed()
                .thenComparing(Person::getName);
```

Kurallar:

```text
Önce yüksek maaş
Maaş aynıysa isim
```

Product kataloğu:

```java
Comparator<Product> catalogOrder =
        Comparator.comparing(Product::getCategory)
                .thenComparing(
                        Comparator.comparingDouble(Product::getPrice)
                                .reversed()
                )
                .thenComparing(Product::getName);
```

Kurallar:

```text
Önce kategori
Kategori aynıysa yüksek fiyat
Fiyat da aynıysa isim
```

Comparator bir nesneyi dönüştürmez; iki nesnenin hangi sırada duracağını
belirleyen davranışı taşır.

## 10. `skip()` ve `limit()` — Sayfalama

Sayfa boyutu üç ve sayfa numarası bir ise:

```text
skip(3)  → ilk sayfayı atla
limit(3) → ikinci sayfa için üç kayıt al
```

Örnek:

```java
List<Person> secondPage = people.stream()
        .sorted(Comparator.comparing(Person::getName))
        .skip(3)
        .limit(3)
        .toList();
```

Sayfalama öncesinde sabit bir sıralama kurulmalıdır. Aksi halde aynı kayıt farklı
çalıştırmalarda farklı sayfalara düşebilir.

## 11. `count()` — Pipeline sonucundaki eleman sayısı

```java
long activeCount = people.stream()
        .filter(Person::isActive)
        .count();
```

Business anlamı:

> Sistemdeki aktif çalışanların adedini göster.

```java
long outOfStockCount = products.stream()
        .filter(product -> product.isActive()
                && product.getStock() == 0)
        .count();
```

Business anlamı:

> Yayında olduğu halde tükenen ürün çeşidi sayısını göster.

`count()` terminal operation'dır ve `long` döndürür.

## 12. Sayısal Stream ve `reduce()`

Person maaşları:

```java
double totalSalary = people.stream()
        .filter(Person::isActive)
        .mapToDouble(Person::getMonthlySalary)
        .sum();
```

Product stok değeri:

```java
double inventoryValue = products.stream()
        .filter(Product::isActive)
        .mapToDouble(product ->
                product.getPrice() * product.getStock())
        .sum();
```

`mapToDouble()`:

```text
Stream<Product> → DoubleStream
```

dönüşümü yapar. Bundan sonra `sum()`, `average()` ve `summaryStatistics()` gibi
hazır sayısal terminal operation'lar kullanılabilir.

`reduce()` karşılığı:

```java
double total = products.stream()
        .map(product ->
                product.getPrice() * product.getStock())
        .reduce(0.0, Double::sum);
```

Burada `0.0` toplamanın identity değeridir.

## 13. `collect()` ile List, Set ve Map raporları

Şehir başına çalışan listesi:

```java
Map<String, List<Person>> peopleByCity = people.stream()
        .collect(Collectors.groupingBy(Person::getCity));
```

Kategori başına ürün listesi:

```java
Map<String, List<Product>> productsByCategory = products.stream()
        .collect(Collectors.groupingBy(Product::getCategory));
```

ID üzerinden ürün bulma haritası:

```java
Map<Long, Product> productById = products.stream()
        .collect(Collectors.toMap(
                Product::getId,
                Function.identity()
        ));
```

`Function.identity()` Product nesnesinin kendisini Map değeri olarak kullanır:

```text
ID      → Product
101     → Laptop
102     → Mouse
```

## 14. `partitioningBy()` — İki boolean grubu

```java
Map<Boolean, List<Person>> ageGroups = people.stream()
        .collect(Collectors.partitioningBy(
                person -> person.getAge() >= 30
        ));
```

Sonuç:

```text
true  → 30 yaş ve üzeri
false → 30 yaş altı
```

Product örneğinde:

```java
Map<Boolean, List<Product>> activeGroups = products.stream()
        .collect(Collectors.partitioningBy(Product::isActive));
```

`partitioningBy()` sonucu her zaman boolean anahtarlı iki business bölümünü
temsil eder.

## 15. `collectingAndThen()` — Topla, sonra son işlem yap

```java
String report = people.stream()
        .filter(Person::isActive)
        .map(Person::getName)
        .collect(Collectors.collectingAndThen(
                Collectors.joining(", "),
                names -> "Active employees: " + names
        ));
```

Akış:

```text
İsimleri birleştir
→ "Alice, Mehmet, Eva"

Son işlemi uygula
→ "Active employees: Alice, Mehmet, Eva"
```

`collect()` terminal operation'dır. `collectingAndThen()` ise collect metoduna
verilen iki aşamalı Collector tarifini oluşturur.

## 16. `min`, `max`, `minBy` ve `maxBy`

```java
Comparator<Product> byPrice =
        Comparator.comparingDouble(Product::getPrice);

Optional<Product> cheapest =
        products.stream().min(byPrice);

Optional<Product> mostExpensive =
        products.stream().max(byPrice);
```

`min()` ve `max()` doğrudan Stream terminal operation'larıdır.

Collector tabanlı karşılık:

```java
Optional<Product> cheapest = products.stream()
        .collect(Collectors.minBy(byPrice));
```

Tek minimum veya maksimum için `min()`/`max()` daha sadedir.
`groupingBy()` gibi Collector birleşimlerinde `minBy()`/`maxBy()` daha
kullanışlıdır.

## 17. Intermediate ve terminal operation ayrımı

Bu örneklerdeki temel ayrım:

| Intermediate operation | Terminal operation |
|---|---|
| `filter()` | `toList()` |
| `map()` | `forEach()` |
| `flatMap()` | `count()` |
| `sorted()` | `sum()` |
| `skip()` | `reduce()` |
| `limit()` | `collect()` |
| `mapToDouble()` | `min()` / `max()` |

Intermediate operation'lar pipeline'ı hazırlar. Gerçek traversal terminal
operation çağrıldığında başlar.

## 18. Aynı Stream neden tekrar kullanılmıyor?

Bir terminal operation Stream'i tüketir:

```java
Stream<Person> stream = people.stream();

long count = stream.count();
stream.forEach(System.out::println); // IllegalStateException
```

Aynı kaynak üzerinde farklı raporlar gerektiğinde yeni Stream oluşturulur:

```java
long count = people.stream().count();
double average = people.stream()
        .mapToInt(Person::getAge)
        .average()
        .orElse(0);
```

Kaynak `List` tekrar kullanılabilir; tüketilen şey oluşturulan Stream nesnesidir.

## 19. Önerilen çalışma sırası

Her iki demo sınıfını birden okumak yerine şu sırayla ilerlemek daha faydalıdır:

```text
1. Person veya Product örneklerinden birini seç.
2. createPeople() / createProducts() verisini incele.
3. Yalnızca filter metodunu çalış.
4. Sonra map ve flatMap'e geç.
5. sorted, skip ve limit ile sayfalama yap.
6. count ve sayısal işlemleri çalış.
7. En son groupingBy, partitioningBy ve collectingAndThen'e geç.
8. Aynı business sorusunu kendin farklı veriyle yeniden yaz.
```

Her metotta önce şu soruyu cevapla:

> İşletme bu veriden hangi sonucu istiyor?

Ardından pipeline'ı Türkçe cümlelere ayır:

```text
Önce kimi seçiyorum?
Neye dönüştürüyorum?
İç listeyi açmam gerekiyor mu?
Hangi sırada gösteriyorum?
Kaç kayıt kullanıyorum?
Sonuç List mi, Set mi, Map mi, sayı mı?
```

Bu soruların cevapları kullanılacak Stream operation'larını doğal olarak ortaya
çıkarır.

