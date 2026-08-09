# Alıştırma Soruları: Tek Bir Elemanı Güvenle Seçmek

Bu sorular, `pickingelement` paketindeki "koleksiyondan ilk eşleşeni bul,
sonucun olmama ihtimalini güvenle yönet" konusunu gerçek iş senaryolarıyla
pekiştirmek için yazıldı. Sorularda `findFirst`, `Optional` gibi isimler
geçmiyor — her senaryoda "sonuç bulunamazsa ne olmalı?" sorusuna dikkat
edin, çünkü cevabınızı bu belirleyecek.

---

### 1. E-postaya Göre Müşteri Bulma

Destek ekibi, bir e-posta adresine sahip İLK müşteriyi bulup bilgilerini
ekranda göstermek istiyor. Böyle bir müşteri yoksa "Böyle bir müşteri
bulunamadı" mesajı gösterilmeli.

### 2. Barkoda Göre Ürün Bulma

Kasadaki bir cihaz, okuttuğu barkoda sahip İLK ürünü stok listesinde bulup
fiyatını göstermeli. Ürün bulunamazsa fiyat alanı yerine "Ürün tanınmadı"
yazısı gösterilmeli.

### 3. İptal Edilen İlk Siparişi Bildirme

Muhasebe sistemi, günün sipariş listesinde durumu "İptal Edildi" olan İLK
siparişi arıyor. Eğer böyle bir sipariş VARSA, o siparişin müşterisine
otomatik bir iade bildirimi gönderilmeli; yoksa hiçbir şey yapılmamalı
(sistemin başka bir şey yazdırmasına veya hata vermesine gerek yok).

### 4. Öğrenci Numarasına Göre Kayıt Bulma

Bir okul sistemi, verilen öğrenci numarasına sahip öğrenciyi kayıtlı
öğrenci listesinde bulmalı. Bu öğrencinin sistemde MUTLAKA kayıtlı olması
gerekiyor (öğrenci numarası zaten sistemden alınıyor); bulunamazsa bu
durumun ciddi bir veri tutarsızlığı olduğu açıkça, programı durdurarak
bildirilmeli.

### 5. Alerjen İçermeyen Yemek Önerisi

Bir restoran uygulaması, kullanıcının belirttiği alerjeni İÇERMEYEN İLK
yemeği menüden bulup önermeli. Uygun yemek yoksa "Size uygun bir yemek
bulunamadı" mesajı gösterilmeli.

### 6. Yeterli Stoktaki İlk Ürünle Siparişi Karşılama

Bir depo sisteminde aynı üründen birden fazla raf var. Sistem, belirlenen
minimum miktarın ÜZERİNDE stoğu olan İLK rafı bulup siparişi o raftan
karşılamalı. Uygun raf yoksa depo sorumlusuna bilgi verilmeli.

### 7. Yetkinliğe Göre Çalışan Bulma

Proje yöneticisi, "İngilizce" yetkinliğine sahip İLK boştaki çalışanı bulup
yeni bir projeye atamak istiyor. Uygun çalışan yoksa "Uygun çalışan
bulunamadı, dışarıdan destek gerekebilir" notu düşülmeli.

### 8. Acil Destek Talebini Öne Çıkarma

Bir destek panelinde, önceliği "Acil" olan İLK talep varsa ekranın en
üstünde vurgulu gösterilmeli; acil talep yoksa bu alan hiç gösterilmemeli.

### 9. Kayıtlı Kullanıcı Yoksa Misafir Olarak Devam Etme

Bir uygulama, giriş yapan kişiyi e-posta adresine göre kayıtlı kullanıcılar
arasında arıyor. Kayıtlı bulunursa o kullanıcı bilgileriyle devam edilir;
bulunamazsa uygulama, önceden tanımlanmış standart bir "misafir kullanıcı"
profiliyle devam etmeli (hiçbir zaman boş/tanımsız bir kullanıcıyla devam
edilmemeli).

### 10. Uçuş Numarasına Göre Kalkış Saati

Bir havayolu uygulaması, girilen uçuş numarasına sahip uçuşu günün uçuş
listesinde bulup kalkış saatini göstermeli. Böyle bir uçuş yoksa kullanıcıya
"Bu numarayla bir uçuş bulunamadı" bilgisi verilmeli.
