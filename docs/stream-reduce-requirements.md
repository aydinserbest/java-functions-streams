# Stream `reduce()` ve Sayısal İndirgeme Alıştırmaları — Requirement'lar

Bu bölümde `reduce`, `mapToInt`, `mapToDouble`, `sum`, `min`, `max`, `average`
ve `summaryStatistics` kullanılır. İndirgeme, çok sayıda elemanı tek bir sonuca
dönüştüren terminal işlemdir.

## 1. Siparişin toplam tutarını hesaplama

`OrderLine(String product, int quantity, double unitPrice)` listesindeki her
satırı `quantity * unitPrice` tutarına dönüştür ve `reduce(0.0, Double::sum)` ile
sipariş toplamını bul.

### Business açısından burada ne yapılıyor?

Ödeme ekranı tek tek satır fiyatlarını değil, müşteriden tahsil edilecek nihai
tutarı ister. `map()` satır maliyetini üretir; `reduce()` bütün maliyetleri tek
ödeme tutarında birleştirir.

## 2. Günlük işlem tutarlarını `sum()` ile toplama

`Transaction(long id, double amount, boolean successful)` listesinden başarılı
işlemleri seç, `mapToDouble()` ile tutarlara dönüştür ve `sum()` kullan.

### Business açısından burada ne yapılıyor?

Finans raporu başarısız denemeleri ciroya katmaz. Geçerli ödeme nesneleri
primitive parasal değerlere çevrilir ve günlük tahsilat toplamı hesaplanır.

## 3. Sepetteki toplam ürün adedini bulma

`CartItem(String product, int quantity)` listesindeki miktarları `mapToInt()` ve
`sum()` ile topla. Boş sepette sonuç `0` olmalıdır.

### Business açısından burada ne yapılıyor?

Sepet rozeti farklı ürün çeşidini değil, müşterinin satın alacağı toplam birim
sayısını gösterir. İki kalemden üçer adet varsa sonuç iki değil altıdır.

## 4. En yüksek teklif tutarını bulma

`Bid(String bidder, double amount)` listesindeki en yüksek tutarı bul. Boş liste
ihtimali nedeniyle `OptionalDouble` sonucunu güvenli biçimde ele al.

### Business açısından burada ne yapılıyor?

Açık artırma ekranı mevcut lider teklifi gösterir. Henüz teklif yoksa hayali bir
kazanan üretmek yerine “teklif yok” durumu ayrıca temsil edilmelidir.

## 5. En düşük teslimat süresini bulma

`DeliveryOption(String company, int estimatedDays)` listesinden en düşük gün
sayısını `mapToInt().min()` ile bul ve şirket bilgisi gerekmiyorsa neden yalnızca
sayısal değere dönüştürdüğünü açıkla.

### Business açısından burada ne yapılıyor?

Ödeme ekranındaki özet yalnızca “en erken 2 günde teslimat” bilgisini gösterecekse
tam şirket nesnesine ihtiyaç duymaz. Kayıt yoksa sonuç bulunmayabilir.

## 6. Müşteri puan ortalamasını hesaplama

`Review(String customer, int rating)` listesindeki 1–5 arası puanların
ortalamasını `average()` ile hesapla. Yorum yoksa `0.0` göster.

### Business açısından burada ne yapılıyor?

Ürün kartındaki yıldız puanı bütün değerlendirmelerin aritmetik ortalamasıdır.
Hiç değerlendirme yokken bölme yapılmaz; arayüz için belirlenen varsayılan değer
kullanılır.

## 7. Maaş istatistiklerini tek geçişte çıkarma

`Employee(String name, int salary)` listesinden `IntSummaryStatistics` kullanarak
çalışan sayısı, toplam, minimum, maksimum ve ortalama maaşı üret.

### Business açısından burada ne yapılıyor?

İnsan kaynakları aynı departman için beş ayrı rapor sorgusu çalıştırmak istemez.
Tek terminal işlemle özet göstergeler hazırlanır.

## 8. İndirimleri sırayla tek fiyata uygulama

Başlangıç fiyatı `200.0` ve indirim oranları `[0.10, 0.20]` olsun. Identity olarak
`1.0` kullanarak oranları kalan fiyat çarpanlarına (`0.90`, `0.80`) dönüştür ve
bu çarpanları `reduce` ile birleştir. Birleşik çarpanı başlangıç fiyatıyla çarp.
Beklenen sonuç `144.0` olmalıdır.

### Business açısından burada ne yapılıyor?

Ardışık kampanyalarda indirim yüzdeleri toplanmaz; kalan fiyat oranları
çarpılır. `1.0` çarpmanın gerçek identity değeridir ve çözüm paralel Stream
sözleşmesine de uygun, associative bir indirgeme kullanmalıdır.

## 9. Etiketleri tek metinde birleştirme

`["java", "stream", "api"]` değerlerini `reduce()` kullanarak
`"java, stream, api"` metnine dönüştür. Baştaki gereksiz virgülü engelle ve boş
liste davranışını açıkla.

### Business açısından burada ne yapılıyor?

İçerik yönetim sistemi etiketleri ayrı kayıtlar halinde tutar, fakat dışa aktarma
dosyasında tek bir okunabilir alan ister. Ayraç yalnızca elemanlar arasına
gelmelidir.

## 10. Genel toplama davranışı çalıştırma

```java
<T> Optional<T> combine(List<T> values, BinaryOperator<T> operation)
```

metodunu yaz. Aynı metotla sayıların toplamını, sayıların çarpımını ve
String'lerin birleştirilmesini yap.

### Business açısından burada ne yapılıyor?

Listeyi tek sonuca indirme algoritması değişmez; farklı raporların birleştirme
kuralı dışarıdan davranış olarak gelir. Başlangıç değeri verilmediğinden boş
listenin sonucu `Optional.empty()` olur.
