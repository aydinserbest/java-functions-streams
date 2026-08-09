# Alıştırma Soruları: Lexical Scoping ve Closure'lar

Bu sorular, `lexicalscopingandclosures` paketindeki "bir değeri parametre
alıp, o değeri hatırlayan bir davranış (Predicate/Function) üreten
fonksiyon" fikrini gerçek iş senaryolarıyla pekiştirmek için yazıldı.
Sorularda `Predicate`, `Function`, `closure` gibi isimler geçmiyor — her
senaryoda "aynı kontrolü farklı bir değerle tekrar tekrar yazmadan nasıl
üretirim?" sorusuna kendiniz cevap verin.

---

### 1. Şehre Göre Müşteri Kontrolü

Bir kargo firması, İstanbul, Ankara ve İzmir için ayrı ayrı "bu müşteri bu
şehirde mi?" kontrolüne ihtiyaç duyuyor. Kontrol mantığı üç şehir için de
birebir aynı (müşterinin şehir alanını karşılaştırmak); sadece karşılaştırılan
şehir adı değişiyor. Aynı kontrol mantığını üç kez yazmadan, istenen şehre
göre hazır bir kontrol üretebilmeniz gerekiyor.

### 2. Sipariş Tutarı Eşiği

Pazarlama ekibi "büyük sipariş" tanımını kampanyaya göre değiştiriyor: bazen
100 TL, bazen 500 TL, bazen 1000 TL üzeri siparişler "büyük" sayılıyor.
Her kampanya için ayrı bir eşik değerine göre çalışan bir "büyük sipariş mi"
kontrolü üretmeniz gerekiyor, kontrol mantığının kendisi hep aynı.

### 3. KDV Dahil Fiyat Hesaplama

Farklı ürün kategorilerinin farklı KDV oranları var (%1, %10, %20). Önce
hangi oranın kullanılacağını belirtip, sonra o orana göre herhangi bir
fiyattan KDV dahil tutarı hesaplayabilen bir yapı istiyorsunuz; oran
belirlendikten sonra elinizde "artık sadece fiyat vererek KDV'li tutarı
hesaplatabileceğiniz" hazır bir hesaplayıcı olmalı.

### 4. Müşteri Tipine Göre İndirim

Öğrenciye %10, çalışana %20, VIP üyeye %30 indirim uygulanıyor. Her müşteri
tipi için ayrı bir indirim hesaplayıcı üretmeniz gerekiyor; indirim mantığı
(fiyattan yüzdeyi düşmek) hepsinde aynı, sadece yüzde farklı.

### 5. Kategoriye Göre Ürün Filtreleme

Bir mağaza uygulamasında kullanıcı "Elektronik", "Giyim" veya "Kitap"
kategorisini seçtiğinde, ürün listesi o kategoriye göre filtrelenmeli.
Kategori adı her seferinde değişse de filtreleme mantığı aynı.

### 6. Geçme Notuna Göre Öğrenci Ayırma

Bir okulda farklı sınavlar için farklı geçme notları var (bazı sınavlarda
50, bazılarında 60, bazılarında 70). Sınavın geçme notu belirlendikten
sonra, herhangi bir öğrenci listesini bu nota göre (geçti/kaldı diye)
ayırabilen bir kontrol üretmeniz gerekiyor.

### 7. Kurumsal E-posta Domainine Göre Kullanıcı Ayırma

Sisteme farklı şirketlerden (ör. `@firma1.com`, `@firma2.com`) kullanıcılar
kayıt oluyor. Belirli bir şirketin e-posta uzantısına sahip kullanıcıları
bulmak için, şirket domaini verildiğinde o domaine göre çalışan bir kontrol
üretmeniz gerekiyor.

### 8. Para Birimine Göre Tutar Gösterimi

Uygulama TL, USD ve EUR için tutarları farklı sembollerle göstermek
istiyor (`"₺150"`, `"$150"`, `"€150"`). Para birimi sembolü belirlendikten
sonra, herhangi bir tutarı bu sembolle biçimlendirebilen bir yapı
üretmeniz gerekiyor.

### 9. Yöneticiye Göre Ekip Filtreleme

İK sistemi, herhangi bir yöneticinin adı verildiğinde o yöneticiye bağlı
çalışanları bir çalışan listesinden filtreleyebilmeli. Yönetici adı her
sorguda değişiyor, filtreleme mantığı aynı kalıyor.

### 10. Kayıt Yılına Göre Kullanıcı Filtreleme

Sistem yöneticisi zaman zaman "2023'ten sonra kayıt olanlar", zaman zaman
"2024'ten sonra kayıt olanlar" diye farklı yıllara göre kullanıcı filtrelemek
istiyor. Yıl bilgisi dışarıdan verildiğinde, o yıldan sonraki kayıtları
bulan bir kontrol üretmeniz gerekiyor; aynı kontrolü her yıl için ayrı ayrı
elle yazmak istemiyorsunuz.
