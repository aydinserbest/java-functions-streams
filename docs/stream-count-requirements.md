# Stream `count()` Alıştırmaları — Requirement'lar

`count()` akışta kalan eleman sayısını `long` olarak döndüren terminal
operation'dır. Yalnızca mevcut bir `Collection` boyutu isteniyorsa `size()`,
pipeline sonrasında kalanların sayısı isteniyorsa `count()` tercih edilir.

## 1. Aktif kullanıcı sayısını panelde gösterme

`User(String username, boolean active)` listesinden aktif kullanıcıların sayısını
hesapla.

### Business açısından burada ne yapılıyor?

Yönetim panelindeki gösterge bütün kayıtların değil, şu an hizmete erişebilen
hesapların adedini gösterir. Önce business koşulu uygulanır, sonra kalan kayıtlar
sayılır.

## 2. Stoku tükenen ürün sayısını bulma

`Product(String name, int stock)` listesinden `stock == 0` olanların sayısını
`long` değişkene al.

### Business açısından burada ne yapılıyor?

Satın alma ekibi ürün listesini okumadan kaç farklı ürün çeşidinin tükendiğini
özet kartta görür. Negatif stoklu hatalı kayıtlar bu tanıma dahil değildir.

## 3. Bugün oluşturulan siparişleri sayma

`Order(long id, LocalDateTime createdAt)` listesinden tarihi bugün olan
siparişlerin sayısını bul. `LocalDate today` dışarıdan verilsin.

### Business açısından burada ne yapılıyor?

Operasyon ekranı günün sipariş hacmini gösterir. Saat bilgisi farklı olsa da aynı
takvim günündeki kayıtlar birlikte sayılır.

## 4. Belirli şehirdeki premium müşterileri sayma

`Customer(String name, String city, boolean premium)` listesinden Amsterdam'da
yaşayan premium müşterilerin sayısını bul.

### Business açısından burada ne yapılıyor?

Pazarlama ekibi yerel premium etkinliğin potansiyel katılımcı hacmini ölçer. İki
koşulu sağlamayan müşteriler sayıya katılmaz.

## 5. Siparişlerdeki toplam satır sayısını bulma

`Order(long id, List<OrderLine> lines)` listesindeki iç listeleri `flatMap()` ile
aç ve toplam sipariş satırı sayısını `count()` ile bul.

### Business açısından burada ne yapılıyor?

Depo iş yükü yalnızca sipariş adediyle ölçülmez; her sipariş birden fazla
hazırlama satırı içerebilir. İç listeler düzleştirilip gerçek satır hacmi sayılır.

## 6. Benzersiz ziyaretçi sayısını hesaplama

Ziyaret loglarındaki kullanıcı ID'lerini `distinct()` sonrasında say.

### Business açısından burada ne yapılıyor?

Bir kullanıcı aynı gün on kez sayfa açsa da günlük tekil ziyaretçi metriğine bir
kez katkıda bulunur. Ham olay sayısı ile benzersiz kullanıcı sayısı ayrılır.

## 7. Geçerli e-posta sayısını bulma

String listesinden `null` olmayan ve `@` içeren adresleri filtreleyip say.

### Business açısından burada ne yapılıyor?

İçe aktarma ön izlemesi, kaydetme başlamadan önce kaç satırın temel e-posta
kontrolünü geçtiğini gösterir. Bu sayı kullanıcıya veri kalitesi hakkında hızlı
geri bildirim verir.

## 8. Puan aralığındaki öğrencileri sayma

`Student(String name, int grade)` listesinden notu 70–100 arasında olan öğrenci
sayısını bul. Sınırlar dahil olmalıdır.

### Business açısından burada ne yapılıyor?

Eğitim raporu başarı kriterini karşılayan öğrenci sayısını ölçer. 70 sınırındaki
öğrenci de başarılı kabul edilir, 100 üzerindeki bozuk veri sayılmaz.

## 9. `count()` ile `Collectors.counting()` karşılaştırması

Aynı tamamlanmamış görev listesini bir kez doğrudan `count()`, bir kez
`collect(Collectors.counting())` ile say ve sonuçların eşitliğini göster.

### Business açısından burada ne yapılıyor?

Tek bir toplam sayı için `count()` sadedir. Daha sonra görevleri projeye göre
gruplayıp her grup içinde saymak gerektiğinde `Collectors.counting()` downstream
collector olarak kullanılabilir.

## 10. `size()` mı `count()` mı?

Bir ürün listesinin toplam kayıt sayısını hem `products.size()` hem
`products.stream().count()` ile bul. Ardından yalnızca fiyatı 100 eurodan yüksek
ürünlerin sayısını hesapla ve hangi durumda hangi yaklaşımın uygun olduğunu yaz.

### Business açısından burada ne yapılıyor?

Katalog toplamı doğrudan koleksiyonun bildiği bir bilgidir. Pahalı ürün metriği
ise business filtresinden sonra oluşur; bu nedenle Stream pipeline'ının sonunda
sayılır.

