# Consumer Alıştırmaları — Requirement'lar

Bu alıştırmaların amacı Java'nın hazır `Consumer<T>` interface'ini gerçek iş
senaryolarında kullanmayı öğrenmektir.

Temel formül:

```text
Consumer<T> = T türünde bir değer alır, o değerle bir işlem yapar ve sonuç döndürmez.
```

Tek bir değer için Consumer'ın `accept()` metodu, bir koleksiyonun bütün
elemanları için çoğunlukla `forEach()` kullanılır. Consumer'ın temel amacı yeni
bir değer üretmek değil; yazdırma, loglama, bildirim gönderme veya nesnenin
durumunu güncelleme gibi bir yan etki gerçekleştirmektir.

## 1. Müşteriye hoş geldin mesajı gösterme

Bir müşteri adını alıp ekrana kişisel hoş geldin mesajı yazdıran
`Consumer<String>` tanımla.

İstenen mesaj biçimi:

```text
Hoş geldin, Alice!
```

Consumer'ı şu değerlerle ayrı ayrı `accept()` kullanarak çalıştır:

```text
"Alice"  -> Hoş geldin, Alice!
"Mehmet" -> Hoş geldin, Mehmet!
```

### Business açısından burada ne yapılıyor?

Gerçek bir uygulamada kullanıcı giriş yaptıktan sonra ana sayfada kişisel bir
karşılama mesajı gösterilebilir:

```text
Kullanıcı: Alice
İşlem:     Başarılı giriş
Ekran:     Hoş geldin, Alice!
```

Sistemin gerçekleştireceği işlem şudur:

- Kullanıcı adı Consumer'a gönderilmeli.
- Consumer adı bir mesajın içine yerleştirip göstermeli.
- Herhangi bir String sonuç döndürülmemeli; mesajı göstermek işlemin kendisidir.

Bu yüzden burada `Function<String, String>` yerine `Consumer<String>` uygundur.

## 2. Sipariş numaralarını loglama

Bir sipariş numarasını aşağıdaki biçimde yazdıran `Consumer<Long>` tanımla:

```text
Sipariş işlendi: 1001
```

Şu sipariş numaralarını içeren listeyi `forEach()` ile işle:

```java
List<Long> orderIds = List.of(1001L, 1002L, 1003L);
```

Beklenen çıktı:

```text
Sipariş işlendi: 1001
Sipariş işlendi: 1002
Sipariş işlendi: 1003
```

### Business açısından burada ne yapılıyor?

Bir sipariş servisi toplu işlem bittikten sonra hangi siparişlerin işlendiğini
operasyon loguna yazabilir. Sistemin kuralı şudur:

- Listedeki her sipariş numarası sırayla alınmalı.
- Her numara için bir log satırı üretilmeli.
- Sipariş numaraları değiştirilmemeli ve yeni bir liste oluşturulmamalı.

`forEach()` her eleman için Consumer'ın `accept()` metodunu arka planda çalıştırır.

## 3. Kampanya e-postası gönderme simülasyonu

Şu modeli oluştur:

```java
Customer(String name, String email)
```

Bir müşteriyi alıp aşağıdaki mesajı yazdıran `Consumer<Customer>` tanımla:

```text
Kampanya e-postası gönderildi: alice@example.com
```

En az üç müşteriden oluşan listeyi `forEach()` ile işle.

### Business açısından burada ne yapılıyor?

Pazarlama ekibi kampanyaya dahil edilmiş müşterilere toplu e-posta göndermek
isteyebilir. Bu alıştırmada gerçek e-posta servisi yerine ekrana mesaj yazdırılır.
Gerçek sistemdeki akış şöyledir:

```text
Müşteri listesi
      ↓
Her müşteri için e-posta adresini al
      ↓
E-posta servisini çağır
      ↓
Gönderim yan etkisi gerçekleşsin
```

Consumer müşteriyi başka bir nesneye dönüştürmez; müşteri bilgisiyle gönderim
işlemini gerçekleştirir. Hangi müşterilerin kampanyaya uygun olduğunu seçmek
gerekirse bu işlemden önce ayrıca Predicate ile filtreleme yapılabilir.

## 4. Ürün stok miktarını azaltma

Değiştirilebilir şu sınıfı oluştur:

```java
Product(String name, int stock)
```

