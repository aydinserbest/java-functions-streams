# Stok Satışında Consumer veya Function Seçimi

Evet, bu lambda `Consumer<Stock>` davranışıdır:

```java
stock -> {
    if (stock.getQuantity() > 0) {
        stock.setQuantity(stock.getQuantity() - 1);
        System.out.println(
                "Sold 1 " + stock.getProductName()
                        + ". Remaining quantity: "
                        + stock.getQuantity()
        );
    } else {
        System.out.println(stock.getProductName() + " is out of stock.");
    }
}
```

Ama önemli ayrıntı şu: Lambda tek başına “Ben Consumer’ım” demez. Consumer olduğunu, kullanıldığı yer belirler.

Örneğin doğrudan `forEach()` içinde:

```java
stocks.forEach(stock -> {
    // davranış
});
```

`forEach()` metodunun sözleşmesi kabaca şöyledir:

```java
void forEach(Consumer<? super Stock> action)
```

Dolayısıyla Java şöyle düşünür:

```text
forEach() Consumer<Stock> bekliyor
             ↓
Lambda'nın stock parametresi Stock olur
             ↓
Lambda, Consumer.accept(Stock) metodunu uygular
```

Aynı lambda açıkça değişkene de atanabilir:

```java
Consumer<Stock> sellOne = stock -> {
    // davranış
};
```

## Soruda Consumer yazmasaydı nasıl karar verirdin?

Kendine iki temel soru sor:

```text
1. Davranış ne alıyor?
2. Davranış ne döndürüyor?
```

Bu örnekte:

```text
Ne alıyor?       → Bir Stock nesnesi
Ne yapıyor?      → Nesnenin quantity alanını değiştiriyor ve mesaj yazdırıyor
Ne döndürüyor?   → Hiçbir şey
```

Tip akışı:

```text
Stock → işlem yap → void
```

Bu doğrudan Consumer sözleşmesidir:

```java
Consumer<Stock>
```

```text
Consumer<T> = T alır, işlem yapar, sonuç döndürmez
```

Burada yapılan şey yeni bir sonuç üretmek değil, mevcut sistemde bir yan etki oluşturmaktır:

- Stok miktarını değiştirmek
- Ekrana mesaj yazdırmak

Bunlar Consumer için güçlü işaretlerdir.

## Neden `Predicate` değil?

Kodda `if` bulunması, lambdayı Predicate yapmaz.

`Predicate<Stock>` bir `Stock` alıp `boolean` döndürmeliydi:

```java
Predicate<Stock> isInStock =
        stock -> stock.getQuantity() > 0;
```

Kullanımı:

```java
boolean result = isInStock.test(iphone);
```

Bu yalnızca şu soruyu cevaplar:

```text
Bu ürün stokta var mı? → true veya false
```

Stok miktarını azaltmaz.

Senin lambda’nda ise `if`, Consumer’ın yapacağı işlemin içindeki bir business kuralıdır:

```text
Stok varsa azalt
Stok yoksa azaltma
```

Lambda’nın sonunda `boolean` döndürülmediği için Predicate değildir.

## Neden `Function` değil?

`Function<Stock, R>` bir `Stock` alıp yeni bir sonuç döndürmeliydi:

```java
Function<Stock, String> stockMessage =
        stock -> stock.getProductName()
                + " quantity: "
                + stock.getQuantity();
```

Bu lambda bir `String` üretir:

```java
String message = stockMessage.apply(iphone);
```

Senin satış lambdan ise yeni bir `Stock` veya `String` döndürmüyor. Mevcut nesnenin durumunu değiştiriyor:

```java
stock.setQuantity(stock.getQuantity() - 1);
```

Bu yüzden Function değil, Consumer’dır.

## Kolay karar tablosu

```text
T alır, boolean döndürür → Predicate<T>
T alır, R döndürür       → Function<T, R>
T alır, sonuç döndürmez  → Consumer<T>
Girdi almaz, T üretir    → Supplier<T>
```

Bu soruya uygularsak:

```text
Bir Stock al
     ↓
Stok varsa quantity değerini azalt
     ↓
Mesaj yazdır
     ↓
Bir sonuç döndürme
```

Sonuç:

```java
Consumer<Stock>
```

En kısa karar yöntemi şudur:

> “Bu davranış bana yeni bir değer mi verecek, yoksa var olan bir şey üzerinde işlem mi yapacak?”

Burada yeni bir değer istemiyorsun; satış gerçekleştikten sonra mevcut `Stock` nesnesini güncelliyorsun. Bu nedenle en doğal hazır functional interface `Consumer<Stock>` olur.

---

