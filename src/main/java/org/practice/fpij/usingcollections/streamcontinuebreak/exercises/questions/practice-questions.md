# Alıştırma Soruları: Stream'de Erken Atlama / Erken Durdurma

Bu klasördeki `skippingvalues` ve `terminatingIterations` örnekleri, geleneksel
döngülerde `continue` ve `break` ile yaptığımız işleri Stream dünyasında
karşılayan dört yapıyı gösteriyor:

- Baştan belirli sayıda kaydı atlama
- Baştan bir şart sağlandığı sürece kaydı atlama
- Sadece belirli sayıda kaydı işleyip durma
- Bir şart sağlandığı sürece işleyip, şart bozulunca hemen durma

Aşağıdaki 10 soru, gerçek bir iş uygulamasında karşınıza çıkabilecek
senaryolar olarak yazıldı. Her senaryoyu okuduğunuzda ihtiyacın ne
olduğunu net şekilde hayal edebilmelisiniz; hangi Stream fonksiyonunun
buna uygun olduğuna kendiniz karar verin ve çözümü kendi dosyanızda
uygulayın.

---

### 1. Sayfalama (Pagination)

Bir e-ticaret sitesinde ürün listesi tarih sırasına göre geliyor.
Kullanıcı 3. sayfayı istedi ve her sayfada 10 ürün gösteriliyor.
Elinizde tüm ürünlerin tam listesi var; kullanıcıya sadece 3. sayfaya
denk gelen 10 ürünü göstermeniz gerekiyor (yani ilk 20 ürünü atlayıp
ondan sonraki 10 tanesini almalısınız).

### 2. Deneme Süresi Biten Abonelikler

Bir SaaS uygulamasında kullanıcılar önce ücretsiz deneme sürecinden
geçiyor, sonra ücretli aboneliğe geçiyor. Elinizde bir kullanıcının
tüm fatura kayıtları, oluşturulma tarihine göre sıralı geliyor ve
deneme süresindeki kayıtların tutarı 0. Raporlama için, deneme
süresine ait sıfır tutarlı kayıtları göz ardı edip, gerçek ücretin
başladığı ilk kayıttan itibaren tüm fatura geçmişini almanız gerekiyor.

### 3. En Yüksek Harcama Yapan Müşteriler

Pazarlama ekibi, bu ay en çok harcama yapan müşterilere özel bir
kampanya e-postası göndermek istiyor ama bütçe sadece 5 kişilik.
Müşteri listeniz harcama tutarına göre büyükten küçüğe sıralı;
sadece en tepedeki 5 müşteriyi seçip e-posta gönderim listesine
eklemeniz gerekiyor.

### 4. Sıcaklık Sensöründe Anomali Öncesi Veriler

Bir soğuk hava deposundaki sensör, dakikada bir sıcaklık ölçümü
kaydediyor. Depo güvenli sıcaklık aralığında olduğu sürece hiçbir şey
yapmanıza gerek yok; ancak sıcaklık ilk kez güvenli eşiğin (örneğin
4°C) üzerine çıktığı anda alarm sürecini tetiklemeniz ve o andan
öncesine ait "her şey normaldi" ölçümlerini raporlamanız gerekiyor.
Yani ölçümleri, sıcaklık eşiği aşana kadar işlemeniz, aştığı anda
durmanız gerekiyor.

### 5. Başarısız Giriş Denemelerinde Hoşgörü Hakkı

Güvenlik politikanıza göre bir kullanıcının art arda ilk 2 başarısız
giriş denemesi normal kabul ediliyor (şifreyi yanlış hatırlamış
olabilir), ama 3. denemeden itibaren her başarısız giriş şüpheli
olay olarak loglanmalı. Elinizde bir kullanıcının bugünkü tüm giriş
denemeleri sırayla var; ilk 2 denemeyi göz ardı edip kalanları
şüpheli olay listesine yazmanız gerekiyor.

### 6. Toplu Baskı Kuyruğunda Yazıcı Kapasitesi

Ofisteki yazıcı, tek seferde en fazla 15 sayfa basabiliyor (kağıt
sıkışmasını önlemek için). Kullanıcı 50 sayfalık bir belge dosyaları
listesi gönderdi; yazdırma işini başlatırken bu kapasiteyi aşmamak
için kuyruktan sadece ilk 15 dosyayı alıp yazıcıya göndermeniz
gerekiyor.

### 7. Kredi Skoru Düşük Geçmiş Başvuruları Atlama

Bir bankanın başvuru sisteminde, geçmişte kredi skoru barajın altında
kaldığı için otomatik reddedilen eski başvurular listenin başında
duruyor (başvuru tarihine göre sıralı). Skorlama politikası
değiştiği için, skoru barajın altında olan bu eski başvuruları
atlayıp, skoru barajı geçen ilk başvurudan itibaren tüm başvuruları
yeniden değerlendirmeye almanız gerekiyor.

### 8. Alışveriş Sepetinde Bütçe Sınırı

Bir kullanıcı sepetine ürünleri eklerken, toplam tutar belirlediği
500 TL'lik bütçeyi aşana kadar ürünleri sepete eklemeye devam ediyor.
Ürün ekleme sırasını (kullanıcının eklediği sıra) koruyarak, toplam
tutar 500 TL'yi aştığı anda sepete ekleme işlemini durdurup, o ana
kadar eklenmiş ürünleri "onaylanan sepet" olarak işaretlemeniz
gerekiyor.

### 9. Vardiya Raporunda İlk Yoğunluk Anına Kadarki Sessiz Dönem

Bir çağrı merkezinde, vardiya başındaki ilk birkaç dakika genelde
sistemlerin ısınma süresi olduğu için çağrı yoğunluğu düşük geçiyor.
Dakika bazlı çağrı sayısı kayıtlarınız sırayla elinizde; çağrı
sayısının belirlenen eşiğin (örneğin 10 çağrı/dakika) altında kaldığı
o ilk "sessiz" dakikaları rapordan çıkarıp, yoğunluğun gerçekten
başladığı ilk dakikadan itibaren tüm veriyi analiz etmeniz gerekiyor.

### 10. Sınırlı Stoklu Kampanyada İlk Katılanlar

"İlk 100 kişiye özel indirim" kampanyası düzenlediniz. Kampanya
sayfasına kayıt olan kullanıcıların listesi kayıt zamanına göre sıralı
elinizde; kampanya kuralına uymak için bu listeden sadece ilk 100
kişiyi alıp onlara indirim kodu göndermeniz, geri kalanlara ise
"kontenjan doldu" bildirimi göndermeniz gerekiyor.
