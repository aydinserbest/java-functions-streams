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

# `map()` ve `filter()` Metotlarının Stream Döndürmesi

Burada daha önce konuştuğumuz birkaç önemli konu tek örnekte birleşiyor. Aklındaki ana konu büyük ihtimalle şu:

> `map()` ve `filter()` işlemi hemen tamamlayıp bir `List` döndürmez; yeni bir `Stream` döndürür. Bu Stream, terminal operation çağrılana kadar çalıştırılmayı bekleyen bir işlem pipeline’ıdır.

## 1. `stream()` ile başlayan tip akışı

Başlangıçta elinde bir koleksiyon var:

```java
List<String> friends =
        List.of("Alice", "Bob", "Charlie", "David");
```

Sonra:

```java
friends.stream()
```

çağrısı yapılıyor.

Tip akışı:

```text
List<String>
    ↓ stream()
Stream<String>
```

Liste hâlâ duruyor. `stream()` listeyi Stream’e “çevirip yok etmez”. Listedeki elemanları işleyebilecek bir Stream pipeline’ı başlatır.

## 2. `map()` yeni bir Stream döndürür

```java
Stream<String> stringStream = friends.stream()
        .map(String::toUpperCase);
```

Tip akışı:

```text
List<String>
    ↓ stream()
Stream<String>
    ↓ map(String::toUpperCase)
Stream<String>
```

`map()` her String girdisinden yeni bir String çıktısı üretecek davranışı taşıyor:

```text
"Alice"   -> "ALICE"
"Bob"     -> "BOB"
"Charlie" -> "CHARLIE"
"David"   -> "DAVID"
```

Ancak bu satır tamamlandığında isimler henüz büyük harfe çevrilmiş olmak zorunda değildir:

```java
Stream<String> stringStream = friends.stream()
        .map(String::toUpperCase);
```

Çünkü `map()` bir intermediate operation’dır. Lazy çalışır.

`stringStream` değişkeninin tuttuğu şey hazır sonuç listesi değil, kabaca şu işlem tarifidir:

```text
Friends listesinden elemanları al
        ↓
Her elemanı büyük harfe dönüştür
        ↓
Sonraki işlemi bekle
```

Gerçek dolaşma şu terminal operation geldiğinde başlar:

```java
stringStream.forEach(System.out::println);
```

Akış:

```text
"Alice" alınır
    ↓ map()
"ALICE" üretilir
    ↓ forEach()
ALICE yazdırılır
```

Ardından Bob için aynı zincir çalışır:

```text
"Bob" alınır
    ↓ map()
"BOB" üretilir
    ↓ forEach()
BOB yazdırılır
```

Burada önce bütün isimlerin dönüştürülüp sonra topluca yazdırılması şart değildir. Stream pipeline çoğunlukla elemanları zincirden tek tek geçirir.

## 3. `map()` koleksiyon döndürmez

Aklındaki önemli ayrımlardan biri muhtemelen buydu:

```java
Stream<String> stringStream = friends.stream()
        .map(String::toUpperCase);
```

`map()` sonucunda şu oluşmaz:

```java
List<String>
```

Şu oluşur:

```java
Stream<String>
```

Yeni bir liste istiyorsan terminal operation gerekir:

```java
List<String> uppercaseNames = friends.stream()
        .map(String::toUpperCase)
        .toList();
```

Tip akışı:

```text
List<String>
    ↓ stream()
Stream<String>
    ↓ map()
Stream<String>
    ↓ toList()
List<String>
```

Dolayısıyla:

```java
map(...)
```

her elemanın yeni karşılığını üreten bir Stream döndürür.

```java
toList()
```

ise bu değerleri gerçekten bir sonuç koleksiyonunda toplar.

## 4. `filter()` da Stream döndürür

İkinci örnek:

```java
Stream<String> lengthString = friends.stream()
        .filter(name -> name.length() > 3);
```

Tip akışı:

```text
List<String>
    ↓ stream()
Stream<String>
    ↓ filter(...)
Stream<String>
```

`filter()` da intermediate operation’dır ve yeni bir Stream döndürür.

Fakat `map()` ile yaptığı iş aynı değildir.

### `map()`

Her elemandan yeni bir çıktı üretir:

```text
Alice   -> ALICE
Bob     -> BOB
Charlie -> CHARLIE
David   -> DAVID
```

