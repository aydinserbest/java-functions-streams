# `Stream.iterate()` ve `Stream.generate()` — Requirement'lar

## 1. Ardışık sipariş numaraları

1001'den başlayan ilk 10 sipariş ID'sini `iterate()` ile üret.

### Business açısından burada ne yapılıyor?

Sipariş servisi entegrasyon testinde veritabanına bağlı olmadan 1001–1010
arasında ardışık ID'ler gerekir. Her yeni değer bir önceki ID'ye bir eklenerek
üretilmelidir; bu nedenle üretim geçmiş değere bağlıdır. İki parametreli
`iterate()` sonsuz olduğu için test yalnızca ilk 10 kaydı `limit()` ile almalı
ve sonuç beklenen sıra numaralarını içermelidir.

## 2. Haftalık takvim tarihleri

Başlangıç tarihinden itibaren dört haftalık tarihi `iterate()` ile üret.

### Business açısından burada ne yapılıyor?

Haftalık sebze kutusu aboneliği 1 Ağustos'ta başlar ve müşteriye dört teslimat
tarihi gösterilir. İkinci tarih ilk tarihten, üçüncü tarih ikinci tarihten yedi
gün sonra hesaplanmalıdır. `Stream.iterate(startDate, date ->
date.plusWeeks(1))` önceki değere bağlı takvimi üretmeli ve `limit(4)` ekranın
istediği dönemle sınırlandırmalıdır.

## 3. Kalan borç simülasyonu

1000 eurodan başlayıp her adımda 100 azalt; sıfıra kadar üç argümanlı
`iterate(seed, condition, rule)` kullan.

### Business açısından burada ne yapılıyor?

Müşterinin 1000 euro borcu vardır ve her ay 100 euro taksit öder. Ödeme planı
ekranı `1000, 900, 800 ... 0` kalan bakiyelerini göstermeli, negatif bakiyeye
geçmemelidir. Üç argümanlı `iterate` içindeki condition üretimin ne zaman
duracağını tanımlamalı; bu sonlu seri için ayrıca keyfi `limit()`
gerekmemelidir.

## 4. Stok büyüme tahmini

10 ile başla, her dönemde iki katına çıkan ilk altı stok tahminini üret.

### Business açısından burada ne yapılıyor?

Yeni ürün lansmanında depo ihtiyacının her dönemde iki katına çıktığı
varsayılır: 10, 20, 40, 80, 160, 320. Kapasite planlama ekranı yalnızca ilk altı
dönemi gösterir. Her tahmin önceki dönemin sonucundan üretildiği için
`iterate()` uygun, sonsuz büyüme riski nedeniyle `limit(6)` zorunludur.

## 5. Rastgele doğrulama kodları

`generate()` ve Supplier ile altı haneli beş kod üret.

### Business açısından burada ne yapılıyor?

Giriş servisi kullanıcıya altı haneli tek kullanımlık doğrulama kodu
gönderecektir. Bir kodun değeri önceki koddan hesaplanmaz; her çağrıda Random
bağımsız sonuç üretir. Test ortamı gerçek SMS göndermeden beş örnek kod
istemektedir. `generate(Supplier)` bu bağımsız üretimi sağlamalı, `limit(5)`
sonsuz Supplier akışını durdurmalıdır.

## 6. Sabit sistem mesajı

`generate(() -> "SERVICE_OK")` ile üç sağlık kontrolü cevabı üret.

### Business açısından burada ne yapılıyor?

Monitoring entegrasyonu test edilirken gerçek uzak servise bağlanılmayacaktır.
Stub her sağlık kontrolü isteğinde bağımsız olarak `"SERVICE_OK"` cevabı
üretmelidir. Önceki cevap yeni cevabı etkilemediği için `generate()`
kullanılmalı; üç servis çağrısını simüle eden üç değer üretildikten sonra akış
sonlandırılmalıdır.

## 7. UUID üretimi

`Stream.generate(UUID::randomUUID)` ile üç benzersiz takip ID'si oluştur.

### Business açısından burada ne yapılıyor?

Dağıtık sistemde üç test isteği farklı servisler arasında takip edilecektir. Her
istek benzersiz UUID taşımalı ve bir önceki UUID'den türetilmemelidir.
`UUID::randomUUID` parametresiz Supplier sözleşmesine uyduğu için
`Stream.generate()` ile kullanılmalı; üretilen üç kimliğin farklı olduğu
gözlemlenmelidir.

## 8. `iterate` ve `generate` seçimi

Ardışık fatura ayları için `iterate`, bağımsız kupon kodları için `generate`
kullan ve seçimi açıkla.

### Business açısından burada ne yapılıyor?

Faturalama takviminde Şubat, Ocak'tan; Mart, Şubat'tan hesaplandığı için aylar
ardışık ve önceki değere bağlıdır. Kampanya kuponları ise her çağrıda bağımsız
rastgele üretilir. Aynı ekranda bu iki veri üretimi yan yana gösterilmeli:
fatura ayları `iterate`, kupon kodları `generate` kullanmalıdır; seçim yalnızca
syntax değil veri üretim ilişkisinden doğmalıdır.

## 9. Sonsuz Stream'i limitsiz çalıştırma riski

İki parametreli `iterate()` oluştur, terminal işlem öncesinde `limit()`
eklemenin neden zorunlu olduğunu yorumla.

### Business açısından burada ne yapılıyor?

`Stream.iterate(1, n -> n + 1)` teorik olarak hiç bitmeyen doğal sayı
kaynağıdır. Bu akışa doğrudan `forEach()` verilirse işlem tamamlanmaz ve sürekli
CPU/çıktı üretir. Test yalnızca ilk 100 sıra numarasını istediği için terminal
operation öncesinde `limit(100)` bulunmalı; sınırın business kapasitesini temsil
ettiği açıklanmalıdır.

## 10. Üretilen veriyi filtreleme

1'den başlayan sayılardan ilk beş çift değeri üretmek için `iterate`, `filter`
ve `limit` sırasını kur.

### Business açısından burada ne yapılıyor?

Paketleme testi için 1'den başlayan sıra numaraları arasından yalnızca çift
numaralı ilk beş kutu seçilecektir. Sistem ilk beş sayıyı alıp sonra filtrelerse
yalnızca iki uygun sonuç çıkar. Doğru pipeline önce sonsuz adayları üretmeli,
`filter()` ile çiftleri seçmeli ve ancak bundan sonra `limit(5)` ile beş uygun
kayıtta durmalıdır.
