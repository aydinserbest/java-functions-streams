# Method Reference Alıştırmaları — Requirement'lar

Bu alıştırmalar uyumlu lambda davranışlarını `::` yazımıyla ifade etmeyi, method
reference'ın metodu çağırmadığını davranışı temsil ettiğini çalıştırır.

## 1. Statik fiyat ayrıştırma

String fiyatları `Double::parseDouble` kullanarak sayıya dönüştür.

### Business açısından burada ne yapılıyor?

Tedarikçiden gelen CSV dosyasında fiyatlar `"19.99"` ve `"250.00"` biçiminde
String olarak bulunur. İndirim veya toplam hesabı String üzerinde yapılamaz. İçe
aktarma servisi her satırdaki fiyatı `Double::parseDouble` davranışıyla sayısal
değere çevirmeli ve sonuçta `List<Double>` üretmelidir. Girdilerin bu
alıştırmada geçerli sayısal metin olduğu varsayılır.

## 2. Statik sipariş loglama

`OrderLogger.log(long id)` statik metodunu `Consumer<Long>` hedefiyle kullan.

### Business açısından burada ne yapılıyor?

Gece çalışan toplu görev yüzlerce siparişi tamamlar ve operasyon ekibi hangi
ID'lerin işlendiğini loglarda görmek ister. Projede zaten `OrderLogger.log(long
id)` metodu vardır. Yeni bir lambda içinde aynı çağrıyı tekrar yazmak yerine
statik method reference, `List<Long>. forEach()` tarafından her sipariş ID'si
için çalıştırılmalıdır.

## 3. Belirli nesnenin bildirim metodu

`EmailService service` nesnesindeki `send(String)` metodunu `service::send`
biçiminde Consumer'a ata.

### Business açısından burada ne yapılıyor?

Uygulama başlangıcında SMTP ayarları verilerek hazırlanmış tek bir `EmailService
service` nesnesi vardır. Kampanya alıcılarının e-posta adresleri bu hazır
instance üzerinden gönderilmelidir. `service::send`, başka bir EmailService
oluşturmadan mevcut nesnenin metodunu Consumer davranışı olarak temsil etmeli ve
listedeki her adres için çağrılmalıdır.

## 4. Sınıf üzerinden instance metodu

String listesini `String::trim` ve `String::toUpperCase` ile dönüştür.

### Business açısından burada ne yapılıyor?

Formdan `" alice "` ve `" mehmet"` gibi boşluklu isimler gelir. Rapor sistemi
önce her String'in kendi `trim()` metodunu, sonra `toUpperCase()` metodunu
çalıştırarak `"ALICE"` ve `"MEHMET"` değerlerini üretmelidir. Burada
`String::trim`, belirli tek bir String instance'ına değil Stream'den sırayla
gelen her String nesnesine uygulanır.

## 5. Constructor reference ile müşteri üretme

`Customer(String name)` constructor'ını `Function<String,Customer>` hedefiyle
`Customer::new` olarak kullan.

### Business açısından burada ne yapılıyor?

Eski sistemden yalnızca `["Alice","Mehmet"]` isim listesi aktarılmıştır. Yeni
CRM ise her kayıt için `Customer` nesnesi bekler. `Customer::new`, bir String
alıp yeni Customer üreten `Function<String,Customer>` sözleşmesine bağlanmalı;
Stream `map()` her isim için constructor'ı çağırarak müşteri listesi
oluşturmalıdır.

## 6. Parametresiz constructor factory

`ArrayList::new` ile `Supplier<List<String>>` oluştur.

### Business açısından burada ne yapılıyor?

Bir dışa aktarma servisi her rapor çalışmasında boş ve değiştirilebilir yeni bir
listeyle başlamalıdır. Önceki raporun listesi yeniden kullanılmamalıdır.
`ArrayList::new`, parametre almadan yeni liste üreten Supplier olarak
tanımlanmalı; iki `get()` çağrısının birbirinden farklı liste instance'ları
döndürdüğü gözlemlenmelidir.

## 7. Comparator içinde method reference

Ürünleri `Comparator.comparingInt(Product::getPrice)` ile sırala.

### Business açısından burada ne yapılıyor?

Müşteri katalogda “ucuzdan pahalıya sırala” seçeneğini seçmiştir. Product
sınıfında fiyatı veren `getPrice()` metodu zaten vardır. Comparator, her üründen
karşılaştırma anahtarını `Product::getPrice` ile çıkarmalı; ürünler
değiştirilmeden fiyat sırasına konmalı ve en ucuz ürün listenin başında
görünmelidir.

## 8. Predicate hedefinde method reference

`User::isActive` ile aktif kullanıcıları filtrele.

### Business açısından burada ne yapılıyor?

Bildirim kampanyası yalnızca giriş yapabilen aktif hesaplara gönderilecektir.
User modelindeki `isActive()` metodu zaten gereken evet/hayır bilgisini üretir.
`User::isActive`, `filter()` tarafından her kullanıcı için Predicate davranışı
olarak çalıştırılmalı; pasif kullanıcılar silinmeden yalnızca kampanya sonucunun
dışında bırakılmalıdır.

## 9. Method reference ve hedef tip

`String::length` ifadesini önce `Function<String,Integer>`, sonra
`ToIntFunction<String>` hedeflerine ata ve sonuç tiplerini karşılaştır.

### Business açısından burada ne yapılıyor?

Metin analizi servisi isim uzunluğunu bazen nesne tabanlı genel dönüşüm
altyapısına, bazen primitive istatistik hesabına gönderir. İki yerde de
`String::length` yazılır; fakat `Function<String,Integer>` sonucu boxing ile
Integer, `ToIntFunction<String>` sonucu primitive int üretir. Method
reference'ın tek başına tip taşımadığı, hedef interface'in sözleşmeyi
belirlediği gösterilmelidir.

## 10. Lambda mı method reference mı?

İçinde ek kontrol bulunan e-posta lambdasını ve yalnızca tek metot çağıran
lambda'yı karşılaştır; yalnızca uygun olanı method reference'a dönüştür.

### Business açısından burada ne yapılıyor?

Bir alıcı listesinde her geçerli adrese yalnızca `emailService.send(email)`
çağrısı yapılacaksa method reference kodu sadeleştirir. Ancak başka bir akışta
önce adresin `@` içerdiği kontrol edilip yalnızca geçerliyse gönderim yapılır.
İkinci davranış birden fazla iş adımı taşıdığı için lambda olarak kalmalıdır;
amaç her lambdayı zorla `::` biçimine çevirmek değildir.
