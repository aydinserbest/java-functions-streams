# Stream `flatMap()` Alıştırmaları — Requirement'lar

`flatMap()` her elemandan oluşan iç akışları tek, düz bir Stream'de birleştirir.
Bu sorularda `map()` ile oluşacak iç içe yapı ile `flatMap()` sonucu arasındaki
farkı özellikle gözlemle.

## 1. Müşterilerin bütün telefonlarını arama listesine çıkarma

`Customer(String name, List<String> phoneNumbers)` listesinden bütün telefon
numaralarını tek bir `List<String>` olarak üret.

### Business açısından burada ne yapılıyor?

Çağrı merkezi müşteri kartlarıyla değil, aranacak düz telefon listesiyle
çalışır. Her müşterinin sıfır, bir veya birden fazla telefonu olabilir; iç
listeler tek arama kuyruğunda birleştirilir.

## 2. Siparişlerdeki bütün ürünleri sevkiyat listesine alma

`Order(long id, List<OrderLine> lines)` ve `OrderLine(String product, int
quantity)` modellerini oluştur. Bütün sipariş satırlarını tek listeye indir.

### Business açısından burada ne yapılıyor?

Depo ekranı siparişleri ayrı kartlar halinde değil, hazırlanacak bütün satırları
ortak bir iş kuyruğunda görmek ister. `flatMap()` sipariş sınırlarını kaldırır,
satırların bilgisini değiştirmez.

## 3. Departmanlardaki çalışan e-postalarını birleştirme

`Department(String name, List<Employee> employees)` ve `Employee(String name,
String email)` modellerinden bütün e-postaları tek listeye çıkar.

### Business açısından burada ne yapılıyor?

İnsan kaynakları şirket çapında duyuru gönderecektir. Çalışanlar departman
altında gruplu saklansa da e-posta servisi düz bir alıcı listesi bekler.

## 4. Cümlelerden benzersiz kelime indeksi oluşturma

Bir cümle listesindeki metinleri boşluğa göre ayır, kelimeleri küçük harfe çevir
ve tek bir benzersiz kelime kümesinde topla.

### Business açısından burada ne yapılıyor?

Arama servisi dokümanları hızlı bulmak için cümlelerden kelime indeksi çıkarır.
Her cümleden bir kelime dizisi oluşur; diziler tek kelime akışında
düzleştirilir.

## 5. Öğrencilerin aldığı dersleri raporlama

`Student(String name, List<String> courses)` listesinden bütün ders kayıtlarını
tek listeye al. Aynı ders farklı öğrencilerde tekrar edebilir; önce tekrarları
koru, ardından `distinct()` eklenmiş ikinci sonucu üret.

### Business açısından burada ne yapılıyor?

İlk sonuç toplam öğrenci-ders kayıtlarını, ikinci sonuç okulda açılan farklı
dersleri temsil eder. Raporların business anlamı farklı olduğu için tekrarların
ne zaman kaldırıldığı önemlidir.

## 6. Projelerdeki açık görevleri tek panoda gösterme

`Project(String name, List<Task> tasks)` ve `Task(String title, boolean
completed)` modellerini oluştur. Bütün projelerdeki tamamlanmamış görevleri tek
listeye çıkar.

### Business açısından burada ne yapılıyor?

Yönetici proje proje dolaşmadan şirketin açık iş yükünü görmek ister. Önce her
projenin görev listesi açılır, sonra bitmiş görevler ortak akıştan çıkarılır.

## 7. Mağazalardaki stok kodlarını merkez sisteme aktarma

`Store(String city, List<String> productSkus)` listesinden bütün SKU değerlerini
tek listeye al ve tekrarları kaldır.

### Business açısından burada ne yapılıyor?

Aynı ürün birden fazla mağazada bulunabilir. Merkez ürün kataloğu, mağaza
yerleşiminden bağımsız olarak sistemde bulunan benzersiz ürün kodlarını ister.

## 8. Kullanıcı rollerinden izin listesi üretme

`User(String username, List<Role> roles)` ve `Role(String name, List<String>
permissions)` modelleri oluştur. Belirli bir kullanıcının bütün rollerindeki
izinleri düzleştirip benzersiz liste üret.

### Business açısından burada ne yapılıyor?

Bir kullanıcı hem `EDITOR` hem `REPORTER` olabilir. Yetkilendirme servisi rol
listelerini değil, kullanıcının efektif izinlerini kontrol eder; ortak izinler
yalnızca bir kez tutulur.

## 9. API sayfalarındaki sonuçları birleştirme

`Page(int number, List<Product> content)` listesinden bütün `content`
listelerini tek ürün listesine dönüştür. Boş sayfalar hata oluşturmamalıdır.

### Business açısından burada ne yapılıyor?

Dış servis sonuçları sayfalı döndürür, fakat dışa aktarma işlemi bütün ürünleri
tek dosyaya yazacaktır. Sayfa katmanı kaldırılarak birleşik veri kümesi
hazırlanır.

## 10. Kategoriler altındaki alt kategorileri iki seviyede açma

`Category(String name, List<Category> children)` modeliyle kök kategorilerin
çocuklarını ve çocukların çocuklarını tek listede topla. Bu alıştırmada yalnızca
iki seviye işlenecektir.

### Business açısından burada ne yapılıyor?

Menü yönetim ekranı iki alt seviyedeki kategorileri toplu seçicide göstermek
ister. Her seviye yeni bir iç liste ürettiği için iki ayrı `flatMap()` adımıyla
hiyerarşinin istenen kısmı düzleştirilir.