Evet, çok doğru düşünüyorsun: Yalnızca verdiğin business cümlelerini okursak `Consumer` tek mümkün çözüm değildir. `Function` da akla gelebilir.

Şu cümle:

> Satıştan sonra stok bir azaltılmalı.

bize yalnızca beklenen sonucu söyler. Fakat bu sonuca nasıl ulaşılacağını kesin olarak söylemez.

İki farklı tasarım mümkündür.

### 1. Mevcut nesneyi değiştirmek: `Consumer<Stock>`

Elimizde değiştirilebilir bir `Stock` nesnesi ve setter varsa:

```java
stock.setQuantity(stock.getQuantity() - 1);
```

aynı nesnenin durumunu değiştiririz:

```text
Girdi: mevcut Stock nesnesi
       quantity = 5
           ↓
Aynı nesne güncellenir
       quantity = 4
           ↓
Yeni değer döndürülmez
```

Bu durumda:

```java
Consumer<Stock> sellOne = stock -> {
    if (stock.getQuantity() > 0) {
        stock.setQuantity(stock.getQuantity() - 1);
    }
};
```

Kullanımı:

```java
sellOne.accept(iphone);
```

Burada Consumer seçmemizin asıl nedeni:

```text
Stock alıyor + aynı nesneyi değiştiriyor + sonuç döndürmüyor
```

### 2. Yeni bir nesne üretmek: `Function`

Mevcut nesneyi değiştirmek yerine yeni stok miktarına sahip başka bir nesne üretmek de mümkündür:

```java
Function<Stock, Stock> sellOne = stock -> {
    if (stock.getQuantity() > 0) {
        return new Stock(
                stock.getProductName(),
                stock.getQuantity() - 1
        );
    }

    return stock;
};
```

Kullanımı:

```java
Stock updatedStock = sellOne.apply(iphone);
```

Akış:

```text
Eski Stock: quantity = 5
              ↓
Function dönüşüm yapar
              ↓
Yeni Stock: quantity = 4
```

Burada mevcut nesneyi değiştirmiyoruz. Yeni bir sonuç ürettiğimiz için `Function` uygundur.

Hatta giriş ve çıkış türleri aynı olduğu için daha özel interface olan `UnaryOperator<Stock>` da kullanılabilir:

```java
UnaryOperator<Stock> sellOne = stock -> {
    if (stock.getQuantity() > 0) {
        return new Stock(
                stock.getProductName(),
                stock.getQuantity() - 1
        );
    }

    return stock;
};
```

## Peki sorudan hangisini çıkaracağız?

Yalnızca şu ifadeler varsa:

- Satıştan sonra stok bir azaltılmalı.
- Stok sıfırsa negatif olmamalı.
- Sistem stok bilgisini günceller.

bunlar tek başına Consumer’ı kesinleştirmez. Hem Consumer hem Function yaklaşımıyla çözülebilir.

Interface’i kesinleştiren teknik ayrım şudur:

```text
“Verilen Stock nesnesinin miktarını güncelle.”
→ Consumer<Stock>

“Yeni stok miktarına sahip bir Stock döndür.”
→ Function<Stock, Stock> / UnaryOperator<Stock>
```

Bizim requirement’ın devamında şu ifade vardı:

> Consumer yeni Product döndürmek yerine verilen nesnenin durumunu güncellemeli.

Ayrıca model için getter ve setter isteniyordu. Bunlar tasarım tercihini netleştiriyor:

```text
Setter kullan
Aynı nesneyi değiştir
Yeni Product döndürme
           ↓
Consumer<Product>
```

Dolayısıyla senin sezgin doğru: yalnızca business kısmını okuyunca Function da akla gelebilir. Consumer’a karar vermemizi sağlayan şey “stok azalacak” olması değil; işlemin mevcut nesne üzerinde yan etki oluşturması ve geriye sonuç döndürmemesidir.

Kısa karar:

```text
Mevcut Stock değişsin             → Consumer<Stock>
Yeni güncellenmiş Stock üretilsin → UnaryOperator<Stock>
Sadece stokta mı diye sorulsun     → Predicate<Stock>
```

---

Buradaki “yan etki”, metodun veya lambda’nın dışarıdan gözlemlenebilen bir değişiklik yapması demektir.

Örneğin:

```java
stock.setQuantity(stock.getQuantity() - 1);
```

bu işlem, kendisine verilen `Stock` nesnesinin içindeki `quantity` alanını değiştirir. Lambda bir değer döndürmese bile çağrıdan sonra nesne artık farklı durumdadır.

## Önceki ve sonraki durum

Başlangıçta:

```java
Stock iphone = new Stock("iphone", 5);
```

Nesnenin durumu:

```text
productName = "iphone"
quantity = 5
```

