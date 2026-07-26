# `Collectors.groupingBy()` — Requirement'lar

## 1. Çalışanları departmana göre gruplama

`Employee` listesini `Map<String,List<Employee>>` yap.

### Business açısından burada ne yapılıyor?

Şirketin organizasyon ekranı, tek çalışan listesini “Engineering”, “Sales” ve
“Finance” başlıkları altında gösterecektir. Her başlığın altında o departmanda
çalışan kişilerin tamamı bulunmalıdır. Sonuçtaki Map'in anahtarı departman adı,
değeri ise o departmana ait çalışan listesidir.

## 2. Ürünleri kategoriye göre gruplama

Ürünleri kategori anahtarıyla grupla.

### Business açısından burada ne yapılıyor?

E-ticaret kataloğunda ürünler tek bir kaynaktan gelir, fakat müşteri ekranda
“Elektronik”, “Mobilya” ve “Aksesuar” bölümlerini ayrı görmek ister. Her ürün
kendi kategori anahtarının altına yerleştirilerek kategori menüsü ve ürün
bölümleri hazırlanır. Aynı kategoride birden fazla ürünün kalması gerektiği için
burada basit `toMap` değil gruplama yapılır.

## 3. Sipariş durumlarını sayma

`groupingBy(status, counting())` ile durum başına adet üret.

### Business açısından burada ne yapılıyor?

Operasyon ekibi dashboard'da kaç siparişin yeni, kaçının ödenmiş ve kaçının
kargolanmış olduğunu görmek ister. Binlerce siparişin ayrıntısını taşımak yerine
her durum için yalnız adet üretilir. Böylece örneğin `PAID -> 128` değeri ödeme
sonrası bekleyen iş yükünü doğrudan gösterir.

## 4. Şehir başına ortalama maaş

`averagingDouble()` downstream collector kullan.

### Business açısından burada ne yapılıyor?

İK bütçe çalışmasında Amsterdam, Berlin ve İstanbul ofislerinin ortalama maaş
maliyetlerini karşılaştıracaktır. Çalışanlar şehirlerine göre ayrılır ve her
şehir grubundaki maaşların ortalaması hesaplanır. Sonuç, şehir adı ile o ofisin
ortalama maaşını eşleyen bir Map olur.

## 5. Ülke ve şehir olarak iç içe gruplama

Müşterileri önce ülke, sonra şehir anahtarıyla grupla.

### Business açısından burada ne yapılıyor?

Uluslararası CRM ekranında önce ülke seçilecek, ardından o ülkenin
şehirlerindeki müşteriler gösterilecektir. Bu nedenle tek seviyeli şehir grubu
yeterli değildir; örneğin önce `Türkiye`, onun altında `İstanbul` ve `Ankara`
grupları oluşmalıdır. İç içe `groupingBy`, ekranın ülke → şehir → müşteriler
yapısını doğrudan üretir.

## 6. Fiyat grubunda yalnızca ürün isimleri

`mapping(Product::name, toList())` ile `Map<Integer,List<String>>` üret.

### Business açısından burada ne yapılıyor?

Bir karşılaştırma ekranı aynı fiyattaki ürünleri fiyat başlıkları altında
listeleyecek, ancak ürünün stok ve iç kimlik gibi tüm alanlarına ihtiyaç
duymayacaktır. Örneğin `150 -> [Desk Chair, Office Chair]` çıktısı yeterlidir.
Gruplama fiyat anahtarını korurken downstream `mapping`, her `Product`ı yalnız
gösterilecek adına dönüştürür.

## 7. Kategori başına stok toplamı

`summingInt(Product::stock)` kullan.

### Business açısından burada ne yapılıyor?

Depo yöneticisi tek tek ürün stoklarından çok, her kategoride toplam kaç
satılabilir birim bulunduğunu izler. Elektronik kategorisindeki bütün ürünlerin
stokları bir araya getirilerek örneğin `ELECTRONICS -> 420` sonucu üretilir. Bu
özet, satın alma ekibinin hangi kategoriye takviye gerektiğini görmesini sağlar.

## 8. Departmanın en yüksek maaşlı çalışanı

`groupingBy` ile `maxBy` downstream collector'ını birleştir.

### Business açısından burada ne yapılıyor?

İK ücret denetiminde şirket genelindeki tek bir maksimumu değil, her departmanın
en yüksek maaşlı çalışanını görmek ister. Çalışanlar departmana göre gruplanır;
her grubun içinde maaşı en yüksek kişi ayrıca seçilir. Departman boşluğu veya
seçim sonucu güvenli biçimde temsil edilebilmesi için değerler
`Optional<Employee>` olabilir.

## 9. Anahtarları sıralı Map'te tutma

Üç argümanlı `groupingBy` ile sonuç kabı olarak `TreeMap::new` kullan.

### Business açısından burada ne yapılıyor?

Aylık organizasyon raporu departman bölümlerini her çalıştırmada aynı alfabetik
sırayla göstermelidir. Varsayılan Map türünün dolaşım sırasına güvenmek rapor
çıktısını öngörülemez yapabilir. Sonuç kabı olarak `TreeMap` seçilerek hem
gruplama yapılır hem de anahtarlar ayrıca sıralama kodu yazmadan düzenli gelir.

## 10. Fiyat bandına göre gruplama

Ürünleri `BUDGET`, `STANDARD`, `PREMIUM` anahtarlarına sınıflandır.

### Business açısından burada ne yapılıyor?

Pazarlama ekibi tek tek fiyatlarla değil, müşteriye sunulacak `BUDGET`,
`STANDARD` ve `PREMIUM` ürün segmentleriyle kampanya hazırlamak ister. Her ürün
önceden belirlenen fiyat sınırlarına göre bir bant anahtarına atanır. Ortaya
çıkan gruplar, segment bazlı indirim ve vitrin çalışmalarında doğrudan
kullanılır.
