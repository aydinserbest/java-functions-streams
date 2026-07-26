# Java `Map` Alıştırmaları — Requirement'lar

## 1. Ürün koduna göre stok

Değiştirilebilir `Map<String,Integer>` oluştur; ekle, güncelle ve key ile oku.

### Business açısından burada ne yapılıyor?

Depo ekranında çalışan barkod olarak `"LAPTOP"` kodunu girer ve sistem mevcut
stok miktarını anında göstermelidir. Başlangıçta Laptop 5, Mouse 20 adet olarak
Map'e eklenir; yeni sevkiyat gelince Laptop değeri 6 yapılır. Aynı ürün kodu
ikinci kez ek bir kayıt oluşturmamalı, mevcut key'in value değeri
güncellenmelidir.

## 2. Varsayılan değerle okuma

Olmayan ürün kodunda `getOrDefault(code,0)` kullan.

### Business açısından burada ne yapılıyor?

Yeni açılan `"MONITOR"` ürünü katalogda bulunmasına rağmen depo sayımı henüz
yapılmamış olabilir. Rapor `null` yazıp hesaplamayı bozmak yerine eksik stok
kaydını geçici olarak `0` göstermelidir. `getOrDefault()` yalnızca okuma
sırasında varsayılan değer vermeli; MONITOR key'ini Map'e kendiliğinden
eklememelidir.

## 3. Key, value ve entry dolaşımı

Aynı Map'i `keySet`, `values` ve `entrySet` ile ayrı ayrı dolaş.

### Business açısından burada ne yapılıyor?

Üç farklı ekran aynı stok Map'ini kullanır: ürün seçici yalnızca kodları,
kapasite grafiği yalnızca miktarları, denetim raporu ise `"LAPTOP -> 6"`
biçiminde ikisini birlikte ister. Her ihtiyaç için doğru görünüm seçilmelidir:
`keySet()` key'leri, `values()` value'ları, `entrySet()` ise `Map. Entry`
üzerinden iki tarafı birlikte sağlamalıdır.

## 4. ID'den Product bulma

`Map<Integer,Product>` oluştur ve güvenli lookup yap.

### Business açısından burada ne yapılıyor?

Sipariş satırında tam ürün yerine yalnızca `productId=103` saklanır. Sipariş
detayı açıldığında katalog listesini baştan sona taramak yerine
`Map<Integer,Product>` üzerinden Monitor nesnesine doğrudan erişilmelidir.
Bulunmayan 999 ID'si için sistem `null` üzerinde çalışmamalı; varsayılan ürün
veya açık hata politikası uygulanmalıdır.

## 5. Depo başına ürün listesi

`Map<Integer,List<Product>>` içinde depo ID'sini ürünlerle eşleştir.

### Business açısından burada ne yapılıyor?

Şirketin 1 numaralı deposunda Laptop ve Mouse, 2 numaralı deposunda Monitor ve
Keyboard vardır. Merkez ekran bir depo ID'si seçildiğinde o depodaki ürünlerin
tam listesini göstermelidir. Bu nedenle value tek Product değil `List<Product>`
olmalı; `get(1)` sonucunun Product değil ürün listesi döndürdüğü açıkça
gösterilmelidir.

## 6. `computeIfAbsent` ile liste oluşturma

Olmayan kategori key'i için yeni liste oluşturup ürün ekle.

### Business açısından burada ne yapılıyor?

Kataloğa ilk kez `"Electronics"` kategorisinde Laptop geldiğinde Map'te bu key
henüz olmayabilir. Kod önce `containsKey` ile uzun kontrol yapmak yerine
`computeIfAbsent()` ile boş listeyi yalnızca gerektiğinde oluşturmalı ve
Laptop'ı eklemelidir. İkinci elektronik ürün geldiğinde yeni liste açılmamalı,
mevcut listeye eklenmelidir.

## 7. Kelime frekansı

`merge(word,1,Integer::sum)` ile kelimeleri say.

### Business açısından burada ne yapılıyor?

Arama kayıtlarında `["java","stream","java","map"]` kelimeleri bulunur. Analitik
ekranı her kelimenin kaç kez kullanıldığını `{java=2, stream=1, map=1}`
biçiminde ister. İlk karşılaşmada sayaç 1 olarak eklenmeli, sonraki
karşılaşmalarda mevcut değer `Integer::sum` ile artırılmalıdır.

## 8. Aynı key'in üzerine yazma

`put()` ile aynı key'i ikinci kez ekle ve eski value'nun dönüşünü gözlemle.

### Business açısından burada ne yapılıyor?

Kullanıcının tema ayarı önce `"dark"`, daha sonra `"light"` olarak kaydedilir.
Map aynı `"theme"` key'i için iki ayrı ayar tutmamalıdır. İkinci `put()` eski
value değerini döndürmeli ve Map'te son tercih kalmalıdır; bu davranış key
benzersizliğini somut olarak göstermelidir.

## 9. Map türü seçme

Aynı kayıtları `HashMap`, `LinkedHashMap`, `TreeMap` içinde dolaştır.

### Business açısından burada ne yapılıyor?

Aynı sipariş durumları üç ayrı raporda kullanılacaktır. Hızlı genel lookup için
sıra garantisi gerekmeyen `HashMap`, işlemlerin eklendiği sırayı göstermek için
`LinkedHashMap`, kodları alfabetik sıralamak için `TreeMap` kullanılmalıdır.
Geliştirici yalnızca “Map lazım” dememeli; ekranın sıralama beklentisine göre
gerçek implementasyonu seçmelidir.

## 10. Güvenli kayıt güncelleme

`putIfAbsent`, `replace` ve `computeIfPresent` ile stok iş akışı kur.

### Business açısından burada ne yapılıyor?

Stok servisinde üç farklı kural vardır: Mouse yoksa başlangıç 20 ile eklenmeli,
varsa yönetici düzeltmesiyle 18 yapılmalı, satış olduğunda mevcut değerden bir
azaltılmalıdır. `putIfAbsent`, `replace` ve `computeIfPresent` bu üç niyeti ayrı
ayrı ifade etmelidir. Olmayan key üzerinde `replace` veya `computeIfPresent`
yanlışlıkla yeni stok kaydı oluşturmamalıdır.