Consumer çalıştırılıyor:

```java
sellOne.accept(iphone);
```

Consumer içinde:

```java
stock.setQuantity(stock.getQuantity() - 1);
```

Çağrıdan sonra aynı nesnenin durumu:

```text
productName = "iphone"
quantity = 4
```

Consumer yeni bir `Stock` döndürmedi:

```java
Stock result = sellOne.accept(iphone); // olmaz, accept() void döndürür
```

Fakat mevcut `iphone` nesnesini değiştirdi:

```java
System.out.println(iphone.getQuantity()); // 4
```

Bu değişiklik yan etkidir.

## “Yeni sonuç üretmek” ne demek?

Bir `Function` çoğunlukla girdiden yeni bir sonuç üretir:

```java
Function<Integer, Integer> subtractOne =
        quantity -> quantity - 1;
```

Kullanımı:

```java
int oldQuantity = 5;
int newQuantity = subtractOne.apply(oldQuantity);
```

Burada:

```text
oldQuantity = 5
newQuantity = 4
```

Eski değer değiştirilmedi. Function hesaplama sonucunu geri döndürdü.

Karşılaştırma:

```text
Function:
5 alır → 4 üretip döndürür

Consumer:
Stock nesnesini alır → nesnenin quantity alanını değiştirir → sonuç döndürmez
```

## Yazdırmak neden yan etkidir?

Şu işlem de yan etkidir:

```java
System.out.println("Product sold");
```

Çünkü lambda bir değer döndürmüyor ama programın dışarıdan gözlemlenebilen durumunda değişiklik oluşturuyor: konsola yeni bir yazı gönderiliyor.

Consumer’ın yaygın yan etkileri şunlardır:

```java
Consumer<Stock> updateStock   // nesneyi değiştirir
Consumer<String> printMessage // konsola yazar
Consumer<Order> saveOrder     // veritabanına kaydeder
Consumer<Email> sendEmail     // dış servise istek gönderir
Consumer<Event> writeLog      // log kaydı oluşturur
```

Hepsinin ortak özelliği:

```text
Bir değer alırlar
Bir işlem gerçekleştirirler
Geriye sonuç döndürmezler
```

## Bu Consumer iki yan etki yapıyor

Senin örneğinde aslında iki farklı yan etki var:

```java
Consumer<Stock> sellOne = stock -> {
    if (stock.getQuantity() > 0) {
        stock.setQuantity(stock.getQuantity() - 1);

        System.out.println(
                "Sold 1 " + stock.getProductName()
        );
    }
};
```

Birinci yan etki:

```java
stock.setQuantity(...)
```

Mevcut nesnenin durumunu değiştirir.

İkinci yan etki:

```java
System.out.println(...)
```

Konsola çıktı gönderir.

Bu nedenle:

> Consumer yalnızca yazdırmak için kullanılmaz.

Consumer’ın asıl sözleşmesi “yazdırmak” değildir:

```text
T al, o değer üzerinde bir işlem yap, sonuç döndürme.
```

Yazdırmak bu işlemlerden yalnızca biridir. Mevcut nesneyi güncellemek de başka bir Consumer kullanımıdır.

En kısa haliyle:

```text
Dönüş değeri → Lambda çağrısının sana geri verdiği sonuç

Yan etki     → Lambda sonuç döndürmese bile dışarıda meydana gelen,
               sonradan gözlemleyebildiğin değişiklik
```

Bu örnekte `accept()` hiçbir şey döndürmez ama çağrıdan sonra `iphone` stoğunun `5` yerine `4` olması gözlemlenebilir. Yan etki tam olarak budur.

---

Bu requirement’ın kastedilen çözümüne bakarsak, mevcut nesneyi değiştiren `Consumer<Stock>` daha uygun görünüyor. Çünkü metinde şu işaretler var:

- “Sistem ürünün mevcut stok bilgisini günceller.”
- `Stock` için setter isteniyor.
- “Stok miktarını bir azalt” deniyor.
- Yeni bir `Stock` üretmekten bahsedilmiyor.

Dolayısıyla alıştırmanın beklediği zihinsel model:

```text
Mevcut Stock nesnesi
quantity = 5
       ↓ satış davranışı
Aynı Stock nesnesi
quantity = 4
```

Bu yüzden bu alıştırma kapsamında tercih:

```java
Consumer<Stock> sellOne = stock -> {
    if (stock.getQuantity() > 0) {
        stock.setQuantity(stock.getQuantity() - 1);
    }
};
```

olur.

Fakat “best practice” açısından konu biraz daha katmanlı.

## Gerçek uygulamada setter’ı Consumer içinde çağırmak

Bu çözüm öğretici ve küçük örnekler için uygundur:

```java
stock.setQuantity(stock.getQuantity() - 1);
```

Ama gerçek bir uygulamada stok azaltma kuralını lambda içine dağıtmak ideal olmayabilir. Çünkü aynı kural başka yerlerde tekrar yazılabilir:

```java
if (stock.getQuantity() > 0) {
    stock.setQuantity(stock.getQuantity() - 1);
}
```

Bir geliştirici başka yerde kontrolü unutabilir:

```java
stock.setQuantity(stock.getQuantity() - 1);
```

Böylece stok `-1` olabilir.

Daha güçlü bir tasarımda business kuralı `Stock` sınıfının kendi metodunda tutulur:

```java
public class Stock {
    private final String productName;
    private int quantity;

    public void sellOne() {
        if (quantity > 0) {
            quantity--;
        }
    }
}
```

Consumer yalnızca bu davranışı çağırır:

```java
Consumer<Stock> sellOne = Stock::sellOne;
```

Kullanımı:

```java
sellOne.accept(iphone);
```

Böylece sorumluluklar daha nettir:

```text
Stock sınıfı       → Stok azaltma kuralını bilir
Consumer           → Bu davranışı taşır
accept()            → Davranışı belirli ürün için çalıştırır
```

Bu, mutable nesne kullanılacaksa daha iyi bir tasarımdır.

## Function yaklaşımı ne zaman daha iyi olabilir?

Immutable, yani oluşturulduktan sonra değişmeyen nesnelerle çalışıyorsak yeni nesne üretmek daha uygun olabilir:

```java
record Stock(String productName, int quantity) {}
```

```java
UnaryOperator<Stock> sellOne = stock -> {
    if (stock.quantity() == 0) {
        return stock;
    }

    return new Stock(
            stock.productName(),
            stock.quantity() - 1
    );
};
```

Kullanırken dönen sonucu saklamak zorundayız:

```java
Stock updatedIphone = sellOne.apply(iphone);
```

Burada:

```text
iphone.quantity()        → 5
updatedIphone.quantity() → 4
```

Bu yaklaşımın avantajları:

- Eski nesne beklenmedik biçimde değişmez.
- Değişiklik daha görünürdür.
- Test etmek daha kolay olabilir.
- Eşzamanlı işlemlerde mutable state kaynaklı bazı riskler azalır.

Fakat dönen nesneyi saklamazsak güncelleme kaybolur:

```java
sellOne.apply(iphone); // Sonuç kullanılmadı.
```

`iphone` hâlâ eski nesneyi gösterir ve stoğu `5` kalır.

## Bu soru için hangisini seçerdim?

Bu alıştırmanın amacı Consumer öğrenmek ve modelde setter bulunuyor. Bu nedenle mevcut nesneyi güncelleyen Consumer çözümünü seçerdim:

```java
Consumer<Stock> sellOne = Stock::sellOne;
```

Henüz `sellOne()` metodu yazılmadıysa alıştırmanın doğrudan çözümü:

```java
Consumer<Stock> sellOne = stock -> {
    if (stock.getQuantity() > 0) {
        stock.setQuantity(stock.getQuantity() - 1);
    }
};
```

Ancak gerçek bir üretim uygulamasında stok satışı yalnızca `Consumer` ile modellenmeyebilir. Satışın başarı veya başarısızlık sonucunu bilmek isteriz:

```text
Satış gerçekleşti mi?
Stok neden azaltılamadı?
Aynı anda iki müşteri son ürünü almaya çalıştı mı?
Değişiklik veritabanına kaydedildi mi?
```

`Consumer` hiçbir sonuç döndürmediği için bunları doğrudan bildiremez. Gerçek uygulamada çoğunlukla bir servis metodu ve anlamlı bir sonuç kullanılır:

```java
SaleResult sellOne(ProductId productId)
```

Özetle:

```text
Bu Consumer alıştırması için
→ Mevcut nesneyi değiştiren Consumer doğru ve kastedilen çözüm.

Mutable tasarımda daha iyi kapsülleme için
→ Stok kuralını Stock.sellOne() metoduna koymak daha iyi.

Immutable/fonksiyonel tasarımda
→ Yeni Stock döndüren UnaryOperator<Stock> tercih edilebilir.

Gerçek satış sisteminde
→ İşlem sonucu ve veri tabanı transaction’ı olan servis metodu daha uygundur.
```

Yani sezgin doğru: Bu requirement’ın bağlamında mevcut nesneyi değiştirmek daha doğal. Fakat bunun nedeni Consumer’ın her zaman Function’dan daha iyi olması değil; requirement’ın mevcut nesneyi güncelleme ve setter kullanma yönünde tasarlanmış olmasıdır.