### `filter()`

Elemanı dönüştürmez. Yalnızca Stream’de kalıp kalmayacağına karar verir:

```text
Alice   -> uzunluk 5 -> true  -> Stream'de kalır
Bob     -> uzunluk 3 -> false -> elenir
Charlie -> uzunluk 7 -> true  -> Stream'de kalır
David   -> uzunluk 5 -> true  -> Stream'de kalır
```

Sonuç:

```text
Alice
Charlie
David
```

Tipin değişmemesinin sebebi bu:

```text
filter öncesi: Stream<String>
filter sonrası: Stream<String>
```

Çünkü `"Alice"` başka bir değere dönüşmedi; ya olduğu gibi kaldı ya da elendi.

## 5. Function ve Predicate bağlantısı

Daha önce konuştuğumuz functional interface bağlantısı burada çok net görülebilir.

### `map()` Function alır

```java
.map(String::toUpperCase)
```

Lambda karşılığı:

```java
.map(name -> name.toUpperCase())
```

Davranış:

```text
String alır -> String üretir
```

Yani:

```java
Function<String, String>
```

Kabaca:

```java
String result = function.apply(name);
```

### `filter()` Predicate alır

```java
.filter(name -> name.length() > 3)
```

Davranış:

```text
String alır -> boolean üretir
```

Yani:

```java
Predicate<String>
```

Kabaca:

```java
boolean keep = predicate.test(name);
```

`true` ise eleman kalır, `false` ise elenir.

### `forEach()` Consumer alır

```java
.forEach(System.out::println)
```

Lambda karşılığı:

```java
.forEach(name -> System.out.println(name))
```

Davranış:

```text
String alır -> yazdırır -> değer döndürmez
```

Yani:

```java
Consumer<String>
```

Kabaca:

```java
consumer.accept(name);
```

Tam bağlantı:

```text
map()     -> Function  -> apply()  -> yeni değer üretir
filter()  -> Predicate -> test()   -> true/false kararı verir
forEach() -> Consumer  -> accept() -> işlem yapar
```

## 6. Intermediate ve terminal operation ayrımı

Senin kodunda:

```java
friends.stream()
        .map(String::toUpperCase);
```

ve:

```java
friends.stream()
        .filter(name -> name.length() > 3);
```

şunlar intermediate operation’dır:

```text
map()
filter()
```

Özellikleri:

- Yeni Stream döndürürler.
- Pipeline’ın devam etmesine izin verirler.
- Genellikle lazy çalışırlar.
- Tek başlarına sonucu tüketmezler.

Şu ise terminal operation’dır:

```java
forEach()
```

Özellikleri:

- Stream’i dolaşmaya başlatır.
- Elemanları tüketir.
- Pipeline’ı tamamlar.
- Bu kullanımda `void` döndürür.

## 7. Stream tek kullanımlıktır

Şu kullanım geçerlidir:

```java
Stream<String> stringStream = friends.stream()
        .map(String::toUpperCase);

stringStream.forEach(System.out::println);
```

Fakat aynı Stream’i ikinci kez kullanamazsın:

```java
stringStream.forEach(System.out::println);
stringStream.forEach(System.out::println); // Hata
```

İkinci çağrıda genellikle şu hata oluşur:

```text
IllegalStateException:
stream has already been operated upon or closed
```

Çünkü ilk `forEach()` Stream’i tüketmiştir.

Tekrar çalıştırmak istersen kaynak listeden yeni Stream oluşturmalısın:

```java
friends.stream()
        .map(String::toUpperCase)
        .forEach(System.out::println);

friends.stream()
        .map(String::toUpperCase)
        .forEach(System.out::println);
```

Liste tekrar kullanılabilir; Stream tekrar kullanılamaz:

```text
List   -> tekrar tekrar stream oluşturabilir
Stream -> terminal operation sonrasında tüketilmiş olur
```

## 8. Kaynak liste değişmez

Hem `map()` hem `filter()` sonrasında kaynak liste aynı kalır:

```java
System.out.println(friends);
```

Çıktı:

```text
[Alice, Bob, Charlie, David]
```

`map()`:

```text
"Alice" değerini değiştirip listenin içine geri koymaz.
Ondan "ALICE" çıktısı üretir.
```

`filter()`:

