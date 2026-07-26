# Karışık Stream Pipeline Alıştırmaları — 20 Requirement

Bu sorular birden fazla Stream operation'ını gerçek bir iş akışında birleştirir.
Amaç yalnızca metotları kullanmak değil, pipeline sırasının business sonucunu
nasıl değiştirdiğini anlamaktır.

## 1. Katalog ana sayfasındaki premium vitrin

`Product(String name, String category, double price, boolean active, int stock)`
listesinden aktif, stoklu ve fiyatı 100 eurodan yüksek ürünleri seç. Fiyata göre
azalan sırala, ilk dört ürünü `"Ürün - €fiyat"` etiketine dönüştür ve listele.

### Business açısından burada ne yapılıyor?

Müşteri yalnızca satın alınabilir premium ürünleri görmelidir. Önce uygunluk
kuralları uygulanır, sonra premium sırası kurulur ve sınırlı vitrin alanı dört
ürünle doldurulur. `limit` filtre öncesinde olursa dört uygun ürün
garantilenmez.

## 2. Sevkiyat için toplam ürün adedi

`Order(long id, String status, List<OrderLine> lines)` ve `OrderLine(String sku,
int quantity)` modelleriyle yalnızca `PAID` siparişlerin satırlarını düzleştir
ve toplam ürün adedini hesapla.

### Business açısından burada ne yapılıyor?

Depo henüz ödenmemiş siparişleri hazırlamaz. Siparişler seçildikten sonra satır
katmanı açılır; satır sayısı değil, quantity alanlarının toplamı gerçek
paketleme hacmini verir.

## 3. Departman bazında aktif çalışan sayısı

`Department(String name, List<Employee> employees)` ve `Employee(String name,
boolean active)` verisinden bütün çalışanları düzleştir, aktifleri seç ve
departman bilgisini kaybetmeden departman başına sayıyı `Map<String,Long>` üret.

### Business açısından burada ne yapılıyor?

Yönetim organizasyonun aktif kadrosunu departman bazında görmek ister.
Düzleştirme sırasında departman adı da sonuç kaydına taşınmalıdır; aksi halde
gruplama anahtarı kaybolur.

## 4. Üçüncü sayfadaki arama sonuçları

`Product` listesinden arama metniyle eşleşen aktif ürünleri ada göre sırala.
Sayfa boyutu 10, sayfa numarası sıfır tabanlı 2 olacak şekilde üçüncü sayfayı
üret.

### Business açısından burada ne yapılıyor?

Kullanıcı arama sonucunun üçüncü sayfasını ister. Önce bütün uygun sonuçlar aynı
sıraya getirilir, sonra ilk 20 sonuç atlanıp sonraki 10 kayıt alınır; böylece
sayfalar arasında tekrar veya kayma olmaz.

## 5. En çok gelir getiren üç kategori

`Sale(String category, int quantity, double unitPrice, boolean refunded)`
listesinden iade edilmemiş satışları seç. Kategori başına geliri hesapla, toplam
gelire göre azalan sırala ve ilk üç kategori sonucunu üret.

### Business açısından burada ne yapılıyor?

Satış yönetimi brüt işlem sayısını değil, iade dışındaki gerçek kategori
gelirini karşılaştırır. Satır geliri `quantity * unitPrice` olarak hesaplanır,
kategori altında toplanır ve lider üç kategori raporlanır.

## 6. Kampanya için benzersiz e-posta örneği

`Customer(String email, boolean active, boolean consent)` listesinden aktif ve
izinli müşterilerin geçerli e-postalarını seç, küçük harfe dönüştür, tekrarları
kaldır ve ilk 100 adresi değiştirilebilir listeye topla.

### Business açısından burada ne yapılıyor?

Gönderim servisi pasif veya izinsiz müşterilere ulaşmamalı, aynı adresi farklı
yazımlarla iki kez hedeflememelidir. Pilot kampanya en fazla 100 benzersiz
alıcıya gider ve liste gönderim öncesi düzenlenebilir.

