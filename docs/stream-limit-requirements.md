# Stream `limit()` Alıştırmaları — Requirement'lar

`limit(n)` akışın en fazla ilk `n` elemanıyla devam eder. Sonucun anlamlı olması
için bazı senaryolarda önce `sorted()` uygulanması gerekir.

## 1. Ana sayfada son üç haberi gösterme

Tarihe göre yeniden eskiye sıralanmış `News(String title, LocalDateTime
publishedAt)` listesinden ilk üç haberi seç.

### Business açısından burada ne yapılıyor?

Ana sayfanın alanı sınırlıdır. Haber servisi bütün arşivi göndermek yerine,
kullanıcının ilk bakışta göreceği en güncel üç kaydı döndürür.

## 2. En pahalı beş ürünü vitrine çıkarma

`Product(String name, double price)` listesini fiyata göre azalan sırala ve ilk
beş ürünü al.

### Business açısından burada ne yapılıyor?

Premium ürün vitrini katalog sırasına değil fiyata göre belirlenir. Önce business
önceliği oluşturulur, ardından ekran kapasitesi beş ürünle sınırlandırılır.

## 3. Arama önerilerini sekiz sonuçla sınırlama

Ürün adlarından arama metnini içerenleri filtrele ve en fazla sekiz isim döndür.
Arama metni metoda parametre olmalıdır.

### Business açısından burada ne yapılıyor?

Kullanıcı yazarken açılan öneri kutusu yüzlerce sonuç göstermemelidir. Önce
eşleşen kayıtlar bulunur, sonra arayüzün taşıyabileceği ilk sekiz öneri gönderilir.

## 4. Bekleyen ilk on desteği işleme alma

`Ticket(long id, LocalDateTime createdAt, String status)` listesinden `OPEN`
olanları en eski oluşturulandan başlayarak sırala ve ilk on kaydı seç.

### Business açısından burada ne yapılıyor?

Destek ekibi sınırlı kapasiteyle çalışır ve adil olmak için en uzun süredir
bekleyen talepleri önce alır. `limit(10)` günlük çalışma partisinin büyüklüğünü
belirler.

## 5. Kampanyadaki ilk 100 uygun müşteriyi seçme

`Customer(long id, boolean consent, boolean active)` listesinden aktif ve
iletişim izni olan ilk 100 müşteriyi al.

### Business açısından burada ne yapılıyor?

Pazarlama ekibi kampanyayı önce küçük bir kontrollü grupta denemek ister. Yalnızca
yasal iletişim izni ve aktif hesabı olan müşteriler adaydır; gönderim hacmi 100
kişiyle sınırlandırılır.

## 6. Sonsuz seri üretimini güvenli biçimde durdurma

`Stream.iterate(1, number -> number + 1)` ile doğal sayıları üret ve ilk 20
değeri listeye al.

### Business açısından burada ne yapılıyor?

Sıra numarası üreticisi teorik olarak sonsuz değer sağlayabilir. Test veya ön
izleme ekranı ise kontrollü sayıda örnek istemelidir; aksi halde terminal işlem
hiç bitmez.

## 7. Rastgele doğrulama kodlarından beş örnek üretme

`Stream.generate()` ile 100000–999999 aralığında kodlar üret ve beş tanesini
listele.

### Business açısından burada ne yapılıyor?

Geliştirici gerçek SMS göndermeden kod üreticisinin örnek çıktılarını görmek
ister. Üretici sınırsız çalışabildiği için test beş değerle durdurulur.

## 8. En yüksek puanlı üç yorumu gösterme

`Review(String text, int helpfulVotes, boolean approved)` listesinden onaylı
yorumları seç, faydalı oyuna göre azalan sırala ve ilk üçünü al.

### Business açısından burada ne yapılıyor?

Ürün sayfası sosyal kanıt olarak yalnızca moderasyondan geçmiş en faydalı
yorumları öne çıkarır. Seçim, sıralama ve ekran limiti ayrı iş adımlarıdır.

## 9. İlk başarısız beş işlemi inceleme listesine alma

`Payment(long id, boolean successful, LocalDateTime attemptedAt)` listesinden
başarısız olanları tarih sırasına göre seç ve ilk beşini al.

### Business açısından burada ne yapılıyor?

Operasyon ekibi bütün hata geçmişi yerine incelemeye küçük bir örnekle başlar.
Kronolojik ilk beş kayıt, sorunun ne zaman başladığını anlamaya yardım eder.

## 10. Limit değerini dışarıdan alan genel ön izleme

```java
<T> List<T> preview(List<T> values, long maximum)
```

metodunu yaz. Negatif limit verilmesini engelle. Metodu ürün, müşteri ve haber
listeleriyle dene.

### Business açısından burada ne yapılıyor?

Farklı ekran bileşenleri aynı “en fazla N kayıt göster” algoritmasını kullanır.
Metot eleman tipini veya ekranın amacını bilmeden yalnızca görüntülenecek miktarı
sınırlar.

