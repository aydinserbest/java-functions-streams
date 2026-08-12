# Alıştırma Soruları: Comparator ile Sıralama ve En Uç Elemanı Bulma

Bu sorular, `comparatorinterface` paketindeki "bir koleksiyonu bir kurala
göre sıralama, bu kuralı bozmadan yeniden kullanma, ya da doğrudan tek bir
uç elemanı bulma" konusunu gerçek iş senaryolarıyla pekiştirmek için
yazıldı. Sorularda `sorted`, `Comparator`, `reversed`, `min`, `max` gibi
isimler geçmiyor — her senaryoda TAM OLARAK ne isteniyor (sıralı bir liste
mi, sadece bir uç eleman mı, orijinal veri korunmalı mı) dikkat edin.

---

### 1. Ürünleri Fiyata Göre Listeleme

Bir e-ticaret sayfası, ürün listesini fiyata göre ARTAN sırada
göstermeli. Ürünlerin orijinal (veritabanından gelen) sırası, başka bir
ekranda hâlâ kullanıldığı için BOZULMAMALI.

### 2. Aynı Listeyi Pahalıdan Ucuza Gösterme

Bir önceki ekranın "filtre" seçeneğinde kullanıcı isterse aynı ürün
listesini fiyata göre AZALAN sırada da görebilmeli. Artan sıralama
kuralını YENİDEN YAZMADAN, var olan kuralın tersini elde etmeniz
isteniyor.

### 3. Çalışanları Soyadına Göre Sıralama

İnsan kaynakları sistemi, çalışan listesini soyadına göre alfabetik
(A'dan Z'ye) sıralı göstermek istiyor.

### 4. En Ucuz Ürünü Bulma

Bir karşılaştırma sitesi, aynı kategorideki ürünler arasından SADECE en
ucuz olanı bulup öne çıkarmak istiyor; tüm listeyi sıralamaya gerek yok.

### 5. En Yüksek Tutarlı Siparişi Bulma

Muhasebe ekibi, günün sipariş listesinden tutarı en YÜKSEK olan tek
siparişi bulup ayrıca incelemek istiyor.

### 6. Uçuşları Süreye Göre İki Farklı Ekranda Gösterme

Bir uçuş arama sonucu ekranı, aynı uçuş listesini hem "en kısa süreli
önce" hem de "en uzun süreli önce" şeklinde iki ayrı sekmede
gösterebilmeli. Süre karşılaştırma mantığını İKİ KERE yazmadan bu iki
sıralamayı elde etmeniz bekleniyor.

### 7. Oyun Skor Tablosu (Leaderboard)

Bir oyun, oyuncuları skora göre en YÜKSEKTEN en DÜŞÜĞE sıralı bir
liderlik tablosu göstermeli.

### 8. Kitapları Yayın Yılına Göre Sıralama

Bir kütüphane kataloğu, kitapları yayın yılına göre ESKİDEN YENİYE sıralı
listelemeli.

### 9. En Erken Randevuyu Bulma

Bir klinik randevu sistemi, günün randevu listesinden SADECE en erken
saatli olanı bulup ekranın üstünde göstermeli. O gün hiç randevu
olmayabileceği için bu durumu da güvenle yönetmesi gerekiyor.

### 10. En Yüksek Stoklu Ürünü Bulma

Bir depo yönetim paneli, bir ürün kategorisindeki raflar arasından
SADECE en yüksek stok miktarına sahip rafı bulup göstermeli; böyle bir
raf yoksa (kategori boşsa) ekranda hiçbir şey gösterilmemeli.
