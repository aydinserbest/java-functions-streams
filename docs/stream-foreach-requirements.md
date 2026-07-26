# `forEach()` ve Consumer Alıştırmaları — Requirement'lar

## 1. Sipariş ID'lerini yazdırma

Sipariş ID listesini `forEach()` ile logla.

### Business açısından burada ne yapılıyor?

Bir e-ticaret uygulaması gece boyunca bekleyen siparişleri işler. İşlem
tamamlandıktan sonra teknik ekip, hangi siparişlerin başarıyla işlendiğini log
ekranında tek tek görmek ister:

```text
İşlenen siparişler: [1001, 1002, 1003]

Log ekranı:
Processed order: 1001
Processed order: 1002
Processed order: 1003
```

Sistem listedeki ilk ID'yi alıp bir log satırı yazmalı, sonra aynı işlemi ikinci
ve üçüncü ID için tekrarlamalıdır. Burada yeni bir sipariş listesi veya toplam
üretilmez; her sipariş için log yazılması işlemin kendisidir. `forEach()` bu
yüzden listedeki her ID üzerinde aynı Consumer davranışını çalıştırır.

## 2. E-posta gönderme simülasyonu

Müşteri listesindeki her kişiye `emailService::send` davranışını uygula.

### Business açısından burada ne yapılıyor?

Pazarlama ekibi kampanya duyurusunu kayıtlı müşterilere göndermek ister.
Sistemin elinde üç müşteri vardır:

```text
Alice  -> alice@example.com
Mehmet -> mehmet@example.com
Sofia  -> sofia@example.com
```

Kampanya başlatıldığında beklenen akış şöyledir:

```text
Alice alınır  -> EmailService.send(Alice) çalışır  -> gönderim mesajı görünür
Mehmet alınır -> EmailService.send(Mehmet) çalışır -> gönderim mesajı görünür
Sofia alınır  -> EmailService.send(Sofia) çalışır  -> gönderim mesajı görünür
```

Gerçek uygulamada `send()` e-posta yollar; bu alıştırmada yalnızca gönderimi
simüle eden bir mesaj yazdırır. `emailService::send`, yapılacak işlemi temsil
eder; `forEach()` bu işlemi listedeki her müşteri için çağırır.

## 3. Stok nesnelerini güncelleme

Her stoklu ürünün miktarını bir azaltan Consumer kullan.

### Business açısından burada ne yapılıyor?

Mağaza kapanırken gün içinde birer adet satılan ürünlerin stokları sisteme
işlenir. Başlangıç verisi şöyle olsun:

```text
Mouse:    5 adet
Monitor:  2 adet
Keyboard: 0 adet
```

Her ürün için “stok sıfırdan büyükse bir azalt” işlemi çalıştırılmalıdır:

```text
Mouse:    5 -> 4
Monitor:  2 -> 1
Keyboard: 0 -> 0
```

Keyboard stoğu eksiye düşmemelidir. Consumer yeni bir Product döndürmez;
elindeki Product nesnesinin stok alanını günceller. Bu nedenle `forEach()`
bittikten sonra aynı ürün nesneleri tekrar yazdırılarak değişiklik görülmelidir.

## 4. Filtre sonrası makbuz yazdırma

Başarılı ödemeleri filtreleyip `forEach()` ile makbuz çıktısı üret.

### Business açısından burada ne yapılıyor?

Bir ödeme ekranında başarılı ve reddedilmiş işlemler aynı listede bulunabilir:

```text
Ödeme 501 -> başarılı
Ödeme 502 -> reddedildi
Ödeme 503 -> başarılı
```

Müşteriden para alınmadıysa makbuz oluşturulmamalıdır. Bu yüzden sistem önce
başarılı ödemeleri seçmeli, sonra yalnızca bu kayıtlar için makbuz
yazdırmalıdır:

```text
Makbuz yazdırıldı: 501
Makbuz yazdırıldı: 503
```

502 numaralı ödeme reddedildiği için receipt servisine hiç gönderilmemelidir.
`filter()` kimin işleme alınacağını, `forEach()` ise seçilen her ödeme için
makbuz yazdırma işleminin çalışmasını sağlar.

## 5. Lambda ve method reference

`value -> System.out.println(value)` ile `System.out::println` sonuçlarını
karşılaştır.

### Business açısından burada ne yapılıyor?

Ürün kontrol ekranında listedeki adların alt alta yazdırılması istenir:

```text
Ürünler: [Laptop, Mouse]

Ekran:
Laptop
Mouse
```