`stock` alanı için getter ve setter ekle. Bir ürün satıldığında stok miktarını bir
azaltan `Consumer<Product>` tanımla.

Kurallar:

- Stok sıfırdan büyükse bir azaltılmalı.
- Stok zaten sıfırsa negatif değere düşürülmemeli.

Şunlarla test et:

```text
Product("Mouse", 5)    -> accept sonrası stok 4
Product("Keyboard", 0) -> accept sonrası stok 0
```

### Business açısından burada ne yapılıyor?

Bir satış tamamlandığında sistem ürünün mevcut stok bilgisini günceller. Gerçek
akış şu şekilde düşünülebilir:

```text
Mouse mevcut stok: 5
Satış adedi:        1
Yeni stok:          4
```

Sistemin kuralları şunlardır:

- Satıştan sonra stok bir azaltılmalı.
- Stok yoksa değer `-1` yapılmamalı.
- Consumer yeni Product döndürmek yerine verilen nesnenin durumunu güncellemeli.

Bu örnek Consumer'ın yalnızca yazdırma değil, nesne üzerinde yan etki oluşturma
amacıyla da kullanılabileceğini gösterir.

## 5. Fatura satırlarını okunabilir biçimde yazdırma

Şu modeli oluştur:

```java
InvoiceLine(String description, int quantity, double unitPrice)
```

Bir fatura satırını aşağıdaki biçimde yazdıran `Consumer<InvoiceLine>` tanımla:

```text
Keyboard | 2 adet | €50.00 | Satır toplamı: €100.00
```

En az üç fatura satırını `forEach()` ile yazdır.

### Business açısından burada ne yapılıyor?

Fatura ekranı veya yazdırma servisi, domain nesnesinin ham görünümü yerine
kullanıcıya anlaşılır satırlar göstermek ister. Her satır için sistem:

- Ürün açıklamasını okumalı.
- Adet ve birim fiyatı göstermeli.
- `quantity * unitPrice` ile satır toplamını hesaplayıp yazmalı.

Bu alıştırmada satır toplamları yeni bir listeye dönüştürülmüyor; hesaplanan değer
doğrudan çıktı işleminde kullanılıyor. Bu yüzden Consumer uygundur.

## 6. Genel işlem çalıştırma metodu

Farklı değer ve işlemlerle tekrar kullanılabilecek generic bir metot yaz:

```java
<T> void process(
    T value,
    Consumer<T> action
)
```

Metot, aldığı Consumer'ı `accept()` ile çalıştırmalı. Aynı metodu değiştirmeden:

- Bir String'i büyük harfle yazdırmak
- Bir Integer'ın karesini yazdırmak
- Bir Customer'ın e-posta adresini yazdırmak

için kullan.

### Business açısından burada ne yapılıyor?

Bir uygulamada aynı "değeri al ve verilen işlemi uygula" mekanizması farklı
işlerde kullanılabilir. Örneğin yönetici ekranında seçilen kayıt için loglama,
bildirim veya çıktı üretme davranışlarından biri çalıştırılabilir.

Sistemin ortak akışı şudur:

```text
Değer + yapılacak işlem
          ↓
process(value, action)
          ↓
action.accept(value)
```

`process()` işlemin ayrıntısını bilmez. Yapılacak işi dışarıdan verilen Consumer
belirler; böylece davranış metoda parametre olarak gönderilmiş olur.

## 7. Kaydetme ve bildirim işlemlerini `andThen()` ile birleştirme

Şu modeli oluştur:

```java
Order(long id, String customerEmail)
```

İki ayrı Consumer tanımla:

- `saveOrder`: `"Sipariş kaydedildi: 1001"` mesajını yazdırsın.
- `sendConfirmation`: `"Onay e-postası gönderildi: alice@example.com"` mesajını
  yazdırsın.

İki Consumer'ı `andThen()` ile birleştir ve tek `accept(order)` çağrısıyla sırayla
çalıştır.

Beklenen işlem sırası:

```text
Sipariş kaydedildi: 1001
Onay e-postası gönderildi: alice@example.com
```

### Business açısından burada ne yapılıyor?

Gerçek bir sipariş tamamlama akışında önce sipariş kalıcı olarak kaydedilir,
ardından müşteriye onay gönderilir:

```text
Order nesnesi
     ↓
Siparişi kaydet
     ↓
Aynı Order ile onay bildirimi gönder
```

