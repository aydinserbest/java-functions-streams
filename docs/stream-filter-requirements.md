# Stream `filter()` Alıştırmaları — Requirement'lar

`filter()` bir `Predicate<T>` alır ve yalnızca `test()` sonucu `true` olan
elemanları akışta bırakır. Aşağıdaki çalışmaların her birinde kaynak koleksiyon
değişmemeli; sonuç yeni bir listeye alınmalıdır.

## 1. Satışa açık ürünleri katalogda gösterme

`Product(String name, boolean active, int stock)` modeliyle en az beş ürün
oluştur. Katalog ekranı için yalnızca `active=true` ve `stock>0` olan ürünleri
filtrele. Örnek olarak aktif ve stoklu `Mouse` sonuçta bulunmalı; pasif `Monitor`
ve stoku sıfır `Keyboard` bulunmamalıdır.

### Business açısından burada ne yapılıyor?

Veritabanında duran her ürün müşteriye satılamaz. Ürün yönetici tarafından
yayından kaldırılmış veya geçici olarak tükenmiş olabilir. Katalog servisi
kayıtları silmeden, yalnızca o an satın alınabilir ürünleri ekrana gönderir.

## 2. Ödenmemiş yüksek tutarlı faturaları bulma

`Invoice(long id, boolean paid, double amount)` listesinden ödenmemiş ve tutarı
500 euroyu aşan faturaları seç. İki ayrı `Predicate<Invoice>` tanımlayıp `and()`
ile birleştir.

### Business açısından burada ne yapılıyor?

Finans ekibi tahsilat riski yüksek kayıtları takip etmek istiyor. Ödenmiş
faturalar aksiyon gerektirmez; düşük tutarlar da bu özel raporun dışında kalır.
Filtre sonucundaki faturalar müşteriye hatırlatma gönderilecek adaylardır.

## 3. Teslimata hazır siparişleri seçme

`Order(long id, String status, boolean addressVerified)` modeli oluştur. Durumu
`PAID` olan ve adresi doğrulanmış siparişleri filtrele. Durum karşılaştırmasında
`"PAID".equals(order.status())` kullan.

### Business açısından burada ne yapılıyor?

Depo, ödeme tamamlanmadan veya teslimat adresi doğrulanmadan paket hazırlamamalı.
Filtre iki operasyonel güvenlik kuralını uygular; başarısız kayıtları silmez,
yalnızca sevkiyat kuyruğuna girerken dışarıda bırakır.

## 4. Yaş ve şehir kuralına uyan etkinlik katılımcıları

`Participant(String name, int age, String city)` listesinden yaşı en az 18 olan
ve Amsterdam'da yaşayan kişileri seç. Beklenen sonucu isimleriyle yazdır.

### Business açısından burada ne yapılıyor?

Amsterdam'da düzenlenen yetişkinlere özel bir etkinliğin kayıt sistemi,
şehir dışındaki veya yaş sınırını karşılamayan başvuruları bu etkinliğin katılımcı
listesine dahil etmez. Kişilerin ana kayıtları değişmeden kalır.

## 5. Geçerli e-posta adreslerini kampanya listesine alma

Bir `List<String>` içinden `null` olmayan, boş olmayan, boşluk içermeyen ve
`@` işaretine sahip e-posta adreslerini filtrele. Predicate null-safe olmalıdır.

### Business açısından burada ne yapılıyor?

Pazarlama servisi hatalı adreslere gönderim yaparak teslimat oranını düşürmek
istemez. Bu basit ön kontrol, teknik olarak gönderime aday değerleri ayırır.
Gerçek doğrulama bağlantısı göndermek ayrı bir süreçtir.

## 6. Kritik stok seviyesindeki ürünleri raporlama

`StockItem(String sku, int stock, int reorderLevel)` modeli oluştur. Stoku sıfır
veya daha büyük olan, ayrıca `stock <= reorderLevel` koşulunu sağlayan ürünleri
filtrele. Negatif stoklu bozuk kayıtlar rapora alınmamalıdır.

### Business açısından burada ne yapılıyor?

Satın alma ekibi hangi ürünler için yeni sipariş açacağını belirler. Yeniden
sipariş seviyesi ürüne göre değişir; bu yüzden sabit bir sayı yerine her kaydın
`reorderLevel` alanı kullanılır. Negatif stoklar önce veri düzeltme sürecine
gitmelidir.

## 7. Uygun iş ilanlarını adaya gösterme

`Job(String title, boolean remote, int minimumExperience)` listesinden uzaktan
çalışmaya açık ve en fazla 3 yıl deneyim isteyen ilanları seç.

### Business açısından burada ne yapılıyor?

Üç yıllık deneyimi bulunan ve yalnızca uzaktan iş arayan adayın ekranında,
başvuramayacağı ilanlar gösterilmez. Filtre ilanı değiştirmez; adayın tercihine
uygun görünüm üretir.

## 8. Tarihi geçmemiş kuponları seçme

`Coupon(String code, LocalDate expiryDate, boolean enabled)` modeli oluştur.
Bugünün tarihini metoda parametre olarak al ve etkin, son kullanma tarihi bugün
veya daha sonra olan kuponları filtrele.

### Business açısından burada ne yapılıyor?

Ödeme ekranı müşteriye artık kullanılamayan kampanya kodlarını sunmamalıdır.
Kupon hem yönetici tarafından etkin bırakılmış olmalı hem de kullanım süresi
dolmamış olmalıdır.

## 9. Moderasyon bekleyen yorumları bulma

`Review(long id, String text, boolean approved, boolean flagged)` listesinden
onaylanmamış veya işaretlenmiş yorumları seç. `or()` ile iki Predicate'i
birleştir.

### Business açısından burada ne yapılıyor?

Moderasyon ekibi iki nedenle inceleme yapar: yorum henüz hiç onaylanmamıştır veya
şikâyet sistemi tarafından riskli işaretlenmiştir. Bu koşullardan birinin
sağlanması inceleme kuyruğuna girmek için yeterlidir.

## 10. Tekrar kullanılabilir müşteri araması

`Customer(String name, String country, boolean premium, double balance)` modeli
ve aşağıdaki metodu oluştur:

```java
List<Customer> search(List<Customer> customers, Predicate<Customer> criteria)
```

Aynı metotla Hollanda'daki müşterileri, premium müşterileri ve bakiyesi 1000
eurodan yüksek premium müşterileri ayrı ayrı bul.

### Business açısından burada ne yapılıyor?

CRM ekranında filtre seçenekleri değişse de listeyi dolaşma algoritması aynıdır.
Arama metodu business kuralını bilmez; kullanıcı ekranında seçilen kriter
`Predicate<Customer>` olarak dışarıdan verilir.

