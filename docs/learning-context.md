# Java Lambda ve Collections — Öğrenme Bağlamı

Bu dosya, yeni bir Codex oturumunda önceki uzun konuşmayı yeniden kurmak zorunda
kalmadan aynı öğrenme yaklaşımıyla devam edebilmek için tutulur.

## Öğrencinin amacı ve seviyesi

- Proje, Java lambda ifadelerini, hazır functional interface'leri ve collections
  kullanımını adım adım öğrenmek için kullanılıyor.
- Konular yalnızca syntax olarak değil, gerçek bir uygulamadaki business karşılığı
  üzerinden anlaşılmak isteniyor.
- Stream API serisi `org.practice.streamsandfunctionalinterfaces.streams` altında sistematik olarak
  işleniyor. Temel oluşturma ve dolaşma sonrasında `filter`, `map`, `flatMap`,
  `forEach`, `limit`, `skip`, `reduce`, `collect` ve `count` konularına geçildi.
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

Requirement dosyalarındaki business bölümü, teknik isteğin daha kısa bir tekrarı
olmamalıdır. Öğrenci soruyu bir şirket çalışanı veya uygulamanın kullanıcısı gibi
okuduğunda ihtiyacı zihninde canlandırabilmelidir. Uygun olduğu yerde şu dört
nokta somutlaştırılmalıdır:

1. İşlemi isteyen aktör, ekran veya servis
2. Sisteme gelen gerçek veri ve verinin neden bu biçimde bulunduğu
3. Uygulanacak business kuralları ve kapsam dışında kalacak kayıtlar
4. Kullanılacak sonuç ile yanlış/eksik çözümün doğuracağı iş problemi

Örneğin “SMS ve e-posta lambda'sı yaz” demekle kalınmamalı; müşteri kaydı
tamamlandığında aynı bildirim akışının telefon numarasına kısa mesaj veya kayıtlı
e-posta adresine hoş geldin iletisi gönderebildiği, alıcı adının mesaja nasıl
yansıdığı ve iki davranışın aynı sözleşmeyle neden değiştirilebilir olduğu
açıklanmalıdır.

Requirement belgelerindeki her business açıklaması, teknik terimleri henüz
bilmeyen bir öğrencinin de zihninde canlandırabileceği, soruya özel bir mini iş
senaryosu olmalıdır. Mümkün olduğunda gelen gerçek veri, tetiklenen olay,
uygulanan kural ve ekranda/raporda/serviste oluşan somut sonuç örnek değerlerle
gösterilmelidir. Aynı genel kontrol listesi bütün sorulara kopyalanmamalıdır.

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

### Comparable ve Comparator

Tekrar çalışıldı:

- Karşılaştırma sonucunda negatif, sıfır ve pozitif `int` değerlerinin anlamı
- `Comparable<T>` ile sınıfın doğal sırasını `compareTo()` içinde tanımlama
- `Comparator<T>` ile sınıfı değiştirmeden farklı business sıraları oluşturma
- Bir sınıfın genellikle tek doğal sırası, fakat ihtiyaç kadar Comparator'ı
  olabilmesi
- `Integer` ve `String` sınıflarının `Comparable` interface'ini zaten implement
  etmesi
- `comparing`, `comparingInt`, `reversed` ve `thenComparing`
- Comparator'ı `sorted`, `min` ve `max` ile kullanma

Örnekler `src/main/java/org/practice/javacore/comparator` paketindedir.

### Streams

Şu anda işlenen ana konu budur. Yeni örnekler
`src/main/java/org/practice/javacore/streams` altında geliştirilmektedir.

İşlenen ve alıştırma seti hazırlanan operation'lar:

- Stream oluşturma, pipeline, lazy çalışma ve tek kullanımlılık
- `iterate` ve `generate`
- `filter`
- `map`
- `flatMap`
- `forEach`
- `limit`
- `skip`
- `reduce`, `mapToInt`, `mapToDouble`, `sum`, `min`, `max`, `average` ve
  `summaryStatistics`
- `collect` ve hazır `Collectors`
- `collectingAndThen`
- `groupingBy`
- `partitioningBy`
- `count`

Her konu için 10 gerçek uygulama requirement'ı ve ayrı örnek çözüm dosyası
bulunur. Ayrıca bu operation'ları anlamlı pipeline'larda birlikte kullanan 20
karışık uygulama sorusu ve çözümü hazırlanmıştır.

İşlenen Stream operation'larını tek domain akışında tekrar görmek için iki
birbirinden bağımsız kapsamlı örnek bulunur:

- `streams.comprehensive.person`: çalışan rehberi, yetenek envanteri, maaş ve
  şehir raporları
- `streams.comprehensive.product`: katalog, stok, kampanya ve fiyat raporları

Her örnekte POJO modeli, `List` kaynakları, `Map` rapor sonuçları ve konu konu
ayrılmış açıklamalı demo metotları vardır.

