# `Transform` Sınıfının Açıklaması

`Transform` sınıfındaki business ihtiyacı şu:

> Uygulamadaki arkadaş isimlerini değiştirmeden, ekranda büyük harfli olarak göstermek veya büyük harfli yeni bir liste oluşturmak.

Başlangıç listesi:

```text
[Alice, Bob, Charlie, David]
```

Beklenen dönüştürülmüş değerler:

```text
[ALICE, BOB, CHARLIE, DAVID]
```

## 1. Stream ile dönüştürme

```java
friends.stream()
        .map(String::toUpperCase)
        .forEach(System.out::println);
```

Akış şöyledir:

```text
Alice   -> ALICE   -> ekrana yazdır
Bob     -> BOB     -> ekrana yazdır
Charlie -> CHARLIE -> ekrana yazdır
David   -> DAVID   -> ekrana yazdır
```

### `map()` ne yapıyor?

```java
.map(String::toUpperCase)
```

`map()`, Stream’den gelen her ismi yeni bir değere dönüştürür.

Method reference’ın lambda karşılığı:

```java
.map(name -> name.toUpperCase())
```

Tip akışı:

```text
String alır -> String döndürür
```

Bu nedenle `map()` burada bir `Function<String, String>` davranışı alır:

```text
Function<String, String>
         ↓
String apply(String name)
```

Kabaca şu çağrılar gerçekleşir:

```text
apply("Alice")   -> "ALICE"
apply("Bob")     -> "BOB"
apply("Charlie") -> "CHARLIE"
apply("David")   -> "DAVID"
```

`map()` mevcut String nesnelerini değiştirmez. Büyük harfli yeni String değerleri üretir.

### `forEach()` ne yapıyor?

```java
.forEach(System.out::println);
```

`map()` tarafından üretilen her büyük harfli ismi ekrana yazdırır.

Lambda karşılığı:

```java
.forEach(name -> System.out.println(name));
```

Tip akışı:

```text
String alır -> ekrana yazdırır -> sonuç döndürmez
```

Bu nedenle `forEach()` bir `Consumer<String>` davranışı alır.

Önemli nokta: `map()` intermediate operation’dır ve tek başına gerçek dönüşümü başlatmaz. Terminal operation olan `forEach()` çağrıldığında Stream dolaşılmaya ve davranışlar çalışmaya başlar.

Bu kod yeni bir liste üretmiyor; değerleri dönüştürüp doğrudan yazdırıyor.

## 2. Klasik döngüyle dönüştürme

```java
List<String> upperCase = new ArrayList<>();

for (String name : friends) {
    upperCase.add(name.toUpperCase());
}
```

Burada bütün işlemleri biz yönetiyoruz:

```text
1. Boş liste oluştur.
2. Arkadaş isimlerini sırayla dolaş.
3. İsmi büyük harfe dönüştür.
4. Dönüştürülen ismi yeni listeye ekle.
```

Sonuçta:

```text
upperCase = [ALICE, BOB, CHARLIE, DAVID]
```

Bu yaklaşım imperative yaklaşımdır. Çünkü işlemin nasıl yapılacağını adım adım biz yazıyoruz.

Ancak sınıfta `upperCase` listesi daha sonra yazdırılmadığı için sonucu konsolda göremiyoruz.

## 3. `forEach()` içinde dış listeyi değiştirme

```java
List<String> uppercaseNames = new ArrayList<>();

friends.forEach(
        name -> uppercaseNames.add(name.toUpperCase())
);

System.out.println(uppercaseNames);
```

Bu kod da şu sonucu üretir:

```text
[ALICE, BOB, CHARLIE, DAVID]
```

Fakat yorumda `BAD IDEA` denmesinin sebebi, `forEach()` içinde dışarıdaki mutable bir listenin değiştirilmesidir:

```text
forEach dışındaki liste
          ↑
Lambda her çalıştığında bu listeyi değiştiriyor
```

Bu bir side effect’tir. Özellikle parallel Stream kullanılırsa birden fazla eleman aynı `ArrayList` üzerinde eş zamanlı değişiklik yapabilir ve güvenilir olmayan sonuçlar oluşabilir.

Yeni liste üretmek isteniyorsa daha uygun kullanım:

```java
List<String> uppercaseNames = friends.stream()
        .map(String::toUpperCase)
        .toList();
```

Bu kod ihtiyacı daha doğrudan anlatır:

```text
İsimleri al
   ↓
Büyük harfe dönüştür
   ↓
Yeni listeye topla
```

Özetle üç yaklaşım arasındaki fark:

```text
1. map + forEach
   Dönüştürür ve doğrudan yazdırır.

2. Klasik for
   Dönüşümün ve listeye eklemenin bütün adımlarını biz yönetiriz.

3. forEach + dış listeyi değiştirme
   Çalışır, fakat dışarıdaki mutable state'i değiştirdiği için önerilmez.

Tercih edilen liste üretme yöntemi
   map + toList()
```

# 21. Satır Neden `BAD IDEA`?

21. satırdaki lambda bir `Function` değil, yine `Consumer<String>`dır:

```java
friends.forEach(name -> uppercaseNames.add(name.toUpperCase()));
```

Kafa karıştıran nokta, satırın içinde hem dönüşüm hem de listeye ekleme bulunmasıdır.

## Lambda neden Consumer?

Lambda’nın functional interface türünü yalnızca içinde çağrılan metoda bakarak belirlemeyiz. Lambda’nın gönderildiği metodun beklediği tipe bakarız:

```java
friends.forEach(/* burası Consumer<String> bekler */);
```

`forEach()` metodunun sözleşmesi kabaca şöyledir:

```java
void forEach(Consumer<? super String> action)
```

Dolayısıyla buraya gönderilen:

```java
name -> uppercaseNames.add(name.toUpperCase())
```

ifadesinin hedef tipi `Consumer<String>`dır.

Java bunu kabaca şöyle görür:

```java
Consumer<String> action =
        name -> uppercaseNames.add(name.toUpperCase());
```

ve her isim için:

```text
action.accept("Alice")
action.accept("Bob")
action.accept("Charlie")
action.accept("David")
```

çağrılarını yapar.

## Ama `add()` boolean döndürüyor

Evet, kafa karıştıran asıl nokta bu:

```java
uppercaseNames.add(...)
```

`List.add()` metodunun dönüş tipi `boolean`dır:

```java
boolean add(String element)
```

Başarılı eklemede genellikle `true` döner. Buna rağmen lambda Consumer olabilir:

```java
Consumer<String> consumer =
        name -> uppercaseNames.add(name.toUpperCase());
```

Çünkü bu bir statement expression’dır. Java, lambda `void` döndüren bir hedef tipe atanmışsa bu ifadenin ürettiği değeri yok sayabilir.

Kavramsal olarak şöyle çalışır:

```java
Consumer<String> consumer = name -> {
    uppercaseNames.add(name.toUpperCase());
    // add() true/false üretiyor ama bu değer kullanılmıyor.
    // accept() metodunun dönüş tipi void.
};
```

Yani:

```text
name.toUpperCase() → String üretir
uppercaseNames.add(...) → boolean üretir
Consumer.accept(...) → hiçbir değer döndürmez
```

İçeride bir metodun değer üretmesi, bütün lambda’nın mutlaka `Function` olduğu anlamına gelmez. Üretilen `boolean` sonuç burada çöpe atılır.

Aynı ifade farklı hedef tip verilirse Function olabilir:

```java
Function<String, Boolean> addUppercase =
        name -> uppercaseNames.add(name.toUpperCase());
```

Bu durumda `add()` metodunun döndürdüğü `boolean`, `Boolean` sonuç olarak dışarı verilir:

```java
Boolean added = addUppercase.apply("Alice");
```

Fakat mevcut kodda hedef tipi `forEach()` belirlediği için lambda `Consumer<String>`dır.

## Neden “BAD IDEA”?

Kod çalışır ve şu listeyi üretir:

```text
[ALICE, BOB, CHARLIE, DAVID]
```

Ancak lambda, kendi dışındaki listeyi değiştiriyor:

```java
List<String> uppercaseNames = new ArrayList<>();
                                  ↑
friends.forEach(name -> uppercaseNames.add(...));
                       dışarıdaki liste değiştiriliyor
```

Buna side effect, yani yan etki denir.

### 1. Dönüşüm amacı yanlış yöntemle anlatılıyor

Business ihtiyacı şudur:

> Her ismi büyük harfli yeni bir isme dönüştür ve sonuçları listeye topla.

Bu doğrudan `map()` ve `toList()` ihtiyacıdır:

```java
List<String> uppercaseNames = friends.stream()
        .map(String::toUpperCase)
        .toList();
```

`forEach()` ise daha çok şunu ifade eder:

> Her isim için sonuç döndürmeyen bir işlem yap.

Örneğin:

```java
friends.forEach(System.out::println);
```

21. satır çalışsa da kodun niyetini doğru araçla ifade etmiyor.

### 2. Dışarıdaki mutable state değiştiriliyor

Lambda yalnızca kendisine gelen `name` değeriyle sonuç üretmiyor. Dışarıdaki `uppercaseNames` listesine bağımlı:

```text
Lambda
  │
  └── dışarıdaki ArrayList'i değiştiriyor
```

Bu durum kodu anlamayı ve test etmeyi zorlaştırır. Lambda’nın sonucunu anlamak için yalnızca lambdaya değil, dışarıdaki listenin önceki ve sonraki durumuna da bakmak gerekir.

### 3. Parallel kullanımda güvenli değildir

Kod daha sonra şu hale getirilirse:

```java
friends.parallelStream()
        .forEach(name -> uppercaseNames.add(name.toUpperCase()));
```

birden fazla thread aynı `ArrayList` üzerinde eş zamanlı olarak `add()` çalıştırabilir.

`ArrayList` thread-safe değildir. Bunun sonucunda:

- Elemanlar kaybolabilir.
- Sıra bozulabilir.
- Liste tutarsız hale gelebilir.
- Beklenmeyen hatalar oluşabilir.

`map(...).toList()` yaklaşımında Stream kendi sonuç toplama mekanizmasını yönetir:

```java
List<String> uppercaseNames = friends.parallelStream()
        .map(String::toUpperCase)
        .toList();
```

## Kısa zihinsel ayrım

```text
Consumer<T>
T alır → işlem yapar → dışarı sonuç döndürmez

Function<T, R>
T alır → yeni R değeri üretip döndürür
```

21. satırda:

```text
String name alınır
      ↓
Büyük harfe çevrilir
      ↓
Dışarıdaki listeye eklenir
      ↓
add() sonucu kullanılmaz
      ↓
Consumer.accept() void olarak tamamlanır
```

Bu nedenle teknik olarak `Consumer<String>`, tasarım açısından ise kötü bir `forEach()` kullanımıdır.

# `map` ve `transform` Fiillerinin Karşılığı

Bu cümlede `map` ve `transform` neredeyse aynı anlamda kullanılıyor:

> The `map` method is useful to map or transform an input collection into a new output collection.

Sade Türkçesi:

> `map` metodu, girdi koleksiyonundaki değerleri dönüştürerek yeni çıktı değerleri üretmek için kullanışlıdır.

İki fiilin nüansı şöyle:

## `transform`

Genel İngilizce bir fiildir:

```text
transform = dönüştürmek, başka bir biçime sokmak
```

Örneğin:

```text
"Alice" -> "ALICE"
Product -> ProductDto
"19.99" -> 19.99
```

Bir değerin biçimi veya tipi değişebilir.

## `map`

Programlamadaki teknik anlamıyla:

```text
map = her girdi elemanını karşılık gelen bir çıktı elemanıyla eşlemek
```

Örneğin:

```text
Alice   -> ALICE
Bob     -> BOB
Charlie -> CHARLIE
David   -> DAVID
```

Burada her giriş elemanının bir çıkış karşılığı vardır:

```text
Girdi elemanı --dönüşüm kuralı--> Çıktı elemanı
```

Bu yüzden `map` için Türkçede bağlama göre şunlar kullanılabilir:

- dönüştürmek
- eşlemek
- bir değeri başka bir değere çevirmek

Ancak yalnızca “eşlemek” yeni öğrenen biri için biraz soyut kalabilir. Bu projedeki en anlaşılır karşılığı:

> Her elemanı alıp yeni bir değere dönüştürmek.

Yazarın iki fiili birlikte kullanmasının nedeni, teknik terimi genel İngilizceyle açıklamaktır:

```text
to map, yani to transform
```

Cümledeki anlam:

> `map` metodunu kullanarak girişteki her elemanı bir çıkış elemanına dönüştürebiliriz.

Küçük ama önemli bir düzeltme: Stream’deki `map()` doğrudan “input collection’ı yeni output collection’a dönüştürmez.” Her Stream elemanını yeni bir elemana dönüştürerek yeni bir Stream üretir:

```java
Stream<String> uppercaseStream = friends.stream()
        .map(String::toUpperCase);
```

Yeni koleksiyon ancak terminal operation ile oluşturulur:

```java
List<String> uppercaseNames = friends.stream()
        .map(String::toUpperCase)
        .toList();
```

Tam akış:

```text
List<String>
    ↓ stream()
Stream<String>
    ↓ map()
Stream<String>
    ↓ toList()
List<String>
```

Dolayısıyla daha teknik ve doğru cümle şöyle olur:

> The `map` method transforms each element of an input stream into a corresponding element in a new stream.

Türkçesi:

> `map` metodu, giriş Stream’indeki her elemanı yeni Stream’deki karşılık gelen bir elemana dönüştürür.

# `map()` ile Girdi ve Çıktı Tipleri

