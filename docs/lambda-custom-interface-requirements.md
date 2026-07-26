# Özel Functional Interface ve Lambda — Requirement'lar

Bu alıştırmalar lambda'nın tek başına değil, tek abstract metotlu bir interface
sözleşmesi içinde anlam kazandığını gerçek uygulama davranışlarıyla çalıştırır.

## 1. Müşteriye bildirim gönderme davranışı

`NotificationAction` adında `void send(String customer)` metotlu functional
interface oluştur. SMS ve e-posta gönderimini simüle eden iki lambda yaz.

### Business açısından burada ne yapılıyor?

Bir alışveriş uygulamasında sipariş kargoya verildiğinde müşteri
bilgilendirilir. Bazı müşteriler SMS, bazıları e-posta tercih etmiştir. Ortak
sipariş akışı yalnızca `"Alice"` gibi müşteri bilgisini ve çalıştırılacak
bildirim davranışını almalıdır; telefon operatörü veya e-posta servisi
ayrıntısını bilmemelidir. `smsNotification.send("Alice")` ekrana `"Alice için
SMS gönderildi"`, `emailNotification.send("Mehmet")` ise e-posta gönderim mesajı
yazmalıdır.

## 2. Sipariş tutarı hesaplama stratejisi

`PriceCalculator` içinde `double calculate(double amount)` tanımla. Standart
fiyat, yüzde 10 indirim ve yüzde 20 vergi davranışlarını ayrı lambdalarla
çalıştır.

### Business açısından burada ne yapılıyor?

Bir e-ticaret ödeme ekranında aynı `100.0` euro tutar normal müşteriye aynen,
kampanya müşterisine yüzde 10 indirimli, vergi raporuna ise yüzde 20 artırılmış
olarak gösterilebilir. Ödeme servisi her kampanya için yeniden yazılmamalıdır.
Servis tutarı almalı; hangi fiyat politikasının uygulanacağını `PriceCalculator`
davranışı belirlemeli ve hesaplanmış yeni tutarı döndürmelidir.

## 3. İki sayılı operasyon

`ArithmeticOperation` içinde `int apply(int a, int b)` tanımla. Toplama,
çıkarma, çarpma ve güvenli bölme lambdaları oluştur.

### Business açısından burada ne yapılıyor?

Bir yönetim panelinde iki sayısal metrik üzerinde kullanıcı tarafından seçilen
işlem çalıştırılır: gelirleri toplama, gideri gelirden çıkarma, adet ile birim
fiyatı çarpma veya toplamı çalışan sayısına bölme. Ekranın “hesapla” mekanizması
değişmez; seçilen davranış `ArithmeticOperation` olarak gönderilir. Bölme
işleminde ikinci sayı sıfırsa sistem açık bir hata vermelidir.

## 4. Kayıt doğrulama kuralı

`ValidationRule<T>` içinde `boolean isValid(T value)` tanımla. Kullanıcı adı ve
pozitif sipariş adedi kurallarını aynı generic interface ile kur.

### Business açısından burada ne yapılıyor?

Kayıt ekranı kullanıcı adının boş olmadığını ve en az beş karakter taşıdığını,
sipariş ekranı ise adedin pozitif olduğunu doğrular. Biri String, diğeri Integer
üzerinde çalışmasına rağmen iki süreç de “değeri al, kurala uygun mu kararını
ver” mekanizmasını kullanır. Form kaydedilmeden önce ilgili `ValidationRule<T>`
çalışmalı; `false` sonucunda işlem durdurulmalıdır.

## 5. Domain nesnesini DTO'ya dönüştürme

`Converter<T,R>` içinde `R convert(T source)` tanımla. `Customer` nesnesini
`CustomerSummary` nesnesine dönüştüren lambda yaz.

### Business açısından burada ne yapılıyor?

