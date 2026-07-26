# Stream `map()` Alıştırmaları — Requirement'lar

## 1. Ürünleri katalog etiketine dönüştürme

`Product(name, price)` listesini `"Laptop - €1200"` String'lerine dönüştür.

### Business açısından burada ne yapılıyor?

Veritabanındaki Product nesnesi ad, fiyat, stok ve dahili alanlar taşır; ürün
kartı ise `"Laptop - €1200"` gibi tek okunabilir etiket ister. `map()` her
Product'ı String etikete dönüştürmeli, hiçbir ürünü elememeli ve kaynak Product
nesnelerini değiştirmemelidir. Sonuçtaki `List<String>` doğrudan katalog
seçicisinde kullanılabilmelidir.

## 2. Çalışanı DTO'ya dönüştürme

`Employee` listesini yalnızca isim ve departman taşıyan `EmployeeSummary`
listesine dönüştür.

### Business açısından burada ne yapılıyor?

İK domainindeki Employee nesnesinde maaş, kimlik ve yönetici notları
bulunabilir. Şirket rehberi API'si yalnızca isim ve departman göstermelidir.
Stream `map()` her Employee için yeni `EmployeeSummary` üretmeli; hassas maaş
alanı DTO'ya taşınmamalı ve listedeki her çalışan için tam bir özet
bulunmalıdır.

## 3. Form fiyatlarını sayıya çevirme

String fiyatları temizleyip `Double` değerlerine dönüştür.

### Business açısından burada ne yapılıyor?

Web formu ve CSV entegrasyonu `" 19.99 "` ile `"250"` fiyatlarını String olarak
gönderir. İndirim ve vergi hesapları başlamadan önce boşluklar temizlenmeli ve
değerler `Double` tipine çevrilmelidir. İki ardışık `map()` adımı dönüşüm
pipeline'ını açıkça göstermeli; geçerli girdiler için sonuç `[19.99,250.0]`
olmalıdır.

## 4. Sipariş satırı toplamları

Her `OrderLine` nesnesini `quantity * unitPrice` değerine dönüştür.

### Business açısından burada ne yapılıyor?

Siparişte Keyboard satırı 2 adet × 50 euro, Mouse satırı 3 adet × 20 eurodur.
Fatura servisi her `OrderLine` nesnesini kendi satır toplamına çevirmeli ve
`[100.0,60.0]` parasal listesini üretmelidir. Bu aşama siparişin genel toplamını
henüz hesaplamaz; yalnızca her domain satırından yeni bir sayısal değer üretir.

## 5. İsimleri normalize etme

İsimleri trim ve büyük harf olmak üzere iki ardışık `map()` ile işle.

### Business açısından burada ne yapılıyor?

CRM'ye isimler `" alice "` ve `"Mehmet "` gibi tutarsız girilmiştir. Rapor önce
kenar boşluklarını temizlemeli, sonra teknik olarak tutarlı büyük harf görünümü
üretmelidir. İki ayrı `map()` adımı `trim` ve `toUpperCase(Locale.ROOT)`
davranışlarını görünür kılmalı; sonuç `"ALICE"` ve `"MEHMET"` olmalıdır.

## 6. ID listesinden nesne bulma

Ürün ID'lerini `Map<Integer,Product>` üzerinden Product değerlerine dönüştür;
bulunmayanları sonra filtrele.

### Business açısından burada ne yapılıyor?

Sipariş servisi `[101,103,999]` ürün ID'lerini alır; katalog Map'inde 101
Laptop, 103 Monitor vardır, 999 yoktur. `map(productById::get)` her referansı
Product veya null sonucuna dönüştürmeli, ardından bulunamayan null kayıtlar
filtrelenmelidir. Nihai liste yalnızca gerçekten katalogda bulunan ürün
nesnelerini içermelidir.

## 7. Optional içindeki Product adını dönüştürme

`max()` sonucundaki `Optional<Product>` üzerinde `map(Product::getName)` kullan.

### Business açısından burada ne yapılıyor?

Katalog özet kartı en pahalı ürünün yalnızca adını gösterecektir. `max()` önce
`Optional<Product>` üretir; liste doluysa Optional içindeki Product
`map(Product::getName)` ile String'e dönüşmelidir. Liste boşsa dönüşüm hiç
çalışmamalı ve `orElse("No products found")` kullanıcıya anlaşılır yedek mesaj
vermelidir.

## 8. İç liste farkını gözlemleme

Person yeteneklerinde `map(Person::getSkills)` kullan ve oluşan
`List<List<String>>` yapısını göster.

### Business açısından burada ne yapılıyor?

Eğitim yöneticisi yetenekleri kişi bazında karşılaştırmak ister: Alice
`[Java,SQL]`, Mehmet `[Spring,AWS]` olarak ayrı satırlarda kalmalıdır.
`map(Person::getSkills)` her Person'ı kendi `List<String>` değerine dönüştürmeli
ve sonuç `List<List<String>>` olmalıdır. Bütün yetenekler tek havuzda
istenmediğinden burada `flatMap()` kullanılmamalıdır.

## 9. Para birimi dönüşümü

Euro fiyatlarını dışarıdan verilen kurla dolar fiyatlarına dönüştür.

### Business açısından burada ne yapılıyor?

Hollanda kataloğundaki `[100.0,250.0]` euro fiyatları ABD ön izlemesinde güncel
`1.10` kuruyla dolar olarak gösterilecektir. Her fiyat `price * exchangeRate`
kuralıyla yeni Double değere dönüşmeli ve kaynak euro listesi korunmalıdır.
Business sonucu `[110.0,275.0]` gibi dolar değerleri olmalı; `map()` kur
dönüşümünü her elemana uygulamalıdır.

## 10. Generic dönüştürme

`<T,R> List<R> transform(List<T>, Function<T,R>)` metodunu Stream map ile yaz.

### Business açısından burada ne yapılıyor?

Dışa aktarma modülü bazen isimleri uzunluklarına, bazen Product nesnelerini
etiketlere, bazen Employee nesnelerini DTO'lara çevirir. Her senaryo için aynı
Stream döngüsü yeniden yazılmamalıdır. `transform()` kaynak listeyi ve
`Function<T,R>` davranışını almalı; listedeki her eleman için mapper'ı
çalıştırıp doğru `List<R>` sonucunu döndürmelidir.
