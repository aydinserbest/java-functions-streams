# `Collectors.collectingAndThen()` — Requirement'lar

## 1. Değiştirilemez kampanya listesi

Aktif müşterileri listeye topla ve `List::copyOf` finisher'ıyla değiştirilemez
hale getir.

### Business açısından burada ne yapılıyor?

Pazarlama servisi, iletişim izni bulunan aktif müşterilerden kesin gönderim
listesini hazırlar. SMS gönderimi başladıktan sonra başka bir kodun bu listeden
müşteri silmesi veya listeye müşteri eklemesi, raporlanan alıcılarla gerçek
alıcıların farklı olmasına yol açar. Bu yüzden toplama bittikten sonra sonuç
değiştirilemez hale getirilerek kampanya boyunca aynı alıcı listesi korunur.

## 2. En pahalı ürün adını üretme

`maxBy()` sonucundaki `Optional<Product>` değerini ürün adına veya `"No products
found"` mesajına dönüştür.

### Business açısından burada ne yapılıyor?

Bir yönetim panelinin “Katalogdaki en pahalı ürün” alanında yalnızca ürün adı
gösterilecektir. Ürün varsa fiyatı en yüksek `Product` bulunup adı üretilmeli;
katalog boşsa `null` ya da anlamsız bir `Optional` yerine kullanıcıya `"No
products found"` gösterilmelidir. İlk collector seçimi yapar, bitirme fonksiyonu
ise sonucu ekranın doğrudan kullanabileceği metne dönüştürür.

## 3. Sipariş toplamını özet DTO'ya çevirme

`summingDouble()` sonucunu `OrderTotalSummary` record'una dönüştür.

### Business açısından burada ne yapılıyor?

Sipariş servisi, seçilen siparişlerin toplam tutarını hesaplayıp API cevabında
anlamlı bir alan adıyla göndermek ister. İstemciye çıplak bir `double` vermek
yerine toplam, `OrderTotalSummary` adlı cevap modelinin içine yerleştirilir.
Böylece hem hesaplama tek akışta tamamlanır hem de sonraki katman neyi temsil
ettiği belli olan bir nesne alır.

## 4. Alıcı isimlerine başlık ekleme

İsimleri `joining(", ")` ile birleştir ve finisher ile başına `"Campaign
recipients: "` ekle.

### Business açısından burada ne yapılıyor?

Kampanya detay ekranında alıcılar `Alice, Mehmet, Sofia` biçiminde
birleştirilecek, fakat bu metnin neyi anlattığı da açıkça görülecektir. Toplama
işlemi isimleri virgülle birleştirir; bitirme adımı başına `"Campaign
recipients: "` başlığını ekler. Sonuç, ek biçimlendirme gerektirmeden rapora
yazılabilecek tam bir cümle olur.

## 5. Benzersiz şehirleri sıralı liste yapma

Şehirleri önce Set'e topla, sonra finisher ile sıralı List'e dönüştür.

### Business açısından burada ne yapılıyor?

Bir kargo filtresinde yalnızca sipariş gönderilen benzersiz şehirler
listelenecektir. Aynı şehir yüzlerce siparişte geçse bile seçenek bir kez
görünmeli ve kullanıcı şehri kolay bulabilsin diye sonuç alfabetik olmalıdır.
Önce `Set` tekrarları kaldırır, bitirme fonksiyonu bu sonucu sıralı bir `List`e
çevirir.

## 6. Departman çalışan listelerini kilitleme

`groupingBy` downstream'inde `collectingAndThen(toList(), List::copyOf)` kullan.

### Business açısından burada ne yapılıyor?

İK raporu çalışanları departmanlarına ayırdıktan sonra bu grupları başka
servislere paylaşır. Sonradan bir tüketicinin “Engineering” listesinden çalışan
silmesi, raporun güvenilirliğini bozabilir. Bu nedenle yalnız dıştaki Map değil,
her departmana ait çalışan listesi de toplama tamamlanırken değiştirilemez hale
getirilir.

## 7. Ortalama puanı formatlama

`averagingDouble()` sonucunu `"Average: 4.25"` String'ine dönüştür.

### Business açısından burada ne yapılıyor?

Müşteri değerlendirmelerinden hesaplanan ortalama puan bir yönetim raporunda iki
ondalık basamakla gösterilecektir. `averagingDouble()` hesaplama için gereken
sayısal değeri üretir; kullanıcı ise `4.253846... ` gibi ham bir sayı değil,
`"Average: 4.25"` biçiminde okunabilir bir ifade görmelidir. Finisher bu sunum
kuralını toplama işleminin sonunda uygular.

## 8. Boş olmayan sonuç kuralı

Listeye toplanan onaylı kayıtlar boşsa finisher'da exception fırlat.

### Business açısından burada ne yapılıyor?

İçerik yayınlama servisi yalnız editör onayından geçmiş yazılardan bir yayın
paketi oluşturur. Filtreleme sonunda hiç onaylı yazı kalmadıysa boş bir paket
üretmek iş açısından geçersizdir ve durum hemen bildirilmelidir. Finisher,
toplanan listenin boşluğunu kontrol ederek açık bir exception fırlatır; geçersiz
verinin sonraki servislere ilerlemesini engeller.

## 9. İlk üç ürünü immutable vitrine çevirme

Sıralanmış ilk üç ürünü topla ve değiştirilemez liste döndür.

### Business açısından burada ne yapılıyor?

E-ticaret ana sayfasında fiyat veya satış puanına göre seçilmiş ilk üç ürün “öne
çıkanlar” alanında gösterilecektir. Seçim ve sıralama tamamlandıktan sonra
şablon kodunun listeye dördüncü ürün eklemesi ya da bir ürünü çıkarması
istenmez. Sonuç değiştirilemez yapılarak onaylanan üçlü vitrin bütün katmanlarda
aynı tutulur.

## 10. Generic bitirme fonksiyonu

Bir Collector sonucunu dışarıdan verilen `Function<R,RR>` ile bitiren örnek
metot veya collector factory kur.

### Business açısından burada ne yapılıyor?

Raporlama altyapısında aynı veriler önce liste, toplam veya özet olarak
toplanabilir; farklı tüketiciler ise bu sonucu DTO, değiştirilemez koleksiyon ya
da gösterim metni olarak isteyebilir. Her rapor için collector kodunu kopyalamak
yerine son dönüşüm dışarıdan bir `Function<R,RR>` olarak alınır. Böylece ortak
toplama davranışı korunur, yalnız teslim biçimi ihtiyaca göre değişir.
