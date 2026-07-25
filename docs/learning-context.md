# Java Lambda ve Collections — Öğrenme Bağlamı

Bu dosya, yeni bir Codex oturumunda önceki uzun konuşmayı yeniden kurmak zorunda
kalmadan aynı öğrenme yaklaşımıyla devam edebilmek için tutulur.

## Öğrencinin amacı ve seviyesi

- Proje, Java lambda ifadelerini, hazır functional interface'leri ve collections
  kullanımını adım adım öğrenmek için kullanılıyor.
- Konular yalnızca syntax olarak değil, gerçek bir uygulamadaki business karşılığı
  üzerinden anlaşılmak isteniyor.
- Stream API serisine yeni başlanıyor. `filter()`, `map()` ve `forEach()` daha önce
  functional interface bağlantısını gösterecek kadar ön bilgi olarak anlatıldı;
  bundan sonra konu `org.practice.javacore.streams` altında sistematik işlenecek.
- Kod örneklerinde lambda'nın hangi interface metodunu uyguladığı ve davranışın ne
  zaman gerçekten çalıştığı özellikle açıklanmalı.

## Tercih edilen anlatım biçimi

Yeni bir kavram veya requirement açıklanırken mümkün olduğunda şu sıra izlenmeli:

1. Requirement'ın sade Türkçe karşılığı
2. "Business açısından burada ne yapılıyor?" bölümü
3. Gerçek ekran, servis veya iş akışı örneği
4. İşletmenin/sistemin koyduğu somut kurallar
5. Functional interface'in tip akışı
6. Lambda'nın neyi temsil ettiği
7. `test()`, `apply()` veya `accept()` çağrısının ne zaman çalıştığı
8. Tek değer ve koleksiyon kullanımı arasındaki fark
9. Örnek girdilerin adım adım sonucu
10. Varsa benzer interface'lerle kısa karşılaştırma

Business açıklaması soyut bırakılmamalı. Örneğin yalnızca "kullanıcı adı kontrol
edilir" demek yerine şu tür somut kurallar verilmelidir:

```text
Kullanıcı adı girilmiş olmalı.
Yalnızca boşluklardan oluşmamalı.
En az 5 karakter olmalı.
```

## Yerleşmiş zihinsel modeller

### Lambda ve functional interface

Lambda tek başına hedefsiz kullanılmaz. Tek abstract metodu bulunan functional
interface, lambda'nın parametre ve sonuç sözleşmesini belirler.

```text
Lambda interface referansına atanır
        ↓
Lambda, tek abstract metodun uygulamasını sağlar
        ↓
Abstract metot çağrılınca davranış gerçekten çalışır
```

Referansa atanan şey metodun sonucu değil, daha sonra çalıştırılabilecek
davranıştır.

### Predicate

```text
Predicate<T> = T alır, boolean döndürür
```

```java
Predicate<T> condition = value -> booleanCondition;
boolean result = condition.test(value);
```

- Lambda bir soru/iş kuralı taşır: geçerli mi, aktif mi, çift mi, stokta mı?
- Tek değer için `test()` doğrudan çağrılır.
- `filter(predicate)`, listedeki her eleman için `test()` çağrısını içeride yapar.
- `and()`, `or()` ve `negate()` ile kurallar birleştirilebilir.

### Function

```text
Function<T, R> = T alır, R döndürür
```

```java
Function<T, R> mapper = value -> convertedValue;
R result = mapper.apply(value);
```

- Lambda bir dönüşüm taşır: String'i Integer'a, Person'ı DTO'ya, fiyatı yeni
  fiyata dönüştürme gibi.
- `apply()` çağrılana kadar gerçek sonuç oluşmaz.
- Stream `map(function)`, her eleman için `apply()` çağrısını içeride yapar.
- `andThen()` soldan sağa; `compose()` parantezdeki Function'dan başlayarak
  çalışır.

### Consumer

```text
Consumer<T> = T alır, işlem yapar, sonuç döndürmez
```

```java
Consumer<T> action = value -> doSomething(value);
action.accept(value);
```

- Lambda bir yan etki taşır: yazdırma, loglama, bildirim gönderme, kaydetme veya
  nesne durumunu güncelleme gibi.
- Tek değer için `accept()` çağrılır.
- `forEach(consumer)`, koleksiyondaki her eleman için `accept()` çağırır.
- Consumer `andThen()` zincirinde iki Consumer aynı girdiyi sırayla alır; ilk
  Consumer sonuç üretip ikinciye aktarmaz çünkü dönüş tipi `void`dur.

### Stream bağlantısının kısa özeti