Evet, doğru anlamışsın. İki `map()` kullanımında da her girdi elemanı alınır ve ona karşılık yeni bir çıktı elemanı üretilir. Çıktının tipi, girdinin tipiyle aynı olmak zorunda değildir.

## 1. String alıp String üretmek

```java
.map(String::toUpperCase)
```

Lambda karşılığı:

```java
.map(name -> name.toUpperCase())
```

Tek bir eleman üzerinden düşünürsek:

```text
"Alice" -> "ALICE"
```

Tip akışı:

```text
String -> String
```

Buradaki davranışın functional interface karşılığı:

```java
Function<String, String>
```

Metot sözleşmesi gibi düşünürsek:

```java
String apply(String name)
```

Listenin tamamında:

```text
Girdi:  [Alice, Bob, Charlie, David]
Çıktı:  [ALICE, BOB, CHARLIE, DAVID]
```

Stream tipi de şöyle değişir:

```text
Stream<String>
      ↓ map(String::toUpperCase)
Stream<String>
```

Girdi ve çıktı tipleri aynı fakat değerlerin biçimi değişti.

## 2. String alıp sayı üretmek

```java
.map(String::length)
```

Lambda karşılığı:

```java
.map(name -> name.length())
```

Tek tek sonuçlar:

```text
"Alice"   -> 5
"Bob"     -> 3
"Charlie" -> 7
"David"   -> 5
```

Tip akışı:

```text
String -> Integer
```

Evet, String alıp `int` uzunluk değeri üretir. Ancak `Stream.map()` nesne tipleriyle çalışan generic bir metot olduğu için primitive `int`, autoboxing ile `Integer` nesnesine çevrilir:

```text
name.length() sonucu: int
                       ↓ autoboxing
Stream eleman tipi:   Integer
```

Functional interface karşılığı:

```java
Function<String, Integer>
```

Metot sözleşmesi gibi düşünürsek:

```java
Integer apply(String name)
```

Listenin tamamında:

```text
Girdi:  [Alice, Bob, Charlie, David]
Çıktı:  [5, 3, 7, 5]
```

Stream tipi değişir:

```text
Stream<String>
      ↓ map(String::length)
Stream<Integer>
```

Bu örnek `map()` için önemli bir noktayı gösteriyor:

```text
map() yalnızca değerin görünümünü değiştirmez;
elemanın tipini de değiştirebilir.
```

Genel sözleşmesi:

```java
<R> Stream<R> map(Function<? super T, ? extends R> mapper)
```

Sade hali:

```text
Stream<T>
   ↓ Function<T, R>
Stream<R>
```

Bu iki örneğe uygularsak:

```text
Function<String, String>
"Alice" -> "ALICE"

Function<String, Integer>
"Alice" -> 5
```

## `mapToInt()` farkı

Uzunlukları daha sonra toplamak, ortalamasını almak veya en büyüğünü bulmak
istiyorsan şu da kullanılabilir:

```java
friends.stream()
        .mapToInt(String::length);
```

Bu durumda sonuç:

```text
IntStream
```

olur; `Stream<Integer>` olmaz.

```text
map(String::length)      -> Stream<Integer>
mapToInt(String::length) -> IntStream
```

`mapToInt()` primitive `int` değerleriyle çalıştığı için boxing maliyetini önler ve sayısal işlemleri doğrudan sunar:

```java
int totalLength = friends.stream()
        .mapToInt(String::length)
        .sum();
```

Özet:

```text
map(String::toUpperCase)
String alır
String üretir
Stream<String> -> Stream<String>

map(String::length)
String alır
int hesaplar
Integer olarak taşır
Stream<String> -> Stream<Integer>

mapToInt(String::length)
String alır
primitive int üretir
Stream<String> -> IntStream
```

# Her `map()` Gündelik Anlamda Bir “Dönüştürme” midir?

Evet, günlük dilde “dönüştürmek” deyince genellikle bir şeyi değiştirip başka biçime sokmak anlıyoruz:

```text
alice -> ALICE
```

Bu nedenle şu örnek doğrudan dönüşüm gibi görünüyor:

```java
.map(name -> name.toUpperCase())
```

Çünkü:

```text
Küçük harfli String -> Büyük harfli String
```

Ama önemli nokta şu: `map()` açısından dönüşümün anlamı daha geniştir.

> Gelen elemandan bir çıktı değeri üretmek.

Yani `map()`, gelen nesneyi mutlaka fiziksel olarak değiştirmek zorunda değildir. Gelen değeri kullanarak onun karşılığı olan başka bir değer de üretebilir.

## `toUpperCase()` örneği

