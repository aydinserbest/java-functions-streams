# Stream Oluşturma, Pipeline ve Tek Kullanım — Requirement'lar

## 1. Departman listesinden Stream oluşturma

Bir `List<String>` departmanı `stream()` ile dolaşıp yazdır.

### Business açısından burada ne yapılıyor?

İK servisinde `["HR","Engineering","Sales"]` departman listesi zaten bellekte
bulunur. Yönetici “departmanları göster” dediğinde yeni veri yapısı kurmak
yerine bu List üzerinden Stream açılmalı ve her ad konsola/rapora
gönderilmelidir. Stream yalnızca veriyi işleme görünümü sağlamalı; kaynak
departman listesine eleman eklememeli veya mevcut sırayı bozmamalıdır.

## 2. Array'den ürün kodu Stream'i

`String[]` ürün kodlarını `Arrays.stream()` ile işle.

### Business açısından burada ne yapılıyor?

Eski depo entegrasyonu ürün kodlarını Collection değil `String[]
{"LAPTOP","MOUSE","MONITOR"}` olarak gönderir. Uygulama sırf Stream kullanmak
için gereksiz ara List oluşturmamalıdır. `Arrays.stream(array)` ile kodlar
doğrudan pipeline'a alınmalı; her kod loglanmalı ve array'in kendisinin
`stream()` metodu olmadığı görülmelidir.

## 3. Sabit değerlerden Stream oluşturma

`Stream.of()` ile üç sipariş durumu oluştur ve yazdır.

### Business açısından burada ne yapılıyor?

Sipariş durum filtresini test eden geliştirici yalnızca `NEW`, `PAID` ve
`SHIPPED` değerlerine ihtiyaç duyar. Üretim veritabanı veya geçici List kurmak
yerine `Stream.of()` ile küçük ve açık bir test kaynağı oluşturmalıdır. Terminal
çıktıda üç durumun sırayla işlendiği görülmelidir.

## 4. Boş Stream ile güvenli sonuç

`Stream.empty()` üzerinde `count()` çalıştır.

### Business açısından burada ne yapılıyor?

Müşteri araması hiçbir kayıt bulmadığında servis `null` döndürürse çağıran her
katman null kontrolü yapmak zorunda kalır. Bunun yerine boş sonuç
`Stream.empty()` ile temsil edilmelidir. `count()` güvenli biçimde `0` üretmeli,
`forEach()` hiçbir işlem yapmadan bitmeli ve uygulama `NullPointerException`
vermemelidir.

## 5. Intermediate işlemlerin lazy olması

`map()` içinde log yaz, fakat önce terminal operation çağırma; sonra `toList()`
ekleyip farkı gözlemle.

### Business açısından burada ne yapılıyor?

Bir raporda her müşteri adını büyük harfe çevirme işlemi loglanmaktadır.
Yalnızca `names.stream(). map(... )` satırı yazıldığında konsolda `"Mapping
Alice"` mesajı çıkmamalıdır; çünkü henüz sonuç isteyen terminal operation
yoktur. `toList()` eklendiği anda bütün dönüşümler çalışmalı ve bu fark
Stream'in lazy davranışını somut göstermelidir.

## 6. Stream'i ikinci kez kullanma hatası

Bir Stream üzerinde `count()` çağırdıktan sonra aynı nesnede `forEach()` dene ve
`IllegalStateException` davranışını gözlemle.

### Business açısından burada ne yapılıyor?

Bir geliştirici aynı `Stream<String>` değişkeninden önce kayıt sayısını, sonra
kayıtların çıktısını almak ister. `count()` ilk terminal operation olarak
Stream'i tüketir; ardından aynı nesnede `forEach()` çağrısı
`IllegalStateException` üretmelidir. Amaç hatayı ezberlemek değil, Stream'in
yeniden kullanılabilir koleksiyon değil tek kullanımlık işlem hattı olduğunu
görmektir.

## 7. Aynı kaynaktan iki ayrı rapor

Bir List'ten aktif kayıt sayısı ve ayrı isim listesi için iki yeni Stream
oluştur.

### Business açısından burada ne yapılıyor?

Yönetim paneli aynı kullanıcı List'inden iki bağımsız kart üretir: aktif
kullanıcı sayısı ve bütün kullanıcı adları. İlk rapor `filter(). count()`,
ikinci rapor `map(). toList()` kullanmalıdır. Aynı Stream nesnesi
paylaşılmamalı; fakat kaynak List değişmediği için her rapor `users.stream()`
ile kendi güvenli pipeline'ını başlatabilmelidir.

## 8. Kaynak listenin değişmediğini gösterme

Şehirleri `map(String::toUpperCase).toList()` ile dönüştür ve kaynak listeyi de
yazdır.

### Business açısından burada ne yapılıyor?

Şehir raporu ekranda `"AMSTERDAM"` ve `"PARIS"` göstermek isterken uygulamanın
kaynak verisi `"amsterdam"` ve `"paris"` olarak korunmalıdır. `map()` yeni
String değerleri ve `toList()` yeni sonuç listesi üretmelidir. İşlemden sonra
hem kaynak hem sonuç yazdırılarak Stream dönüşümünün orijinal koleksiyonu
değiştirmediği kanıtlanmalıdır.

## 9. Stream builder ile dinamik test verisi

`Stream.builder()` ile koşula göre üç bildirim kodu ekleyip Stream oluştur.

### Business açısından burada ne yapılıyor?

Bildirim entegrasyonu test sırasında kanalları tek seferde değil koşullara göre
adım adım ekler: e-posta her zaman, SMS telefon varsa, PUSH mobil uygulama
varsa. `Stream. Builder<String>` bu değerleri toplamalı ve `build()` sonrasında
tek Stream üretmelidir. Oluşan akış terminal operation ile listeye çevrilerek
hangi kanalların seçildiği görülmelidir.

## 10. Pipeline aşamalarını sınıflandırma

`filter → map → sorted → toList` pipeline'ında hangi işlemlerin intermediate,
hangisinin terminal olduğunu kod yorumuyla belirt.

### Business açısından burada ne yapılıyor?

Aktif müşterilerin adlarını alfabetik rapora hazırlayan pipeline dört ayrı iş
adımı taşır: pasifleri ele, Customer'ı isme dönüştür, isimleri sırala ve sonuç
listesini üret. Geliştirici `filter`, `map`, `sorted` çağrılarının yalnızca
pipeline hazırlayan intermediate işlemler olduğunu; gerçek traversal ve sonuç
üretiminin `toList()` terminal operation'ında başladığını yorumlarla
belirtmelidir.