```text
"Bob" değerini kaynak listeden silmez.
Yalnızca oluşturulan Stream akışına dahil etmez.
```

Bu yüzden:

```text
Kaynak liste:
[Alice, Bob, Charlie, David]

map sonucu:
[ALICE, BOB, CHARLIE, DAVID]

filter sonucu:
[Alice, Charlie, David]
```

## 9. İki işlem aynı pipeline’da birleşebilir

`map()` ve `filter()` Stream döndürdüğü için birbirinin arkasına eklenebilir:

```java
friends.stream()
        .filter(name -> name.length() > 3)
        .map(String::toUpperCase)
        .forEach(System.out::println);
```

Akış:

```text
Alice
  ↓ length > 3? true
  ↓ toUpperCase
ALICE
  ↓ println

Bob
  ↓ length > 3? false
  ↓ burada elenir; map ve forEach çalışmaz

Charlie
  ↓ length > 3? true
  ↓ toUpperCase
CHARLIE
  ↓ println

David
  ↓ length > 3? true
  ↓ toUpperCase
DAVID
  ↓ println
```

Çıktı:

```text
ALICE
CHARLIE
DAVID
```

Bu zincirin kurulabilmesinin sebebi hem `filter()` hem `map()` metodunun tekrar Stream döndürmesidir:

```text
Stream<String>
    ↓ filter()
Stream<String>
    ↓ map()
Stream<String>
    ↓ forEach()
void
```

## Kısa özet

Senin hatırladığın konu büyük ihtimalle şu zihinsel model:

```text
stream()
→ Koleksiyondan bir işlem akışı başlatır.

map()
→ Her elemandan yeni bir çıktı üretir.
→ Yeni Stream döndürür.
→ Function kullanır.

filter()
→ Her eleman için kal/elen kararı verir.
→ Yeni Stream döndürür.
→ Predicate kullanır.

forEach()
→ Akışı gerçekten tüketir.
→ Her kalan elemana işlem uygular.
→ Consumer kullanır.
→ void döndürür.
```

En kritik cümle:

> `map()` ve `filter()` hazır sonucu taşıyan koleksiyonlar değil, terminal operation çağrıldığında çalıştırılacak yeni Stream aşamaları döndürür.

# `map()`, Lazy Çalışma ve Terminal Operation İhtiyacı

Evet, üç ifade de aynı Stream çalışma modelinin farklı parçalarını anlatıyor ve birbirini tamamlıyor. Ortak merkezleri terminal operation ihtiyacı.

Fakat küçük bir ayrımı net tutalım:

> Her terminal operation Stream’i çalıştırır; fakat her terminal operation yeni bir koleksiyon oluşturmaz.

Örneğin:

```java
forEach(...)
```

Stream’i çalıştırır ve değerleri yazdırır ama koleksiyon üretmez.

```java
toList()
```

Stream’i çalıştırır ve üretilen değerlerden yeni bir liste oluşturur.

## Üç ifadeyi birleştirelim

### 1. `map()` yeni bir Stream döndürür

```java
Stream<String> uppercaseStream = friends.stream()
        .map(String::toUpperCase);
```

Tip olarak gerçekten şunu alırsın:

```text
Stream<String>
```

Ama bu Stream’in içinde önceden hazırlanmış şu değerlerin bulunduğunu düşünmemeliyiz:

```text
[ALICE, BOB, CHARLIE, DAVID]
```

Daha doğru zihinsel model:

```text
Kaynak: friends
Kural:  Her isim geldiğinde büyük harfe çevir
Durum:  Henüz çalıştırılmadı
```

Yani `map()`:

- Pipeline’a yeni bir dönüşüm aşaması ekler.
- Bu aşamayı temsil eden yeni bir Stream döndürür.
- Henüz kaynak listeyi dolaşmaz.
- Henüz büyük harfli sonuçları toplamaz.

Bu nedenle şu cümleyi daha hassas kurabiliriz:

> `map()`, her elemanın nasıl dönüştürüleceğini tarif eden yeni bir Stream aşaması döndürür.

## 2. `map()` intermediate operation olduğu için lazy çalışır

Şu satırlar çalıştığında:

```java
Stream<String> uppercaseStream = friends.stream()
        .map(String::toUpperCase);
```

pipeline kurulmuştur fakat tüketilmemiştir:

