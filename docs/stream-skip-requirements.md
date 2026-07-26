# Stream `skip()` Alıştırmaları — Requirement'lar

`skip(n)` akışın ilk `n` elemanını atlar. Sıralama yapılmadan “ilk” kavramının
kaynağın mevcut sırasına bağlı olduğunu unutma.

## 1. Ürün aramasının ikinci sayfasını hazırlama

Ürün adları listesinden sayfa boyutu 5 olacak şekilde ikinci sayfayı üret.
`skip(5)` ve `limit(5)` kullan.

### Business açısından burada ne yapılıyor?

Kullanıcı ilk beş ürünü gördükten sonra ikinci sayfayı açar. Servis önce ilk
sayfanın kayıtlarını atlar, ardından yalnızca ikinci sayfaya ait beş kaydı
döndürür.

## 2. İlk üç eğitim videosu tamamlandıktan sonra devam etme

Sıralı video listesinden ilk üç videoyu atla ve kalanları “devam edilecekler”
listesine al.

### Business açısından burada ne yapılıyor?

Öğrenme platformu öğrencinin tamamladığı başlangıç bölümünü tekrar önermez.
Kaynak sırası ders sırasıdır; atlanan elemanlar silinmez, yalnızca devam
görünümünde yer almaz.

## 3. Rapor başlık satırını atlama

CSV satırları listesinin ilk elemanı sütun başlığıdır. İlk satırı atlayıp veri
satırlarını döndür.

### Business açısından burada ne yapılıyor?

İçe aktarma servisi `"name,email,city"` başlığını müşteri kaydı sanmamalıdır.
Dosya yapısına ait metadata atlanır, gerçek kayıtlar işleme gönderilir.

## 4. En yüksek puanlı ilk iki adayı yedek listeden çıkarma

`Candidate(String name, int score)` listesini puana göre azalan sırala, ilk iki
adayı atla ve sonraki üç kişiyi yedek listeye al.

### Business açısından burada ne yapılıyor?

İlk iki aday ana kadroya seçilmiştir. Yedek liste sıralamadaki sonraki üç
kişiden oluşur; bu nedenle `sorted`, `skip` ve `limit` sırası business sonucunu
belirler.

## 5. Son işlenen işlemden sonraki kayıtları alma

Zamana göre sıralı işlem listesinin ilk `processedCount` kaydı daha önce
işlenmiştir. Bu miktarı atlayıp kalan kayıtları döndüren metot yaz.

### Business açısından burada ne yapılıyor?

Toplu iş yarıda durduğunda sistem baştan başlamaz. Checkpoint'te tutulan
işlenmiş kayıt sayısı kadar eleman geçilir ve kalan iş kuyruğu hazırlanır.

## 6. Ücretsiz örnek bölümlerden sonraki ücretli bölümleri gösterme

Kitap bölümleri listesinin ilk iki bölümü ücretsiz örnektir. İlk ikiyi atlayarak
abonelik ekranındaki ücretli içerik listesini üret.

### Business açısından burada ne yapılıyor?

Ön izleme ve abonelik ekranları aynı kaynak listeyi farklı başlangıç
noktalarıyla kullanır. `skip()` yeni görünüm oluşturur; kitap içeriğini
değiştirmez.

## 7. Log dosyasındaki eski 100 kaydı atlama

Kronolojik log mesajlarından ilk 100 kaydı atla ve kalanları inceleme listesine
al. Liste 100'den kısaysa sonucun ne olacağını gözlemle.

### Business açısından burada ne yapılıyor?

Operasyon ekibi daha önce incelenmiş eski bölümün ardından gelen yeni olaylara
odaklanır. Atlanacak sayı akıştan büyükse güvenli biçimde boş sonuç oluşur.

## 8. İlk eşleşen müşteriyi atlayıp alternatifleri bulma

Aktif müşterileri filtrele, isme göre sırala, ilk eşleşmeyi atla ve sonraki üç
müşteriyi alternatif iletişim listesine al.

### Business açısından burada ne yapılıyor?

Birincil müşteri zaten başka temsilciye atanmıştır. Sistem aynı uygunluk
kurallarını sağlayan sıradaki kişileri alternatif olarak gösterir.

## 9. Dinamik sayfalama metodu

```java
<T> List<T> page(List<T> values, int pageNumber, int pageSize)
```

metodunu `skip((long) pageNumber * pageSize)` ve `limit(pageSize)` ile, sıfır
tabanlı sayfa numarası kullanarak yaz. Negatif değerleri reddet.

### Business açısından burada ne yapılıyor?

Ürün, kullanıcı ve sipariş ekranları aynı sayfalama hesabını kullanabilir.
`long` çarpım büyük sayfalarda `int` taşmasını önlemeye yardım eder.

## 10. En yeni kaydı hariç tutarak geçmişi gösterme

`AccountEvent(String description, LocalDateTime occurredAt)` listesini yeniden
eskiye sırala, en yeni olayı atla ve kalan geçmişi göster.

### Business açısından burada ne yapılıyor?

Hesap ekranında en yeni olay ayrı bir “son aktivite” kartında zaten gösterilir.
Geçmiş listesi aynı kaydı tekrarlamamak için sıralamadaki ilk olayı atlar.
