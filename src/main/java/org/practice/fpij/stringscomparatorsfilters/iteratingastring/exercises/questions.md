# Alıştırma Soruları: Bir Metni Karakter Karakter İşlemek

Bu sorular, `iteratingastring` paketindeki "bir String'in karakterlerini
tek tek gezip süzme/sayma/dönüştürme" konusunu gerçek iş senaryolarıyla
pekiştirmek için yazıldı. Sorularda `chars`, `filter`, `mapToObj` gibi isim
verilmiyor — her senaryoda hangi karakterlerin arandığına ve sonucun ne
şekilde kullanılacağına (evet/hayır mı, sayı mı, süzülmüş bir metin mi)
dikkat edin.

---

### 1. PIN Doğrulama

Bir mobil bankacılık uygulaması, kullanıcının girdiği 4 haneli PIN'in
SADECE rakamlardan oluştuğunu doğrulamalı. İçinde rakam olmayan tek bir
karakter bile varsa PIN reddedilmeli.

### 2. Şifre Güç Kontrolü

Kayıt formu, girilen şifrenin İÇİNDE EN AZ 2 rakam bulunmasını şart
koşuyor. Sistem, şifredeki rakam SAYISINI bulup bu şartı sağlayıp
sağlamadığını kontrol etmeli.

### 3. Kategori Kodu Raporu

Bir ürün kodu harflerle rakamların karışımından oluşuyor (örn. "TX204A").
Raporlama modülü, bu koddan SADECE harfleri süzüp yeni bir metin olarak
göstermeli (rakamlar rapor çıktısında görünmemeli).

### 4. Telefon Numarasını Normalleştirme

Kullanıcıların bazen boşluklu girdiği bir telefon numarasını ("0532 123
45 67"), sistem veritabanına kaydetmeden önce içindeki TÜM boşluk
karakterlerinden arındırıp tek parça bir metne çevirmeli.

### 5. Bağırarak Yazılmış Başlığı Tespit Etme

Bir destek talebi başlığındaki BÜYÜK HARF sayısı belirli bir eşiği (örn.
5) aşarsa, sistem bu başlığı "muhtemelen bağırarak yazılmış" diye
işaretlemeli.

### 6. Başlık İçin Sesli Harf Sayacı

Bir SEO aracı, girilen blog başlığındaki sesli harflerin (a, e, i, o, u)
SAYISINI hesaplayıp, bu sayı belli bir minimumun altındaysa başlığı
"okunması zor olabilir" diye uyarmalı.

### 7. Zayıf Güvenlik Sorusu Cevabını Tespit Etme

Bir güvenlik sorusu cevabı içinde HİÇBİR rakam BULUNMAMALI (rakam içeren
cevaplar tahmin edilmesi kolay kabul ediliyor). Sistem, cevapta rakam olup
olmadığını kontrol edip varsa cevabı "zayıf" diye işaretlemeli.

### 8. Seri Numarası Format Doğrulama

Bir cihazın seri numarasının TAM OLARAK 6 rakam içermesi gerekiyor (harf
sayısı önemli değil). Sistem, verilen seri numarasındaki rakam sayısını
bulup bu 6 rakam şartını karşılayıp karşılamadığını doğrulamalı.

### 9. Token'ı Log İçin Okunabilir Yazdırma

Bir hata ayıklama aracı, sistemin ürettiği bir erişim token'ının her
karakterini, log ekranında ALT ALTA okunabilir tek tek harfler/semboller
olarak göstermeli (sayısal kodlar değil, gerçek karakterler).

### 10. Boşluksuz Kullanıcı Adı Kontrolü

Bir kayıt formu, girilen kullanıcı adının İÇİNDE HİÇBİR boşluk karakteri
OLMADIĞINI doğrulamalı; herhangi bir boşluk bulunursa kullanıcı adı
reddedilmeli.