## 7. Ürün değerlendirme özeti

`Product(String name, List<Review> reviews)` ve `Review(int rating, boolean
approved)` yapısında seçilen ürünün onaylı yorum sayısını ve ortalama puanını
hesapla.

### Business açısından burada ne yapılıyor?

Ürün kartı moderasyondan geçmemiş puanları müşteriye yansıtmaz. Aynı onaylı veri
kaynaklarından hem değerlendirme adedi hem yıldız ortalaması üretilir; yorum
yoksa ortalama `0.0` olur.

## 8. Yedek teslimat seçenekleri

`DeliveryOption(String company, double price, int days, boolean available)`
listesinden mevcut seçenekleri önce gün, sonra fiyat sırasına koy. En iyi
seçeneği atla ve sonraki iki alternatifi listele.

### Business açısından burada ne yapılıyor?

Ödeme ekranında en iyi teslimat seçeneği ana seçim olarak ayrıca gösterilir.
Alternatifler kartında aynı kayıt tekrarlanmaz; sıradaki iki uygun seçenek
sunulur.

## 9. Proje panosundaki acil görevler

`Project(String name, List<Task> tasks)` ve `Task(String title, int priority,
boolean completed)` listesinden bütün açık görevleri düzleştir, önceliğe göre
azalan sırala ve ilk beşini `"Proje: görev"` biçiminde göster.

### Business açısından burada ne yapılıyor?

Yönetici proje sınırlarından bağımsız en acil açık işleri görmek ister. Görev
düzleştirilirken hangi projeye ait olduğu korunmalı; çıktı aksiyon alınabilir
bir etiket taşımalıdır.

## 10. Banka hareketlerinde riskli toplam

`Account(String iban, List<Transaction> transactions)` ve `Transaction(double
amount, String country, boolean successful)` verisinden başarılı, ülke dışı ve
1000 euro üzerindeki hareketleri düzleştir. Adetlerini ve toplam tutarlarını
hesapla.

### Business açısından burada ne yapılıyor?

Risk motoru bütün hareketleri şüpheli saymaz. Üç koşulu karşılayan işlemlerin
hem olay adedi hem parasal etkisi denetim panelinde gösterilir.

## 11. İkinci parti eski açık destek kayıtları

`Ticket(long id, String status, LocalDateTime createdAt)` listesinden açık
kayıtları eskiden yeniye sırala. İlk çalışma partisindeki 20 kaydı atlayıp
sonraki 20 kaydı al.

### Business açısından burada ne yapılıyor?

İlk destek ekibi ilk 20 kaydı almıştır. İkinci ekip aynı sıralamadaki sonraki
partiyi alır; filtre ve sıralama her iki parti için de aynı olmalıdır.

## 12. Ders kataloğundaki benzersiz eğitmenler

`Course(String title, boolean published, List<Instructor> instructors)` ve
`Instructor(long id, String name)` listesinden yayınlanmış derslerin
eğitmenlerini düzleştir, ID'ye göre tekilleştir ve ada göre sıralı liste üret.

### Business açısından burada ne yapılıyor?

Eğitmen aynı anda birkaç ders verebilir, fakat katalog filtresinde bir kez
görünmelidir. Yayında olmayan dersin eğitmeni bu görünümden dolayı listeye
eklenmez.

## 13. Fatura müşterilerine göre borç toplamı

`Invoice(String customer, double amount, boolean paid)` listesinden ödenmemiş
faturaları seç ve müşteri başına toplam borcu `Map<String,Double>` olarak topla.

### Business açısından burada ne yapılıyor?

Tahsilat ekibi tek tek faturalardan önce müşterinin toplam riskini görür.
Ödenmiş kayıtlar borca dahil edilmez; aynı müşterinin açık faturaları
birleştirilir.