CRM içindeki Customer nesnesinde kimlik, e-posta, telefon ve dahili notlar
bulunabilir. Arama sonuç ekranı ise yalnızca büyük harfli gösterim adı ve
e-posta bilgisine ihtiyaç duyar. `Converter<Customer,CustomerSummary>` tam
domain nesnesini ekrana uygun küçük DTO'ya çevirmeli; hassas veya gereksiz
alanlar sonuca taşınmamalıdır.

## 6. Sipariş tamamlanınca callback çalıştırma

`OrderCompletedCallback` içinde `void onCompleted(long orderId)` tanımla.
Sipariş kaydı bittikten sonra loglama ve bildirim callback'lerini ayrı ayrı
dene.

### Business açısından burada ne yapılıyor?

Sipariş veritabanına başarıyla kaydedildikten sonra farklı ortamlarda farklı
işler yapılabilir: geliştirme ortamında yalnızca log yazılır, üretimde müşteriye
bildirim gönderilir. Sipariş servisi bu yan işleri kendi içine sabitlememelidir.
Kayıt tamamlanınca `onCompleted(orderId)` çağrılmalı ve dışarıdan verilen
callback aynı sipariş ID'siyle çalışmalıdır.

## 7. Teslimat ücreti stratejisi

`ShippingStrategy` içinde `double fee(double weight)` tanımla. Standart, hızlı
ve ücretsiz teslimat lambdaları oluştur.

### Business açısından burada ne yapılıyor?

Ödeme ekranında 4 kilogramlık bir paket için standart teslimat kilogram başına
1,5 euro, hızlı teslimat 3 euro hesaplayabilir; premium müşteri için ücret sıfır
olabilir. Sepet aynı kalırken müşterinin seçtiği teslimat seçeneği değişir.
`ShippingStrategy` ağırlığı alıp doğru ücreti üretmeli; checkout kodu her yeni
teslimat türü için uzun bir `if/else` zincirine dönüşmemelidir.

## 8. Nesne fabrikası

`Factory<T>` içinde parametresiz `T create()` tanımla. Boş sepet ve varsayılan
rapor nesnesi üreten lambdalar yaz.

### Business açısından burada ne yapılıyor?

Bir alışveriş oturumu başladığında boş ve değiştirilebilir sepet, rapor üretimi
başladığında ise yeni bir `StringBuilder` gerekir. Bu nesneleri isteyen kod
constructor ayrıntısına bağlı kalmamalıdır. `Factory<T>` her `create()`
çağrısında yeni bir nesne üretmeli; iki çağrının aynı sepet instance'ını
yanlışlıkla paylaşmadığı gösterilmelidir.

## 9. Genel davranış çalıştırıcı

```java
<T, R> R execute(T value, Operation<T, R> operation)
```

metodunu ve ilgili functional interface'i yaz. String uzunluğu ve fiyat indirimi
için aynı metodu kullan.

### Business açısından burada ne yapılıyor?

Raporlama altyapısında aynı “değeri al ve verilen işlemi çalıştır” adımı birçok
yerde kullanılır. Bir ekran `"Amsterdam"` değerini karakter sayısına, başka bir
ekran `100.0` fiyatını indirimli fiyata dönüştürür. `execute()` String veya
fiyat ayrıntısını bilmemeli; girdiyle birlikte verilen `Operation<T,R>`
davranışını çalıştırıp doğru sonuç tipini geri vermelidir.

## 10. Anonymous class'ı lambdaya dönüştürme

Bir `AuditAction` anonymous class çözümü yaz, sonra aynı davranışı lambda olarak
ifade et ve ikisini çalıştır.

### Business açısından burada ne yapılıyor?

Eski bir denetim modülünde `"LOGIN_SUCCESS"` olayını yazdırmak için uzun bir
anonymous class kullanılıyor olabilir. Modernizasyon sırasında aynı davranış
lambda biçimine çevrilecektir. İki sürüm de `AuditAction.record(event)`
sözleşmesi üzerinden çalışmalı ve aynı audit satırını üretmelidir; değişen
business davranışı değil yalnızca Java yazım biçimidir.
