# Function Alıştırmaları — Requirement'lar

Bu alıştırmaların amacı Java'nın hazır `Function<T, R>` interface'ini gerçek iş
senaryolarında kullanmayı öğrenmektir.

Temel formül:

```text
Function<T, R> = T türünde bir değer alır, R türünde bir değere dönüştürür.
```

Tek bir değerde davranış `apply()` ile, koleksiyonda ise çoğunlukla Stream
API'nin `map()` metodu ile çalıştırılır.

## 1. Müşteri adını standartlaştırma

Bir müşteri adını raporlarda kullanılacak standart biçime dönüştüren bir
`Function<String, String>` tanımla.

Kurallar:

- Baştaki ve sondaki boşlukları kaldır.
- Metni büyük harfe dönüştür.
- Teknik olarak tutarlı sonuç için `Locale.ROOT` kullan.

Şunlarla test et:

```text
"  alice smith  " -> "ALICE SMITH"
"Mehmet Yılmaz"   -> "MEHMET YILMAZ"
```

### Business açısından burada ne yapılıyor?

Bir CRM sistemine müşteri adları farklı biçimlerde girilebilir. Bir kullanıcı
`" alice smith "`, diğeri `"Alice Smith"` yazabilir. Raporlama, arama veya başka
bir sisteme veri aktarımı öncesinde bu değerlerin tek bir biçime dönüştürülmesi
istenir. Function burada bir doğruluk kararı vermez; gelen metni standart bir
metne dönüştürür.

## 2. Formdan gelen fiyatı sayıya dönüştürme

Bir form veya CSV dosyasından metin olarak gelen fiyatı `Double` değerine
dönüştüren bir `Function<String, Double>` tanımla.

Kurallar:

- Baştaki ve sondaki boşlukları kaldır.
- Metni `Double` değerine dönüştür.
- Bu alıştırmada girdilerin sayısal biçimde geçerli olduğunu varsay.

Şunlarla test et:

```text
" 19.99 " -> 19.99
"250"     -> 250.0
```

### Business açısından burada ne yapılıyor?

HTML formları, CSV dosyaları ve bazı dış servisler fiyat bilgisini metin olarak
gönderebilir. Hesaplama yapabilmek için `"19.99"` değerinin `19.99` sayısına
dönüştürülmesi gerekir. Function, veri aktarım katmanındaki String değeri
hesaplamaya uygun `Double` değerine çevirir.

## 3. İndirimli fiyat hesaplama

Bir ürün fiyatına yüzde 20 indirim uygulayan `Function<Double, Double>` tanımla.
Sonucu iki ondalık basamağa yuvarla.

Şunlarla test et:

```text
100.0  -> 80.0
249.99 -> 199.99
```

### Business açısından burada ne yapılıyor?

Bir e-ticaret kampanyasında uygun ürünlerin satış fiyatı yüzde 20 azaltılır.
Predicate kullanılsaydı yalnızca "ürün kampanyaya uygun mu?" kararı verilirdi.
Burada ise mevcut fiyat yeni bir fiyata dönüştürülür; bu nedenle Function
kullanılır.

## 4. Person nesnesini ekranda gösterilecek özete dönüştürme

Şu modelleri oluştur:

```java
Person(String name, int age, String city)
PersonSummary(String displayName, String city)
```

Bir `Person` nesnesini `PersonSummary` nesnesine dönüştüren
`Function<Person, PersonSummary>` tanımla. `displayName` büyük harfli olmalı.

Örnek:

```text
Person("Alice", 30, "Amsterdam")
-> PersonSummary("ALICE", "Amsterdam")
```

### Business açısından burada ne yapılıyor?

Veritabanındaki domain nesnesi her alanıyla kullanıcı arayüzüne gönderilmek
istenmeyebilir. Ekran yalnızca gösterilecek ad ve şehir bilgisine ihtiyaç duyar.
Function burada zengin `Person` nesnesini daha küçük bir görünüm/DTO nesnesine
dönüştürür.

## 5. Ürün listesinden etiket listesi üretme

Şu modeli oluştur:

```java
Product(String name, double price)
```

Bir ürün listesini Stream `map()` ve `Function<Product, String>` kullanarak şu
biçimdeki etiketlere dönüştür:

```text
"Laptop - €1200.00"
"Mouse - €40.00"
```

### Business açısından burada ne yapılıyor?

Katalog ekranında uygulamanın `Product` nesnesinin tamamı yerine kullanıcıya
okunabilir ürün etiketleri gösterilir. Her Product yeni bir String etikete
dönüştürülür. Ürünler elenmediği için `filter()` değil, her elemanı başka bir
biçime çevirdiğimiz için `map()` kullanılır.

## 6. Genel liste dönüştürme metodu

Farklı liste dönüşümlerinde tekrar kullanılabilecek generic bir metot yaz:

```java
<T, R> List<R> transform(
    List<T> values,
    Function<T, R> mapper
)
```

Aynı metodu değiştirmeden şu dönüşümleri yap:

- İsim listesini isim uzunluklarına dönüştür: `String -> Integer`
- Sayı listesini sayıların karelerine dönüştür: `Integer -> Integer`
- Person listesini yalnızca isimlere dönüştür: `Person -> String`

### Business açısından burada ne yapılıyor?

Bir uygulamada dışa aktarma, raporlama ve API cevabı hazırlama gibi pek çok işlem
aynı "listedeki her elemanı başka bir değere dönüştür" algoritmasını kullanır.
Algoritmayı tekrar yazmak yerine dönüşüm davranışı Function olarak metoda
gönderilir. Metot dönüşümün ayrıntısını bilmez; yalnızca `apply()` çalıştırır.

## 7. Sipariş satırının toplamını hesaplama

Şu modeli oluştur:

```java
OrderLine(String productName, int quantity, double unitPrice)
```

Her sipariş satırını toplam tutarına dönüştüren bir
`Function<OrderLine, Double>` tanımla:

```text
satır toplamı = quantity * unitPrice
```

Bir sipariş satırı listesini `map()` ile tutar listesine dönüştür ve ardından
toplam sipariş bedelini hesapla.

Örnek:

```text
Keyboard: 2 × 50.0 -> 100.0
Mouse:    3 × 20.0 -> 60.0
Sipariş toplamı     -> 160.0
```

### Business açısından burada ne yapılıyor?

Faturada her satırın maliyeti adet ve birim fiyattan üretilir. Function bir
`OrderLine` nesnesini parasal satır toplamına dönüştürür. Daha sonra bu dönüşüm
bütün satırlara uygulanarak siparişin toplam bedeli hesaplanabilir.

## 8. Kullanıcı girdisini e-posta anahtarına dönüştürme (`andThen`)

İki ayrı Function tanımla:

- `normalizeEmail`: Metnin boşluklarını kaldırıp küçük harfe dönüştürsün.
- `extractDomain`: E-posta adresinden `@` işaretinden sonraki alan adını çıkarsın.

Bu Function'ları `andThen()` ile birleştirerek tek bir dönüşüm oluştur.

Örnek:

```text
"  ALICE@EXAMPLE.COM  " -> "example.com"
```

Bu alıştırmada girdinin geçerli bir e-posta olduğunu varsay.

### Business açısından burada ne yapılıyor?

Bir şirket, kullanıcıların hangi e-posta sağlayıcılarını kullandığını raporlamak
isteyebilir. Önce kirli kullanıcı girdisi standartlaştırılır, ardından alan adı
çıkarılır. İşlemler ardışık olduğundan ilk Function'ın sonucu ikinci Function'ın
girdisi olur.

## 9. Vergi ve servis ücretini sıralı uygulama (`compose` ve `andThen`)

İki Function tanımla:

- `addServiceFee`: Tutara sabit 10 euro eklesin.
- `addTax`: Tutarı yüzde 20 artırarak vergi eklesin.

Aynı `100.0` tutarı için şu iki akışı hesapla:

```text
Önce servis ücreti, sonra vergi -> 132.0
Önce vergi, sonra servis ücreti -> 130.0
```

Bir sonucu `andThen()`, diğerini `compose()` ile üret ve işlem sırasını açıkla.

### Business açısından burada ne yapılıyor?

Faturalama sistemlerinde ücretlerin hangi sırada uygulandığı nihai tutarı
değiştirebilir. Vergi servis ücretini de kapsıyorsa önce servis ücreti, sonra
vergi uygulanır. Kapsamıyorsa sıra ters olabilir. Function zinciri bu hesaplama
adımlarını açık ve tekrar kullanılabilir biçimde temsil eder.

## 10. CSV çalışan kaydını bordro özetine dönüştürme

Şu modelleri oluştur:

```java
Employee(String name, String department, double monthlySalary)
PayrollSummary(String employeeName, double annualSalary)
```

Üç aşamalı bir dönüşüm oluştur:

1. `Function<String, Employee>`: `"Alice,Engineering,5000"` biçimindeki CSV
   satırını `Employee` nesnesine dönüştürsün.
2. `Function<Employee, PayrollSummary>`: Çalışanı yıllık maaş özetine
   dönüştürsün (`monthlySalary * 12`).
3. İki Function'ı `andThen()` ile birleştirerek doğrudan
   `String -> PayrollSummary` dönüşümü oluştur.

Örnek:

```text
"Alice,Engineering,5000"
-> PayrollSummary("Alice", 60000.0)
```

Birden fazla CSV satırını Stream `map()` ile bordro özetleri listesine dönüştür.

### Business açısından burada ne yapılıyor?

İnsan kaynakları sistemi dışarıdan CSV verisi alabilir, fakat uygulama ham metin
yerine nesnelerle çalışır. İlk Function dış veri biçimini domain nesnesine,
ikinci Function domain nesnesini bordro raporuna dönüştürür. Zincirleme sayesinde
bu iki aşama tek bir tekrar kullanılabilir veri hattı hâline gelir.