İki Consumer da aynı Order nesnesini alır. İlk Consumer'ın çıktısı ikinciye
aktarılmaz; çünkü Consumer `void` döndürür. `andThen()` burada dönüşüm zinciri
değil, aynı veri üzerinde sıralı iki yan etki oluşturur.

## 8. Denetim kaydı oluşturma

Şu modeli oluştur:

```java
UserAction(String username, String action)
```

Bir kullanıcı hareketini aşağıdaki biçimde yazdıran `Consumer<UserAction>`
tanımla:

```text
AUDIT | Kullanıcı: admin | İşlem: PRODUCT_DELETED
```

Şu hareketleri bir listeye koyup `forEach()` ile işle:

```text
admin   -> PRODUCT_DELETED
mehmet  -> ORDER_APPROVED
support -> USER_UNLOCKED
```

### Business açısından burada ne yapılıyor?

Finans, yönetim ve güvenlik açısından önemli işlemlerin kim tarafından yapıldığı
kayıt altına alınmalıdır. Örneğin yönetici bir ürünü sildiğinde sistem:

- İşlemi yapan kullanıcıyı bilmeli.
- Yapılan işlemin türünü bilmeli.
- Bu bilgileri denetim günlüğüne yazmalı.

Consumer bu örnekte geri değer üretmez; denetlenebilirlik için log yan etkisini
gerçekleştirir. Gerçek projede `System.out` yerine bir logging veya audit servisi
kullanılabilir.

## 9. Method reference ile bildirim kuyruğuna ekleme

Şu servis sınıfını oluştur:

```java
class NotificationService {
    void send(String message) {
        System.out.println("Bildirim gönderildi: " + message);
    }
}
```

`NotificationService` nesnesinin `send` metodunu method reference kullanarak bir
`Consumer<String>` referansına ata:

```text
service::send
```

Şu mesajları `forEach()` ile gönder:

```text
Siparişiniz hazırlandı.
Siparişiniz kargoya verildi.
Siparişiniz teslim edildi.
```

### Business açısından burada ne yapılıyor?

Sipariş yaşam döngüsündeki olaylar müşteriye bildirim olarak iletilebilir. Servisin
zaten `String alıp void döndüren` uygun bir metodu varsa aynı davranışı lambda ile
tekrar yazmak gerekmez.

Sistemin akışı şudur:

- Bildirim mesajları sırayla alınmalı.
- Her mesaj mevcut `NotificationService.send()` metoduna gönderilmeli.
- Gönderim yapıldıktan sonra yeni bir sonuç değeri beklenmemeli.

Method reference, mevcut servis metodunu Consumer sözleşmesine bağlar.

## 10. Başarılı ödemeleri filtreleyip makbuz işlemlerini çalıştırma

Şu modeli oluştur:

```java
Payment(long id, double amount, boolean successful)
```

Bir ödeme listesi üzerinde şu akışı kur:

1. Yalnızca başarılı ödemeleri `Predicate<Payment>` ve `filter()` ile seç.
2. Başarılı her ödeme için makbuz yazdıran `Consumer<Payment>` tanımla.
3. Başarılı ödemeleri `forEach(receiptPrinter)` ile işle.

Makbuz biçimi:

```text
Makbuz | Ödeme: 1001 | Tutar: €75.00
```

Örnek veri:

```text
Payment(1001, 75.0, true)  -> makbuz yazdırılmalı
Payment(1002, 40.0, false) -> yazdırılmamalı
Payment(1003, 120.0, true) -> makbuz yazdırılmalı
```

### Business açısından burada ne yapılıyor?

Ödeme sistemi başarısız bir işlem için makbuz üretmemelidir. Gerçek akış iki
farklı sorumluluğu birleştirir:

```text
Bütün ödemeler
      ↓ Predicate: Ödeme başarılı mı?
Başarılı ödemeler
      ↓ Consumer: Makbuzu yazdır
Yan etki tamamlandı
```

Sistemin kuralları şunlardır:

- Başarısız ödeme `filter()` aşamasında elenmeli.
- Başarılı ödeme Consumer'a gönderilmeli.
- Consumer ödeme nesnesini dönüştürmemeli; makbuz yazdırma işlemini yapmalı.

Bu soru Predicate'in seçim, Consumer'ın ise seçilmiş veriler üzerinde işlem
yapma sorumluluğunu birlikte gösterir.