```text
friends
   ↓
stream()
   ↓
toUpperCase uygulanacak
   ↓
terminal operation bekleniyor
```

Bu aşamada kabaca şunlar vardır:

```text
Kaynak hazır:          Evet
Dönüşüm kuralı hazır:  Evet
Liste dolaşıldı mı:    Hayır
"ALICE" üretildi mi:   Hayır
Sonuç toplandı mı:     Hayır
```

`uppercaseStream`, hazır sonuçların bulunduğu bir kutu değil; çalıştırılmayı bekleyen pipeline’dır.

## 3. Terminal operation pipeline’ı çalıştırır

Şimdi şunu çağırırsan:

```java
uppercaseStream.forEach(System.out::println);
```

terminal operation gelir ve gerçek dolaşma başlar:

```text
Alice kaynaktan alınır
        ↓
map: ALICE üretilir
        ↓
forEach: ALICE yazdırılır

Bob kaynaktan alınır
        ↓
map: BOB üretilir
        ↓
forEach: BOB yazdırılır
```

Burada `forEach()` sonucu tüketir ama yeni koleksiyon oluşturmaz.

Çıktı:

```text
ALICE
BOB
CHARLIE
DAVID
```

## Koleksiyon istiyorsak `toList()`

Business ihtiyacı büyük harfli isimleri yeni bir listede saklamaksa:

```java
List<String> uppercaseNames = friends.stream()
        .map(String::toUpperCase)
        .toList();
```

`toList()` terminal operation olduğu için pipeline’ı çalıştırır ve sonuçları bir listede toplar:

```text
Alice
  ↓ map
ALICE
  ↓
toList içinde tutulur

Bob
  ↓ map
BOB
  ↓
toList içinde tutulur
```

Nihai sonuç:

```text
uppercaseNames = [ALICE, BOB, CHARLIE, DAVID]
```

Tam tip ve çalışma akışı:

```text
List<String> friends
        ↓ stream()
Stream<String>
        ↓ map(String::toUpperCase)
Stream<String> — dönüşüm tarifi var, henüz çalışmadı
        ↓ toList()
List<String> — pipeline çalıştı, sonuçlar toplandı
```

## `forEach()` ve `toList()` farkı

İkisi de terminal operation’dır:

```java
friends.stream()
        .map(String::toUpperCase)
        .forEach(System.out::println);
```

```text
Pipeline çalışır.
Büyük harfli değerler üretilir.
Değerler ekrana yazdırılır.
Yeni liste oluşmaz.
Sonuç tipi void'dur.
```

Diğeri:

```java
List<String> uppercaseNames = friends.stream()
        .map(String::toUpperCase)
        .toList();
```

```text
Pipeline çalışır.
Büyük harfli değerler üretilir.
Değerler yeni listede toplanır.
Sonuç tipi List<String>'dir.
```

## En doğru birleşik ifade

Üç açıklamayı tek cümlede şöyle birleştirebiliriz:

> `map()` lazy çalışan bir intermediate operation’dır. Her eleman için uygulanacak dönüşümü pipeline’a ekleyip yeni bir Stream döndürür; elemanlar ancak `forEach()` veya `toList()` gibi bir terminal operation geldiğinde kaynaktan alınır ve gerçekten dönüştürülür. `toList()` seçilirse bu sonuçlar ayrıca yeni bir koleksiyonda toplanır.

Zihinsel model:

```text
stream()
→ Kaynağa bağlı bir Stream oluşturur.

map()
→ Dönüşüm tarifini pipeline'a ekler.
→ Yeni Stream döndürür.
→ Henüz dolaşmayı başlatmaz.

terminal operation
→ Dolaşmayı başlatır.
→ map davranışını elemanlar üzerinde çalıştırır.

forEach()
→ Sonuçları tüketip işlem yapar.

toList()
→ Sonuçları tüketip yeni liste oluşturur.
```

Dolayısıyla vardığın sonuç doğru:

> `map()` tek başına dönüşümü tamamlayan son adım değildir; kurduğu dönüşüm aşamasının gerçekten çalışması için terminal operation gerekir.

Sadece şu ayrımı ekliyoruz:

> Terminal operation çalışmayı başlatır; yeni koleksiyon ise özellikle `toList()` veya `collect(...)` gibi sonuç toplayan bir terminal operation seçilirse oluşur.
