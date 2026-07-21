# Function Alıştırmaları — Örnek Çözümler

Bu dosya [Function requirement'larının](function-requirements.md) örnek
çözümlerini içerir. Önce requirement'ı kendin çözmeye çalışıp daha sonra çözümle
karşılaştırman önerilir.

## Function için kısa hatırlatma

```java
Function<T, R> mapper = value -> convertedValue;

R result = mapper.apply(value);             // Tek değer
List<R> results = values.stream()
        .map(mapper)                         // Listedeki her değer
        .toList();
```

`Function<T, R>` içindeki `T` girdi, `R` sonuç tipidir. Lambda atandığı anda
dönüşüm çalışmaz; gerçek sonuç `apply()` veya onu içeride çağıran `map()` ile
oluşur.

## 1. Müşteri adını standartlaştırma — Çözüm

### Business açısından burada ne yapılıyor?

CRM sistemine farklı yazım biçimleriyle girilmiş müşteri adları, raporlama ve
arama öncesinde ortak bir biçime getiriliyor. `normalizeCustomerName` bir ismin
geçerli olup olmadığına karar vermiyor; aldığı ham String'i temizlenmiş ve büyük
harfli yeni bir String'e dönüştürüyor.

Gerçek bir CRM ekranında aynı ad `" alice smith "`, `"Alice Smith"` veya
`"ALICE SMITH"` biçiminde girilebilir. Sistem, raporlamadan önce boşlukları
temizlemeli, adı ortak büyük harf biçimine getirmeli ve teknik dönüşümün makinenin
dil ayarından etkilenmesini önlemelidir. Sonuç kabul/ret kararı değil, standart
yeni bir String'dir.

```java
import java.util.Locale;
import java.util.function.Function;

Function<String, String> normalizeCustomerName = name ->
        name.trim().toUpperCase(Locale.ROOT);

System.out.println(normalizeCustomerName.apply("  alice smith  "));
// ALICE SMITH

System.out.println(normalizeCustomerName.apply("Mehmet Yılmaz"));
// MEHMET YILMAZ
```

Burada Function şu dönüşümü taşır:

```text
String -> String
```

`trim()` yeni bir String, `toUpperCase(Locale.ROOT)` da onun büyük harfli yeni
biçimini üretir. İki işlem tek Function gövdesinde uygulanır.

## 2. Formdan gelen fiyatı sayıya dönüştürme — Çözüm

### Business açısından burada ne yapılıyor?

Form veya CSV üzerinden metin olarak gelen fiyat, hesaplamalarda kullanılabilecek
sayısal bir değere çevriliyor. `parsePrice`, veri giriş katmanındaki String ile
fiyat hesaplamalarının beklediği Double tipi arasında dönüştürücü görevi görüyor.

Örneğin ödeme formundan `" 19.99 "` gelir. Sistem önce gereksiz boşlukları
temizlemeli, sonra metni `Double` değerine çevirmelidir. String olarak gelen veri
ancak bu dönüşümden sonra indirim, vergi veya toplam hesaplarında kullanılabilir.

```java
import java.util.function.Function;

Function<String, Double> parsePrice = text ->
        Double.parseDouble(text.trim());

Double firstPrice = parsePrice.apply(" 19.99 ");
Double secondPrice = parsePrice.apply("250");

System.out.println(firstPrice);  // 19.99
System.out.println(secondPrice); // 250.0
```

Tip akışı:

```text
String -> Double
```

`Double.parseDouble()` primitive `double` üretir; Java bunu generic sonuç tipi
olan `Double` için boxing ile sarar. Geçersiz bir metin verilirse
`NumberFormatException` oluşur. Requirement geçerli girdi varsaydığı için hata
yönetimi bu alıştırmanın kapsamı dışındadır.

## 3. İndirimli fiyat hesaplama — Çözüm

### Business açısından burada ne yapılıyor?

Bir kampanya kuralı mevcut satış fiyatını yeni bir satış fiyatına dönüştürüyor.
Function'ın görevi ürünün indirime uygun olup olmadığına karar vermek değil;
kendisine verilen fiyata yüzde 20 indirimi uygulayıp hesaplanmış fiyatı üretmek.

Gerçek bir kampanyada `249.99` euroluk ürün için sistem mevcut fiyatın yüzde
80'ini hesaplamalı ve gösterilecek sonucu iki ondalık basamağa yuvarlamalıdır.
Function çalışınca eski fiyat `199.99` değerine dönüşür. Ürünün kampanyaya uygun
olup olmadığı ise ayrı bir Predicate kuralı olabilir.

```java
import java.util.function.Function;

Function<Double, Double> applyTwentyPercentDiscount = price -> {
    double discountedPrice = price * 0.80;
    return Math.round(discountedPrice * 100.0) / 100.0;
};

System.out.println(applyTwentyPercentDiscount.apply(100.0));  // 80.0
System.out.println(applyTwentyPercentDiscount.apply(249.99)); // 199.99
```

Burada Predicate gibi "indirimli mi?" sorusu sorulmuyor. `Double` fiyat başka bir
`Double` fiyata dönüştürülüyor. Gerçek finans uygulamalarında kayan nokta hassasiyeti
nedeniyle para için çoğunlukla `BigDecimal` tercih edilir; bu alıştırmada odak
Function olduğu için `Double` kullanılmıştır.

## 4. Person nesnesini özete dönüştürme — Çözüm

### Business açısından burada ne yapılıyor?

Uygulamanın iç modelindeki bütün Person bilgileri ekrana açılmak yerine, ekranın
ihtiyaç duyduğu alanlardan küçük bir özet nesnesi hazırlanıyor. Böylece domain
nesnesi ile kullanıcı arayüzüne sunulan veri birbirinden ayrılıyor.

Örneğin profil listesi yalnızca büyük harfli gösterim adı ve şehir ister; yaş
bilgisini istemez. Sistem kaynak Person'ı değiştirmeden `name` alanını
`displayName` biçimine getirmeli ve gereken `city` alanını yeni özet nesnesine
taşımalıdır.

```java
import java.util.Locale;
import java.util.function.Function;

record Person(String name, int age, String city) {}
record PersonSummary(String displayName, String city) {}

Function<Person, PersonSummary> toSummary = person ->
        new PersonSummary(
                person.name().toUpperCase(Locale.ROOT),
                person.city()
        );

Person person = new Person("Alice", 30, "Amsterdam");
PersonSummary summary = toSummary.apply(person);

System.out.println(summary);
// PersonSummary[displayName=ALICE, city=Amsterdam]
```

Tip akışı:

```text
Person -> PersonSummary
```

Lambda parametresi tek bir `Person` nesnesidir. `apply(person)` çağrılınca yeni
bir `PersonSummary` oluşturulur; orijinal Person değiştirilmez.

## 5. Ürün listesinden etiket listesi üretme — Çözüm

### Business açısından burada ne yapılıyor?

Katalogdaki Product nesneleri, kullanıcı arayüzünde gösterilebilecek okunabilir
etiketlere çevriliyor. Hiçbir ürün elenmediği için filtreleme yapılmıyor; her ürün
aynı sırada bir String gösterim değerine dönüştürülüyor.

Katalog ekranında ham Product yerine `"Laptop - €1200.00"` gibi etiketler
gösterilir. Sistemin kuralı her ürünün adını ve fiyatını okunabilir tek bir String
içinde birleştirmektir. Ürün elenmediği için eleman sayısı aynı kalır; eleman tipi
`Product`tan `String`e dönüşür.

```java
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

record Product(String name, double price) {}

List<Product> products = List.of(
        new Product("Laptop", 1200),
        new Product("Mouse", 40)
);

Function<Product, String> toLabel = product ->
        String.format(Locale.ROOT, "%s - €%.2f", product.name(), product.price());

List<String> labels = products.stream()
        .map(toLabel)
        .toList();

System.out.println(labels);
// [Laptop - €1200.00, Mouse - €40.00]
```

`map(toLabel)`, listedeki her Product için arka planda
`toLabel.apply(product)` çalıştırır. Eleman sayısı değişmez; yalnızca her elemanın
tipi `Product`tan `String`e dönüşür.

## 6. Genel liste dönüştürme metodu — Çözüm

### Business açısından burada ne yapılıyor?

Raporlama, dışa aktarma ve API cevabı hazırlama gibi işlemlerde tekrar eden
"listedeki her elemanı dönüştür" algoritması tek metotta toplanıyor. Dönüşümün
ne olacağı metoda Function olarak verildiği için aynı altyapı farklı veri tipleri
ve farklı iş ihtiyaçlarıyla kullanılabiliyor.

Örneğin aynı rapor altyapısı bugün isimleri uzunluklarına, yarın Person
nesnelerini adlarına dönüştürebilir. Ortak kural şudur: Listedeki her değer alınır,
dışarıdan gelen Function uygulanır ve sonuç yeni listeye eklenir. `transform()`
çıktının ne olduğunu bilmez; bunu `Function<T,R>` belirler.

```java
import java.util.List;
import java.util.function.Function;

record Person(String name, int age) {}

static <T, R> List<R> transform(
        List<T> values,
        Function<T, R> mapper
) {
    return values.stream()
            .map(mapper)
            .toList();
}
```

İsimleri uzunluklara dönüştürme:

```java
List<Integer> lengths = transform(
        List.of("Ali", "Mehmet", "Zeynep"),
        String::length
);

System.out.println(lengths); // [3, 6, 6]
```

Sayıları karelerine dönüştürme:

```java
List<Integer> squares = transform(
        List.of(2, 3, 4),
        number -> number * number
);

System.out.println(squares); // [4, 9, 16]
```

Person nesnelerini isimlere dönüştürme:

```java
List<Person> persons = List.of(
        new Person("Alice", 30),
        new Person("Bob", 25)
);

List<String> names = transform(persons, Person::name);
System.out.println(names); // [Alice, Bob]
```

`transform()` dönüşüm ayrıntısını bilmez. `mapper.apply(value)` çağrısını Stream
`map()` aracılığıyla listedeki her değer için gerçekleştirir.

## 7. Sipariş satırının toplamını hesaplama — Çözüm

### Business açısından burada ne yapılıyor?

Her sipariş satırındaki adet ve birim fiyat bilgisi parasal satır toplamına
dönüştürülüyor. Oluşan satır tutarları daha sonra toplanarak siparişin genel
bedeli elde ediliyor; yani Function fatura hesaplama sürecindeki tek satırlık
hesaplama kuralını temsil ediyor.

Gerçek bir faturada `2 × 50` euro Keyboard ve `3 × 20` euro Mouse satırı olsun.
Sistem her satır için `quantity * unitPrice` hesaplayarak `100` ve `60` euro
üretmeli, ardından bunları toplayarak `160` euro sipariş toplamına ulaşmalıdır.
Function'ın işi tek satırı tutara dönüştürmektir.

```java
import java.util.List;
import java.util.function.Function;

record OrderLine(String productName, int quantity, double unitPrice) {}

List<OrderLine> lines = List.of(
        new OrderLine("Keyboard", 2, 50.0),
        new OrderLine("Mouse", 3, 20.0)
);

Function<OrderLine, Double> calculateLineTotal = line ->
        line.quantity() * line.unitPrice();

List<Double> lineTotals = lines.stream()
        .map(calculateLineTotal)
        .toList();

double orderTotal = lineTotals.stream()
        .mapToDouble(Double::doubleValue)
        .sum();

System.out.println(lineTotals); // [100.0, 60.0]
System.out.println(orderTotal); // 160.0
```

`calculateLineTotal` bir sipariş satırını parasal değere dönüştürür:

```text
OrderLine -> Double
```

İkinci stream ise oluşmuş tutarları toplar. Function hesaplamayı, `map()` bu
hesabı bütün satırlara uygulamayı üstlenir.

## 8. E-posta alan adını Function zinciriyle çıkarma — Çözüm

### Business açısından burada ne yapılıyor?

Kullanıcının düzensiz biçimde girdiği e-posta önce standartlaştırılıyor, ardından
raporlama için yalnızca alan adı çıkarılıyor. İki bağımsız dönüşüm `andThen()` ile
tek bir veri hattında birleştiriliyor; ilk aşamanın sonucu otomatik olarak ikinci
aşamanın girdisi oluyor.

Örneğin sisteme `"  ALICE@EXAMPLE.COM  "` girilir. Önce boşluklar temizlenip
harfler küçültülerek `"alice@example.com"`, sonra `@` sonrası alınarak
`"example.com"` üretilmelidir. Bu sıra zorunlu olduğu için `andThen()` iş akışını
soldan sağa açıkça gösterir.

```java
import java.util.Locale;
import java.util.function.Function;

Function<String, String> normalizeEmail = email ->
        email.trim().toLowerCase(Locale.ROOT);

Function<String, String> extractDomain = email ->
        email.substring(email.indexOf('@') + 1);

Function<String, String> normalizeAndExtractDomain =
        normalizeEmail.andThen(extractDomain);

String domain = normalizeAndExtractDomain.apply("  ALICE@EXAMPLE.COM  ");
System.out.println(domain); // example.com
```

`andThen()` soldan sağa çalışır:

```text
"  ALICE@EXAMPLE.COM  "
        -> normalizeEmail
"alice@example.com"
        -> extractDomain
"example.com"
```

Yaklaşık matematiksel karşılığı:

```java
extractDomain.apply(normalizeEmail.apply(input))
```

## 9. Vergi ve servis ücreti sırası — Çözüm

### Business açısından burada ne yapılıyor?

Faturaya servis ücreti ve vergi eklenirken işlem sırasının nihai tutarı nasıl
değiştirdiği modelleniyor. Verginin servis ücretini kapsayıp kapsamaması gibi bir
iş kararı, Function'ların `andThen()` veya `compose()` ile hangi sırada
çalıştırılacağı üzerinden açıkça ifade ediliyor.

100 euroluk işlemde servis ücreti de vergilendiriliyorsa sistem önce 10 euro
ekleyip sonra yüzde 20 vergi uygulamalıdır: `100 -> 110 -> 132`. Servis ücreti
vergiden muafsa önce vergi, sonra ücret uygulanır: `100 -> 120 -> 130`. İşlem
sırası faturayı değiştiren somut bir business kuralıdır.

```java
import java.util.function.Function;

Function<Double, Double> addServiceFee = amount -> amount + 10;
Function<Double, Double> addTax = amount -> amount * 1.20;

// addServiceFee çalışır, çıkan sonuç addTax'e gider.
Function<Double, Double> feeThenTax =
        addServiceFee.andThen(addTax);

// compose içindeki addTax önce, soldaki addServiceFee sonra çalışır.
Function<Double, Double> taxThenFee =
        addServiceFee.compose(addTax);

System.out.println(feeThenTax.apply(100.0)); // 132.0
System.out.println(taxThenFee.apply(100.0)); // 130.0
```

İşlem sıraları:

```text
addServiceFee.andThen(addTax)
100 -> 110 -> 132
```

```text
addServiceFee.compose(addTax)
100 -> 120 -> 130
```

Hatırlatma:

```text
first.andThen(second) = first, sonra second
first.compose(second) = second, sonra first
```

## 10. CSV kaydını bordro özetine dönüştürme — Çözüm

### Business açısından burada ne yapılıyor?

Dış sistemden gelen ham CSV satırı önce uygulamanın anlayacağı Employee nesnesine,
ardından bordro raporunun ihtiyaç duyduğu PayrollSummary nesnesine çevriliyor.
Zincirlenmiş Function, içe aktarmadan rapor üretimine kadar olan dönüşüm sürecini
tek ve tekrar kullanılabilir bir akış hâline getiriyor.

Örneğin dış dosyada `"Alice,Engineering,5000"` satırı bulunur. Sistem alanları
ayırıp Employee oluşturmalı, aylık maaşı 12 ile çarpıp yıllık maaşı hesaplamalı ve
rapora yalnızca ad ile yıllık maaşı taşımalıdır. Zincirin nihai sonucu
`PayrollSummary("Alice", 60000.0)` anlamındaki nesnedir.

```java
import java.util.List;
import java.util.function.Function;

record Employee(String name, String department, double monthlySalary) {}
record PayrollSummary(String employeeName, double annualSalary) {}

Function<String, Employee> parseEmployee = csvLine -> {
    String[] fields = csvLine.split(",");
    return new Employee(
            fields[0].trim(),
            fields[1].trim(),
            Double.parseDouble(fields[2].trim())
    );
};

Function<Employee, PayrollSummary> toPayrollSummary = employee ->
        new PayrollSummary(
                employee.name(),
                employee.monthlySalary() * 12
        );

Function<String, PayrollSummary> csvToPayrollSummary =
        parseEmployee.andThen(toPayrollSummary);

List<String> csvLines = List.of(
        "Alice,Engineering,5000",
        "Bob,Sales,4000"
);

List<PayrollSummary> payroll = csvLines.stream()
        .map(csvToPayrollSummary)
        .toList();

payroll.forEach(System.out::println);
```

Beklenen çıktı:

```text
PayrollSummary[employeeName=Alice, annualSalary=60000.0]
PayrollSummary[employeeName=Bob, annualSalary=48000.0]
```

Tip akışı:

```text
String -> Employee -> PayrollSummary
```

`parseEmployee.andThen(toPayrollSummary)` bu iki ayrı dönüşümü tek bir
`Function<String, PayrollSummary>` hâline getirir. Stream `map()` de oluşan
Function'ı her CSV satırı için çalıştırır.