```text
Predicate<T>  → test()   → filter(predicate)
Function<T,R> → apply()  → map(function)
Consumer<T>   → accept() → forEach(consumer)
```

Stream konusu henüz ayrıca işlenmediği için bu bağlantı ön bilgi seviyesinde
tutulmalı; gerekmedikçe ileri Stream ayrıntılarına geçilmemeli.

## İşlenen konular

### Özel functional interface ile lambda

Dosyalar:

- `src/main/java/org/practice/javacore/functional/lambda/custominterface/Hello.java`
- `src/main/java/org/practice/javacore/functional/lambda/custominterface/ArithmeticOperation.java`
- `src/main/java/org/practice/javacore/functional/lambda/custominterface/LambdaExpressionDemo.java`

İşlenen ayrımlar:

- Lambda'yı önce interface referansına atayıp metoda göndermek
- Lambda'yı doğrudan functional interface bekleyen metoda göndermek
- Davranışı ayrı metoda parametre olarak aktarmak
- Lambda'nın anonymous class karşılığı

### Predicate

İşlendi ve birkaç alıştırma kodlandı:

- Kullanıcı adı doğrulama
- Genel sayı kontrol metodu
- Aktif kullanıcıları filtreleme
- Udemy Person yaş/şehir filtresi
- Map içindeki öğrenci yaşlarını filtreleme
- `and`, `or`, `negate`, `Predicate.not`, `Predicate.isEqual`

### Function

İşlendi:

- `Function<T,R>` tipleri
- `apply()`
- Lambda ve method reference
- String büyük harf dönüşümü
- String uzunluğu
- Sayı karesi ve iki katı
- `andThen()` ve `compose()` sırası
- Locale duyarlı `toUpperCase()` ve `Locale.ROOT` farkı

### Consumer

İşlenenler:

- `Consumer<T>` ve `void accept(T)`
- Tek değer için `accept()`
- Liste için `forEach()`
- Consumer'ın sonucu yazdırması ile Function'ın sonucu döndürmesi arasındaki fark
- `andThen()` içinde aynı girdinin iki Consumer'a sırayla verilmesi
- `Map<K,V>` ile Stream `map()` metodunun farklı kavramlar olması

### Method reference

İşlendi:

- Static method reference: `ClassName::staticMethod`
- Belirli nesnenin instance metodu: `instance::method`
- Sınıf üzerinden instance metodu: `ClassName::instanceMethod`
- Constructor reference: `ClassName::new`
- Method/constructor reference'ın uyumlu functional interface hedef tipine ihtiyaç
  duyması

### Streams

Şu anda başlanılan ana konu budur. Yeni örnekler
`src/main/java/org/practice/javacore/streams` altında geliştirilecektir.

## Doküman indeksi

### Predicate

- Requirement'lar: `docs/predicate-requirements.md`
- Örnek çözümler: `docs/predicate-solutions.md`

### Function

- Requirement'lar: `docs/function-requirements.md`
- Örnek çözümler: `docs/function-solutions.md`

### Consumer

- Requirement'lar: `docs/consumer-requirements.md`
- Çözüm dosyası henüz oluşturulmadı.

### Collections

- Hızlı oluşturma ve kullanım rehberi:
  `docs/java-collections-quick-reference.md`

### Toplu functional interface özeti

- Projedeki özel ve hazır functional interface kullanımları:
  `docs/functional-interfaces-cheat-sheet.md`
- Predicate, Function ve Consumer lambda örneklerinin kısa cümleli toplu özeti:
  `docs/predicate-function-consumer-lambda-examples.md`

## Çalışma yöntemi

- Öğrenci requirement'ları önce kendisi çözmek istiyor.
- Kod yazıldığında önce güncel dosya okunmalı ve requirement ile karşılaştırılmalı.
- Hata varsa yalnızca sonuç değil, hatanın sebebi ve çalışma akışı açıklanmalı.
- Kullanıcı açıkça revize etmemizi isterse kod düzenlenip Maven derlemesiyle kontrol
  edilmeli.
- Referans çözümler kullanıcının dosyasının üzerine otomatik uygulanmamalı.
- Yeni bir 10 soruluk konu hazırlanırken requirement ve solution ayrı Markdown
  dosyalarında tutulmalı; her soruda somut business açıklaması bulunmalı.

## Sonraki muhtemel adımlar

1. Stream API temel kavramlarını `streams.basics` altında işlemek
2. `filter`, `map` ve `forEach` bağlantılarını bu kez Stream akışı açısından
   detaylandırmak
3. Intermediate ve terminal operation ayrımına ilerlemek
4. Consumer requirement'larına gerektiğinde geri dönmek ve kullanıcı istediğinde
   `consumer-solutions.md` oluşturmak