Aynı liste önce `value -> System.out.println(value)` davranışıyla, sonra
`System.out::println` davranışıyla çalıştırılmalıdır. Her iki kullanım da
yukarıdaki çıktıyı aynen üretmelidir. Business işlemi değişmez: listedeki ürün
adını al ve ekrana yaz. Değişen yalnızca Java'da bu davranışın yazılış
biçimidir.

## 6. Önceden tanımlı Consumer

Bir `Consumer<Product>` değişkenini birden fazla ürün listesinde kullan.

### Business açısından burada ne yapılıyor?

Depo uygulamasında elektronik ve mobilya ürünleri ayrı listelerde tutulur:

```text
Elektronik: [Laptop, Monitor]
Mobilya:    [Desk, Chair]
```

İki listenin de aynı operasyon loguna şu biçimde yazılması gerekir:

```text
Product processed: Laptop
Product processed: Monitor
Product processed: Desk
Product processed: Chair
```

“Ürün adını al ve bu log biçiminde yaz” davranışı bir kez `Consumer<Product>`
olarak tanımlanmalıdır. Sonra aynı Consumer iki listenin `forEach()` çağrısında
kullanılmalıdır. Böylece mesaj biçimi değişirse iki ayrı yerde kod düzeltmek
gerekmez.

## 7. `andThen()` ile iki yan etki

Önce kaydetme, sonra bildirim Consumer'ını tek `forEach()` içinde çalıştır.

### Business açısından burada ne yapılıyor?

Bir sipariş tamamlandığında iki işlem yapılır: önce sipariş veritabanına
kaydedilir, ardından müşteriye onay bildirimi gönderilir.

```text
Sipariş: 1001

1. Veritabanına kaydedildi: 1001
2. Müşteriye bildirim gönderildi: 1001
```

1002 numaralı siparişe geçildiğinde de aynı iki adım aynı sırayla
tekrarlanmalıdır. Bildirimin kayıttan önce gitmesi yanlış olur; müşteri sistemde
henüz bulunmayan bir sipariş için mesaj alabilir. `save.andThen(notify)` bu iki
Consumer'ı tek davranış halinde birleştirir ve `forEach()` bunu her sipariş için
çalıştırır.

## 8. Map `forEach`

`Map<String,Integer>` için key ve value'yu birlikte yazdır.

### Business açısından burada ne yapılıyor?

Depo sisteminde ürün kodu ile stok miktarı birlikte tutulur:

```text
LAPTOP -> 6
MOUSE  -> 20
```

Operasyon ekibinin istediği rapor şöyledir:

```text
LAPTOP has 6 units
MOUSE has 20 units
```

Bir satırı oluşturabilmek için hem ürün koduna hem stok miktarına aynı anda
ihtiyaç vardır. Bu nedenle Map'in `forEach()` metodu her turda iki değer verir:
`code` ve `stock`. Collection `forEach()` tek elemanla çalışırken bu kullanım
Map'teki key-value çiftini birlikte işler.

## 9. `forEachOrdered`

Paralel Stream'de sıralı rapor çıktısı için `forEachOrdered()` kullan.

### Business açısından burada ne yapılıyor?

Bir faturadaki satırlar müşteri tarafından şu sırada oluşturulmuştur:

```text
1. Laptop
2. Mouse
3. Keyboard
4. Monitor
```

Rapor hazırlığı parallel Stream ile yapılırken normal `forEach()` bu satırları
örneğin `Mouse, Monitor, Laptop, Keyboard` sırasıyla yazabilir. Hesaplama doğru
olsa bile faturadaki sıra bozulduğu için çıktı kullanıcı açısından yanlıştır.

`forEachOrdered()` kullanıldığında paralel işlem yapılsa da ekranda yine Laptop,
Mouse, Keyboard ve Monitor sırası görülmelidir. Bu soruda amaç “her elemanı
işlemenin” yanında kaynak sırasının raporda korunmasını sağlamaktır.

## 10. Sonuç üretme hatası

Toplam hesaplamak için dış değişkeni `forEach()` içinde değiştirmek yerine
`mapToInt().sum()` kullan.

### Business açısından burada ne yapılıyor?

Depo yöneticisi ürünleri tek tek yazdırmak değil, depodaki toplam ürün adedini
görmek ister:

```text
Laptop:   6
Mouse:   20
Monitor:  4
----------------
Toplam:  30
```

Burada istenen sonuç her ürün için ayrı bir yan etki değil, bütün kayıtların
birleşiminden oluşan tek sayıdır. Bu nedenle dışarıda `total` değişkeni açıp
`forEach()` içinde artırmak yerine her Product stok sayısına çevrilmeli ve
`sum()` ile `30` sonucu üretilmelidir. `forEach()` “her elemana bir işlem yap”
içindir; `sum()` ise “elemanlardan tek toplam sonuç üret” ihtiyacını doğrudan
anlatır.