## 14. Son beş benzersiz arama

`SearchEvent(String userId, String query, LocalDateTime time)` listesinden
belirli kullanıcının olaylarını yeniden eskiye sırala, sorgu metnini normalize
et ve en son beş benzersiz sorguyu al.

### Business açısından burada ne yapılıyor?

Arama kutusu kullanıcının yakın geçmişini gösterir. Aynı sorgunun tekrarları
alanı doldurmamalı; en yeni tekrarın sırası korunarak beş farklı öneri
sunulmalıdır.

## 15. Şubelere göre benzersiz ürün sayısı

`Branch(String city, List<Product> products)` verisinde her şube için benzersiz
SKU sayısını `Map<String,Long>` olarak üret.

### Business açısından burada ne yapılıyor?

Merkez raporu ürün birimi veya stok adedini değil, şubenin sunduğu farklı ürün
çeşidi sayısını karşılaştırır. Aynı SKU şube içinde tekrar görünse de bir kez
sayılır.

## 16. Maaş bordrosu üst sınır raporu

`Employee(String name, double monthlySalary, boolean active)` listesindeki aktif
çalışanları maaşa göre azalan sırala; en yüksek iki yöneticiyi ayrı raporda
oldukları için atla. Sonraki beş çalışanın adını ve yıllık maaşını listele, bu
beş maaşın toplamını da hesapla.

### Business açısından burada ne yapılıyor?

Bütçe analisti yönetici ücretlerini hariç tutarak sıradaki yüksek maliyetli
kadroyu inceler. Seçilen aynı beş kayıt hem detay listesine hem toplam maliyete
kaynak olmalıdır.

## 17. Etkinlik katılımcı ülke özeti

`Event(String name, boolean published, List<Participant> participants)` ve
`Participant(String name, String country, boolean approved)` modellerinden
yalnızca yayınlanmış etkinliklerin onaylı katılımcılarını düzleştir ve ülke
başına katılımcı sayısını üret.

### Business açısından burada ne yapılıyor?

Organizatör tek tek kayıt yerine uluslararası katılım dağılımını görmek ister.
İptal veya onaysız katılımcılar vize ve kapasite planına katılmaz.

## 18. Restoran menüsünde ekonomik seçenekler

`MenuCategory(String name, List<Dish> dishes)` listesinden mevcut ve 20 euro
altındaki yemekleri düzleştir, fiyata göre sırala, ilk üçü atla ve sonraki
beşini listele.

### Business açısından burada ne yapılıyor?

İlk üç ekonomik ürün başka bir promosyon alanında gösterilmektedir. Menü
sayfasındaki ikinci öneri bandı aynı uygunluk ve fiyat sırasındaki sonraki beş
yemeği kullanır.

## 19. Log hata kodu özeti

`LogBatch(LocalDate date, List<LogEntry> entries)` ve `LogEntry(String level,
String code)` verisinden bugünkü `ERROR` kayıtlarını düzleştir; hata kodu başına
adet üret ve en sık ilk üç kodu bul.

### Business açısından burada ne yapılıyor?

Operasyon ekibi ham log satırları yerine bugünün baskın hata türlerini
önceliklendirir. Önce doğru gün ve seviye seçilir, sonra kodlar sayılır ve en
yüksek üç problem çıkarılır.

## 20. Sipariş yönetim dashboard'u

`Order(long id, String status, double total, List<OrderLine> lines)` listesinden
iptal olmayan siparişler için toplam sipariş sayısı, toplam gelir, toplam ürün
adedi, durum başına sayı ve en yüksek tutarlı üç siparişi üret.

### Business açısından burada ne yapılıyor?

Dashboard aynı güvenilir sipariş kümesinden birkaç KPI üretir. İptal kayıtların
bir metrikten çıkarılıp diğerine yanlışlıkla dahil edilmemesi için ortak
business seçimi açıkça tanımlanmalıdır.
