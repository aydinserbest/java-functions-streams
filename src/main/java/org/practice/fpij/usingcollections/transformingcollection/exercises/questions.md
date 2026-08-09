# Alıştırma Soruları: Koleksiyonu Dönüştürmek

Bu sorular, `transformingcollection` paketindeki "her elemandan yeni bir
değer üretip yeni bir koleksiyonda toplama" konusunu gerçek iş
senaryolarıyla pekiştirmek için yazıldı. Sorularda `map`, `toList` gibi
isimler geçmiyor. Bazı sorular BİLE İSTEYE, "yeni koleksiyon" ile "sonucu
sadece yazdırma" arasındaki farkı ve iyi/kötü tasarım ayrımını fark
etmeniz için tuzak içeriyor.

---

### 1. Ürün İsimlerini Büyük Harfe Çevirme

Bir raporlama aracı, ürün isimlerinin BÜYÜK HARFLİ hâlinden oluşan YENİ bir
liste istiyor; bu yeni liste başka bir modüle gönderilecek.

### 2. Çalışan İsimlerini Ayıklama

İK sisteminde elinizde tam çalışan nesnelerinin (isim, departman, maaş
içeren) bir listesi var; sadece isimlerden oluşan YENİ, ayrı bir liste
istiyorsunuz.

### 3. Sipariş Tutarlarından Yeni Bir Sayı Listesi

Muhasebe, her siparişin toplam tutarını hesaplattırıp bu tutarların yer
aldığı YENİ bir sayı listesi istiyor; grafik çizim aracına bu liste
verilecek.

### 4. Döviz Kuruyla Fiyat Güncelleme

Bir e-ticaret sitesi, TL cinsinden fiyat listesindeki her fiyatı güncel bir
döviz kuruyla çarparak dolar cinsinden YENİ bir fiyat listesi oluşturmak
istiyor; orijinal TL listesi başka yerlerde de kullanıldığı için
değişmemeli.

### 5. İsim Uzunluklarından İstatistik Listesi

Bir kullanıcı adı politikası aracı, kullanıcı isimlerinin karakter
uzunluklarından oluşan YENİ bir sayı listesi istiyor (isimlerin kendisiyle
değil, uzunluklarıyla ilgileniyor).

### 6. Yanlış Kurgulanmış Bir Rapor Listesi (Tuzak)

Bir geliştirici, ürün isimlerini büyük harfe çevirip yeni bir listede
toplamak için, dışarıda boş bir liste oluşturup elemanları tek tek gezerek
bu listeye ekleyen bir kod yazmış. Kod çalışıyor ve doğru sonucu üretiyor.
Bu yaklaşımı değerlendirmeniz ve daha uygun bir alternatif önermeniz
isteniyor.

### 7. Kelime Sayısı Listesi

Bir metin analiz aracı, cümlelerden oluşan bir listeden, her cümledeki
kelime sayısını (boşluklara göre ayırarak) hesaplayıp YENİ bir sayı listesi
istiyor.

### 8. Doğum Tarihinden Yaş Listesi

Bir İK raporu, çalışanların doğum tarihlerinden hesaplanan yaşlarından
oluşan YENİ bir sayı listesi istiyor; çalışan nesnelerinin kendisi
değişmeyecek.

### 9. Farklı Bir Görüntüleme Nesnesine Dönüştürme

Bir API, veritabanından gelen tam ürün nesnelerini (maliyet, tedarikçi gibi
hassas bilgiler içeren) dışarıya göndermeden önce, sadece isim ve fiyat
içeren SADE bir görüntüleme nesnesinden oluşan YENİ bir listeye çevirmek
istiyor.

### 10. Henüz Çalışmayan Bir Dönüşüm (Tuzak)

Bir geliştirici, ürün isimlerini büyük harfe çevirecek bir işlem tanımlıyor
ama bu işlemi hiçbir yerde kullanmıyor (ekrana yazdırmıyor, listeye de
toplamıyor). Bu satırın çalıştırılmasından sonra hangi ürün isimlerinin
gerçekten büyük harfe çevrilmiş olacağını belirlemeniz isteniyor.
