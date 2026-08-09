# Alıştırma Soruları: Consumer<T> ve forEach()

Bu klasördeki sorular, `consumerforeach` paketinde gördüğünüz `Consumer<T>`,
`accept()` ve `forEach()` konusunu gerçek iş senaryolarıyla pekiştirmek için
yazıldı. Sorularda hiçbir yerde `Consumer`, `accept`, `forEach`, `Function`
gibi isimler geçmiyor — her senaryoyu okuduğunuzda ihtiyacın ne olduğunu net
şekilde hayal edip hangi aracın uygun olduğuna kendiniz karar vermelisiniz.

Not: Bazı sorular BİLE İSTEYE bu konunun sınırlarını zorluyor — yani her
senaryo mutlaka bu paketteki araçlarla çözülmeyebilir. Bu, "her problem aynı
çekiçle çözülmez" ayrımını fark etmeniz için bilinçli bir tuzak.

---

### 1. Sipariş Onay Bildirimi

Bir e-ticaret sisteminde, gün sonunda o gün oluşturulan siparişlerin hepsi
için müşterilere "Siparişiniz hazırlanıyor" bildirimi ekrana (konsola)
yazdırılacak. Elinizde günün sipariş listesi var; bildirim her sipariş için
ayrı ayrı gösterilmeli, herhangi bir sonuç/liste geri dönmesine gerek yok.

### 2. Tek Müşteriye Karşılama Mesajı

Bir üyelik sistemine yeni kayıt olan TEK bir kullanıcıya, kayıt işlemi
tamamlandığı anda "Hoş geldin" mesajı gösterilmesi gerekiyor. Bu davranış bir
listeye değil, doğrudan o TEK kullanıcıya uygulanmalı.

### 3. Kampanya Kapsamında Fiyat Güncelleme

Bir mağaza indirim kampanyası başlatıyor: depodaki ürün listesindeki her
ürünün mevcut fiyatını, ürünü yeni bir nesneye dönüştürmeden, doğrudan
kendi üzerinde güncelleyerek %15 indirimli hale getirmek istiyor. Ürün
nesnelerinin fiyatını değiştirebilen bir metodu (setter) var.

### 4. Düşük Stok Uyarı Kaydı

Bir depo yönetim sistemi, envanterdeki her üründen stok miktarı belirlenen
kritik eşiğin (örneğin 5 adet) altındaysa konsola bir uyarı satırı
yazdırmak, eşiğin üzerindeyse hiçbir şey yapmamak istiyor. İşlem sonunda
herhangi bir sonuç değeri beklenmiyor, sadece ekrana yazdırma var.

### 5. Sepet Davranışının Hem Tekil Hem Toplu Çalıştırılması

Bir alışveriş sitesinde "sepete eklenen ürünü stoktan düş ve işlemi
logla" davranışı hem bir müşteri anlık sepete tek bir ürün eklediğinde O
ÜRÜN için, hem de gün sonunda bekleyen tüm sepetlerdeki ürünler için TOPLU
olarak çalıştırılabilmeli. Aynı davranışın kodu iki yerde ayrı ayrı
tekrar yazılmamalı; bir kere tanımlanıp her iki durumda da kullanılmalı.

### 6. Fatura KDV Hesaplama Raporu

Muhasebe departmanı, bu ayki tüm faturalardan KDV dahil tutarları
hesaplattırıp bu tutarların yer aldığı YENİ bir liste istiyor; bu yeni
listeyi ayrı bir rapor motoruna göndermeyi planlıyorlar. Mevcut fatura
nesnelerinin kendisi hiç değişmeyecek, sadece hesaplanan tutarlardan oluşan
taze bir sonuç listesi lazım.

### 7. Bildirimleri Okundu Olarak İşaretleme

Bir mobil uygulamada kullanıcı "Tümünü okundu işaretle" butonuna bastığında,
bildirim listesindeki her bildirim nesnesinin kendi üzerinde zaten var olan
"okundu işaretle" davranışının çalıştırılması gerekiyor (her bildirim kendi
okunma durumunu biliyor ve güncelleyebiliyor). Yeni bir bildirim listesi
üretilmeyecek, mevcut bildirimler güncellenecek.

### 8. Şüpheli İşlem Listesi Biriktirme

Bir denetim ekibi, işlem kayıtları listesinden tutarı 10.000 TL'yi aşan
işlemleri ayrı bir "şüpheli işlemler" listesinde toplamak istiyor. Bu yeni
listeyi daha sonra ayrı bir dosyaya yazacaklar. Ekip bunu "listedeki her
kayda bak, şartı sağlıyorsa dışarıdaki başka bir listeye ekle" şeklinde,
tek bir dolaşma içinde çözmeyi düşünüyor.
