# Alıştırma Soruları: Elemanları Birleştirme (String.join / StringJoiner / Collectors.joining)

Bu sorular `joiningelements` paketindeki `String.join`, `Collectors.joining`
ve `StringJoiner` konusunu gerçek iş senaryolarıyla pekiştirmek için
yazıldı. Sorularda hiçbir yerde bu isimler geçmiyor — hangi aracın uygun
olduğuna kendiniz karar verin.

---

### 1. Rapor Satırı

Bir yönetici, günlük satılan ürünlerin isimlerini tek bir satırda, aralarına
virgül koyarak görmek istiyor. Elinizde günün satılan ürün isimlerinin
listesi var.

### 2. Boş Sepet Mesajı

Bir alışveriş sitesinde sepet özeti gösterilecek: sepette ürün varsa isimleri
köşeli parantez içinde virgülle ayrılmış olarak (`[Elma, Ekmek, Süt]`),
sepet boşsa doğrudan "Sepetiniz boş" yazısı gösterilmeli.

### 3. Toplu Sorgu İfadesi

Bir raporlama aracı, seçilen sipariş numaralarını bir veritabanı sorgusunda
kullanılacak şekilde `(1001, 1002, 1003)` formatında, parantez içinde
virgülle ayrılmış olarak hazırlamak istiyor.

### 4. Parti Referans Kodu

Bir üretim hattında, bir partiye dahil edilen ürün kodları önce büyük harfe
çevrilip sonra aralarına tire konularak tek bir "parti referans kodu"
oluşturulacak (örn. `A100-B200-C300`).

### 5. Dosya Yolu Oluşturma

Bir belge yönetim sisteminde, klasör isimlerinin sırayla verildiği bir liste
var (`"belgeler"`, `"2026"`, `"faturalar"`); bunlardan aralarına `/` konularak
tek bir tam dosya yolu (`belgeler/2026/faturalar`) oluşturulmalı.

### 6. Log Satırı

Bir sistem, her işlem için tarih, kullanıcı adı ve yapılan aksiyonu ayrı ayrı
tutuyor; bunları `" | "` ile ayırarak tek satırlık okunabilir bir log metni
(`2026-08-09 | ayse | LOGIN`) üretmesi gerekiyor.

### 7. Şubeler Arası Liste Birleştirme

İki farklı şube, kendi VIP müşteri isimlerini ayrı ayrı, virgülle ayrılmış
birer liste hâlinde biriktirmiş durumda. Genel merkez bu iki listeyi, aynı
virgül ayracını koruyarak TEK bir listede birleştirmek istiyor.

### 8. Etiket Gösterimi

Bir blog yazısının etiketleri (`"java"`, `"stream"`, `"lambda"`) ekranda
her biri `#` ile başlayacak, aralarına boşluk konularak (`#java #stream
#lambda`) gösterilecek.

### 9. Fazladan Ayraç Sorunu

Bir geliştirici, isimleri virgülle ayırıp yazdırmak için her isimden sonra
`", "` ekleyen bir döngü yazmış; ama çıktının sonunda istenmeyen fazladan bir
virgül kalıyor (`Ali, Veli, Ayşe, `). Bu sorunu çözmeniz isteniyor.

### 10. Kargo Takip Kodu

Bir kargo firması, takip kodunu üç parçadan (ülke kodu, yıl, sıra numarası)
aralarına tire koyarak (`TR-2026-004521`) oluşturuyor.
