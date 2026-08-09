# Alıştırma Soruları: Lambda İfadelerini Yeniden Kullanma

Bu sorular, `reusinglambdas` paketindeki "aynı kontrolü birden fazla
koleksiyonda tekrar tekrar yazmak yerine bir kere tanımlayıp paylaşma"
fikrini gerçek iş senaryolarıyla pekiştirmek için yazıldı. Her senaryoda
BİRDEN FAZLA liste üzerinde AYNI kontrolün kullanılması gerekiyor —
mantığı bir kere yazıp nasıl paylaşacağınıza siz karar verin.

---

### 1. Aktif Müşteri Sayımı

Şirketin VIP, standart ve kurumsal olmak üzere üç ayrı müşteri listesi var.
Her listede "hesabı aktif olan" müşteri sayısını bulmanız gerekiyor;
aktiflik kontrolü üç listede de aynı.

### 2. Stokta Olan Ürünleri Filtreleme

Elektronik, giyim ve kitap kategorilerine ait üç ayrı ürün listesi var. Her
kategoride "stokta olan" (miktarı sıfırdan büyük) ürünleri filtrelemeniz
gerekiyor; stok kontrolü üç kategoride de aynı.

### 3. Kıdemli Çalışanları Bulma

İstanbul, Ankara ve İzmir ofislerinde ayrı ayrı tutulan çalışan listeleri
var. Her ofiste "5 yıldan fazla kıdemi olan" çalışanları bulmanız
gerekiyor; kıdem kontrolü üç ofiste de aynı.

### 4. Sınıfı Geçen Öğrencileri Sayma

Bir okulda üç farklı sınıfın (10-A, 10-B, 10-C) not listeleri var. Her
sınıfta "geçme notunun üzerinde olan" öğrenci sayısını bulmanız gerekiyor;
geçme kontrolü üç sınıfta da aynı.

### 5. Süresi Geçmiş Ürünleri Bulma

İki farklı depoda (merkez depo, şube deposu) tutulan ürün listeleri var.
Her depoda "son kullanma tarihi geçmiş" ürünleri bulmanız gerekiyor; tarih
kontrolü iki depoda da aynı.

### 6. Uzaktan Çalışanları Filtreleme

Frontend, backend ve QA ekiplerinin ayrı ayrı çalışan listeleri var. Her
ekipte "uzaktan çalışan" kişileri filtrelemeniz gerekiyor; kontrol mantığı
üç ekipte de aynı.

### 7. İade Edilen Siparişleri Sayma

Bu haftanın ve geçen haftanın sipariş listeleri ayrı ayrı tutuluyor. Her
iki listede de "iade edilmiş" sipariş sayısını bulmanız gerekiyor; iade
kontrolü ikisinde de aynı.

### 8. Çözülmemiş Şikayetleri Bulma

İki farklı şubenin müşteri şikayeti listeleri var. Her şubede "henüz
çözülmemiş" şikayetleri bulmanız gerekiyor; kontrol mantığı ikisinde de
aynı.

### 9. Dolu Seferleri Bulma

Sabah ve akşam seferlerinin ayrı ayrı yolcu listeleri (her sefer için
doluluk bilgisiyle) var. Her iki sefer grubunda da "kapasitesi dolmuş"
seferleri bulmanız gerekiyor; doluluk kontrolü ikisinde de aynı.

### 10. Değerlendirmesi Tamamlanmamış Çalışanları Sayma

Satış ve pazarlama departmanlarının ayrı ayrı çalışan listeleri var. Her
departmanda "performans değerlendirmesi tamamlanmamış" çalışan sayısını
bulmanız gerekiyor; kontrol mantığı iki departmanda da aynı.