Kapsamlı örneklerin konu anlatımı ve önerilen çalışma sırası:
`docs/comprehensive-stream-examples-guide.md`

## Doküman indeksi

### Predicate

- Requirement'lar: `docs/predicate-requirements.md`
- Örnek çözümler: `docs/predicate-solutions.md`

### Function

- Requirement'lar: `docs/function-requirements.md`
- Örnek çözümler: `docs/function-solutions.md`

### Consumer

- Requirement'lar: `docs/consumer-requirements.md`
- Örnek çözümler: `docs/consumer-solutions.md`

### Özel functional interface ve lambda

- Requirement'lar: `docs/lambda-custom-interface-requirements.md`
- Örnek çözümler: `docs/lambda-custom-interface-solutions.md`

### Method reference

- Requirement'lar: `docs/method-reference-requirements.md`
- Örnek çözümler: `docs/method-reference-solutions.md`

### Collections

- Hızlı oluşturma ve kullanım rehberi:
  `docs/java-collections-quick-reference.md`
- Comparable ve Comparator hatırlatma rehberi:
  `docs/comparable-comparator-guide.md`
- Comparator alıştırmaları: `docs/comparator-requirements.md`
- Comparator örnek çözümleri: `docs/comparator-solutions.md`

Map syntax'ını tekrar etmek için `org.practice.streamsandfunctionalinterfaces.mapexamples` paketinde
üç açıklamalı demo ve bir Product POJO'su bulunur:

- `BasicMapOperationsDemo`: `put`, `get`, `getOrDefault`, `containsKey`,
  `containsValue`, `putIfAbsent`, `replace`, `computeIfPresent` ve `remove`
- `MapTraversalDemo`: `keySet`, `values`, `entrySet`, `getKey`, `getValue` ve
  `Map.forEach`
- `ProductMapExamples`: `Map<Integer, Product>` ve
  `Map<Integer, List<Product>>`, ayrıca `computeIfAbsent`

Map alıştırmaları:

- Requirement'lar: `docs/map-requirements.md`
- Örnek çözümler: `docs/map-solutions.md`

### Toplu functional interface özeti

- Projedeki özel ve hazır functional interface kullanımları:
  `docs/functional-interfaces-cheat-sheet.md`
- Predicate, Function ve Consumer lambda örneklerinin kısa cümleli toplu özeti:
  `docs/predicate-function-consumer-lambda-examples.md`

### Stream alıştırmaları

- Person ve Product kapsamlı örnek rehberi:
  `docs/comprehensive-stream-examples-guide.md`
- Stream oluşturma, pipeline ve tek kullanım:
  `docs/stream-basics-requirements.md`,
  `docs/stream-basics-solutions.md`
- Iterate ve generate: `docs/stream-iterate-generate-requirements.md`,
  `docs/stream-iterate-generate-solutions.md`
- Filter: `docs/stream-filter-requirements.md`,
  `docs/stream-filter-solutions.md`
- Map: `docs/stream-map-requirements.md`,
  `docs/stream-map-solutions.md`
- FlatMap: `docs/stream-flatmap-requirements.md`,
  `docs/stream-flatmap-solutions.md`
- ForEach ve Consumer: `docs/stream-foreach-requirements.md`,
  `docs/stream-foreach-solutions.md`
- Limit: `docs/stream-limit-requirements.md`,
  `docs/stream-limit-solutions.md`
- Skip: `docs/stream-skip-requirements.md`,
  `docs/stream-skip-solutions.md`
- Reduce ve sayısal indirgeme: `docs/stream-reduce-requirements.md`,
  `docs/stream-reduce-solutions.md`
- Collect: `docs/stream-collect-requirements.md`,
  `docs/stream-collect-solutions.md`
- CollectingAndThen: `docs/stream-collecting-and-then-requirements.md`,
  `docs/stream-collecting-and-then-solutions.md`
- GroupingBy: `docs/stream-groupingby-requirements.md`,
  `docs/stream-groupingby-solutions.md`
- PartitioningBy: `docs/stream-partitioningby-requirements.md`,
  `docs/stream-partitioningby-solutions.md`
- Count: `docs/stream-count-requirements.md`,
  `docs/stream-count-solutions.md`
- 20 karışık Stream pipeline sorusu: `docs/stream-mixed-requirements.md`,
  `docs/stream-mixed-solutions.md`

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

1. Functional interface, Map, Comparator veya Stream requirement'larını
   öğrencinin seçtiği konudan başlayarak çözmek
2. Her çözümde pipeline sırasını, intermediate/terminal ayrımını ve kullanılan
   functional interface'i açıklamak
3. Konu bazlı sorulardan sonra karışık Stream pipeline sorularına geçmek
4. Consumer requirement'larına gerektiğinde geri dönmek ve kullanıcı istediğinde
   `consumer-solutions.md` oluşturmak
