# `Collectors.partitioningBy()` — Requirement'lar

## 1. Aktif ve pasif kullanıcılar

Kullanıcıları `active` koşuluyla ikiye ayır.

### Business açısından burada ne yapılıyor?

Hesap yönetimi ekranı, sisteme giriş yapabilen aktif kullanıcılarla erişimi
kapatılmış pasif kullanıcıları ayrı bölümlerde gösterecektir. Tek bir `active`
kuralı bütün kullanıcılar üzerinde çalıştırılır ve hem `true` hem `false`
listesi aynı sonuçta hazırlanır. Destek ekibi böylece iki tarafı da eksiksiz
görür.

## 2. Geçen ve kalan öğrenciler

Notu en az 60 olanları partition et.

### Business açısından burada ne yapılıyor?

Okul sistemi 60 ve üzeri not alan öğrencileri başarılılar listesine, diğerlerini
telafi sınavı listesine koyacaktır. Aynı öğrencinin iki listede bulunmaması ve
hiçbir öğrencinin kaybolmaması gerekir. `partitioningBy`, tek geçme kuralından
iki karşıt sonucu birlikte üretir.

## 3. Ödenmiş ve açık faturalar

Faturaları `paid` durumuna göre ayır.

### Business açısından burada ne yapılıyor?

Finans ekranında ödenmiş faturalar mutabakat için, açık faturalar ise tahsilat
takibi için ayrı gösterilecektir. Her fatura `paid` alanına göre iki gruptan
yalnız birine girer. `false` tarafı, müşteriye hatırlatma gönderilecek borç
listesini somut olarak verir.

## 4. Stokta ve tükenmiş ürünler

`stock > 0` Predicate'i kullan.

### Business açısından burada ne yapılıyor?

Katalog servisi stoku bulunan ürünlerde “Sepete ekle” düğmesini açarken, satın
alma ekibi stoku tükenen ürünleri tedarik listesine almak ister. Aynı ürün
listesi `stock > 0` kuralıyla ikiye ayrılır. Böylece satışa açık ve takviye
bekleyen ürünler tek işlemde ve birbirini tamamlayacak şekilde hazırlanır.

## 5. Yetişkin ve çocuk katılımcılar

Yaşı 18 ve üzeri olanları partition et.

### Business açısından burada ne yapılıyor?

Etkinlik kayıt sistemi 18 yaş ve üzerindeki katılımcıları standart kayıt
akışına, çocukları ise veli onayı gereken akışa yönlendirecektir. Yaş kuralının
iki sonucu da operasyon için gereklidir. Bölme sonucundaki `true` yetişkinleri,
`false` ise ek izin belgesi istenecek kişileri taşır.

## 6. İletişim izni olan müşteriler

Consent alanına göre kampanya uygunluğunu ayır.

### Business açısından burada ne yapılıyor?

Pazarlama ekibi kampanya mesajını yalnız açık iletişim izni vermiş müşterilere
gönderebilir. İzni olanlar gönderim listesine alınırken olmayanlar denetim
amacıyla ayrıca raporlanmalıdır; sessizce yok edilmemelidir. Consent Predicate'i
iki listeyi aynı anda oluşturarak hem kampanyayı hem yasal uygunluk kontrolünü
destekler.

## 7. İki grubun sayılarını üretme

`partitioningBy(condition, counting())` kullan.

### Business açısından burada ne yapılıyor?

Bir kredi ön değerlendirme dashboard'u, müşteri ayrıntılarını değil koşulu
karşılayan ve karşılamayan başvuru sayılarını gösterecektir. Her başvuruyu
listede tutmak yerine iki tarafta `counting()` çalıştırılır. Sonuç örneğin `true
-> 845`, `false -> 155` biçiminde hızlı bir operasyon özeti sağlar.

## 8. İki grupta yalnızca isimleri tutma

Downstream `mapping(name,toList())` kullan.

### Business açısından burada ne yapılıyor?

Bir katılım ekranı onay veren ve vermeyen kişileri iki başlık altında gösterir,
fakat kişilerin tüm domain alanlarını tarayıcıya göndermek istemez. Boolean
gruplar korunurken her kişi yalnız adına dönüştürülür. Böylece sonuç
`Map<Boolean,List<String>>` olur ve ekran gereksiz kişisel veri almaz.

## 9. Premium ve standart fiyat özeti

Her partition için `summarizingDouble(price)` üret.

### Business açısından burada ne yapılıyor?

Finans ekibi belirlenen fiyat eşiğinin üzerindeki premium ürünlerle diğer
ürünlerin ticari profilini karşılaştıracaktır. Her iki taraf için ürün adedi,
toplam fiyat, minimum, maksimum ve ortalama aynı anda hesaplanır. Böylece iki
segmentin özetleri ayrı sorgular yazılmadan yan yana raporlanabilir.

## 10. Generic partition metodu

`<T> Map<Boolean,List<T>> partition(List<T>, Predicate<T>)` yaz.

### Business açısından burada ne yapılıyor?

Uygulamanın farklı yerlerinde kullanıcılar aktifliğe, ürünler stoka, ödemeler
başarı durumuna göre ikiye ayrılacaktır. Her domain tipi için aynı döngüyü
kopyalamak yerine liste ve iş kuralı parametre olarak alınır. Generic metot,
verilen `Predicate<T>`ye uyanları `true`, diğerlerini `false` anahtarında
döndürerek ortak ve test edilebilir bir çözüm sunar.