```java
.map(name -> name.toUpperCase())
```

Akış:

```text
"Alice" alınır
      ↓
Büyük harfli karşılığı üretilir
      ↓
"ALICE"
```

Burada:

```text
Girdi tipi:  String
Çıktı tipi:  String
```

Değerin biçimi değişti:

```text
"Alice" -> "ALICE"
```

Buna rahatlıkla “dönüştürme” diyebiliriz.

## `length()` örneği

```java
.map(name -> name.length())
```

Akış:

```text
"Alice" alınır
      ↓
Karakter sayısı hesaplanır
      ↓
5 üretilir
```

Haklısın: Burada `"Alice"` metni değiştirilip başka biçimde bir metne çevrilmiyor. İsmin bir özelliği hesaplanıyor ve çıktı olarak o özellik üretiliyor.

```text
"Alice" -> 5
```

Burada:

```text
Girdi tipi:  String
Çıktı tipi:  Integer
```

Programlama terminolojisinde bu da geniş anlamıyla bir transformation’dır. Çünkü Stream’e giren eleman ile Stream’den çıkan eleman aynı değildir:

```text
Giren eleman:  "Alice"
Çıkan eleman:  5
```

Ama daha hassas isimlendirmek istersek buna projection denebilir.

## Projection nedir?

Projection, bir nesnenin tamamından yalnızca belirli bir bilgisini çıkarıp sonuç olarak üretmektir.

Örneğin:

```text
"Alice" -> uzunluğu -> 5
Person  -> adı      -> "Alice"
Product -> fiyatı   -> 1200.0
Order   -> ID'si    -> 1001
```

Java örnekleri:

```java
.map(String::length)
.map(Person::getName)
.map(Product::getPrice)
.map(Order::getId)
```

Bunlarda nesnenin tamamını başka biçime sokmaktan çok, içinden veya üzerinden bir bilgi çıkarıyoruz.

Bu yüzden iki kullanım arasında şöyle bir nüans var:

```text
name.toUpperCase()
→ Değerin biçimini dönüştürüyor.

name.length()
→ Değerden yeni bir bilgi hesaplıyor/çıkarıyor.
```

Ama ikisi de `map()` açısından aynı sözleşmeye uyuyor:

```text
Bir T al
   ↓
O T'den bir R üret
   ↓
R'yi yeni Stream'in elemanı yap
```

## String gerçekten `5`e mi dönüşüyor?

Hayır. `"Alice"` String nesnesi değiştirilip `5` haline gelmiyor.

Daha doğru ifade:

> `"Alice"` girdisine karşılık çıktı olarak `5` üretiliyor.

```text
"Alice" değişmeden kalır.

Function:
"Alice" -> 5

Yeni Stream:
[5, 3, 7, 5]
```

Kaynak liste hâlâ aynıdır:

```text
[Alice, Bob, Charlie, David]
```

`map()` sonucundaki Stream ise Integer değerleri taşır:

```text
[5, 3, 7, 5]
```

## “Map” kelimesi burada neden daha uygun?

Matematikte ve programlamada “map”, bir kümedeki her elemana başka bir kümede bir karşılık atamak anlamına gelir:

```text
Alice   ↦ 5
Bob     ↦ 3
Charlie ↦ 7
David   ↦ 5
```

Bu nedenle `map(String::length)` için “çevirmek” yerine şu ifade daha anlaşılır olabilir:

> Her ismi, o ismin uzunluk değerine eşleştir.

Ya da daha sade:

> Her ismi al ve o isimden uzunluk sonucunu üret.

## En net ayrım

```text
map(name -> name.toUpperCase())

Girdi:  "Alice"
İşlem:  Yazım biçimini değiştir
Çıktı:  "ALICE"

Bu, biçim dönüşümüdür.
```

```text
map(name -> name.length())

Girdi:  "Alice"
İşlem:  Karakter sayısını hesapla
Çıktı:  5

Bu, bilgi çıkarma/hesaplama işlemidir.
Teknik olarak yine geniş anlamda transformation,
daha özel adıyla projection'dır.
```

Kısacası:

> Her `map()` işlemi girdiden bir çıktı üretir; fakat her `map()` gündelik anlamda “bir şeyi değiştirip başka şekle sokmak” değildir.

Bazıları gerçekten biçim dönüştürür:

```java
String::toUpperCase
```

Bazıları bir özellik çıkarır:

```java
String::length
```

Bazıları tamamen farklı bir nesne üretir:

```java
person -> new PersonDto(person.getName())
```

Hepsi `map()`tir çünkü her girdi elemanına karşılık yeni bir çıktı elemanı üretir.
