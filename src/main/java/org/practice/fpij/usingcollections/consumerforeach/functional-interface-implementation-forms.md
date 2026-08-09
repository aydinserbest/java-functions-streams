# Functional Interface Uygulama Biçimleri

Hayır, functional interface’ler mutlaka lambda ile yazılmak zorunda değildir.

`Consumer`, `Function` ve `Predicate` birer interface’tir. Bunların tek abstract metodu farklı biçimlerde uygulanabilir:

1. Lambda
2. Method reference
3. Anonymous class
4. Normal bir class

Buradaki önemli nokta yazım şekli değil, metodun functional interface sözleşmesiyle uyumlu olmasıdır.

## 1. Lambda ile Consumer

`Consumer<Stock>` sözleşmesi:

```java
void accept(Stock stock);
```

Lambda ile:

```java
Consumer<Stock> sellOneAction = stock -> stock.sellOne();
```

Burada lambda:

```text
Stock alıyor
stock.sellOne() metodunu çağırıyor
Sonuç döndürmüyor
```

Buradaki `sellOne()` metodunun `Stock` sınıfında tanımlanmış bir nesne metodu
olduğu varsayılır:

```java
public class Stock {
    private String productName;
    private int quantity;

    public void sellOne() {
        if (quantity > 0) {
            quantity--;
        }
    }
}
```

Örneğin `iphone` bir `Stock` nesnesiyse:

```java
Stock iphone = new Stock("iphone", 5);
iphone.sellOne();
```

çağrısı `iphone` nesnesinin kendi stok miktarını azaltır.

Consumer üzerinden çalıştırıldığında:

```java
sellOneAction.accept(iphone);
```

veri akışı şöyledir:

```text
iphone
  ↓ accept() metoduna verilir
lambda içindeki stock parametresi
  ↓
stock.sellOne()
  ↓
iphone.sellOne()
```

Burada iki farklı yapı bulunur:

```text
sellOneAction → Consumer<Stock> davranışını tutan değişken
sellOne()     → Stock nesnesinin stok azaltan metodu
```

### Mevcut `Stock` sınıfıyla farkı

Şu an projedeki `Stock` sınıfında `sellOne()` metodu bulunmuyor. Stok azaltma
kuralı doğrudan `StockDemo` içindeki Consumer lambdasında yazılı:

```java
Consumer<Stock> sellOne = stock -> {
    if (stock.getQuantity() > 0) {
        stock.setQuantity(stock.getQuantity() - 1);
    }
};
```

Dolayısıyla aşağıdaki örnek mevcut sınıfla doğrudan çalışmaz:

```java
stock -> stock.sellOne()
```

Önce `Stock` sınıfına `sellOne()` metodunun eklenmesi gerekir. Bu alternatif
tasarımda stok miktarının sıfırın altına düşmemesi gibi business kuralı
`StockDemo` içindeki lambda yerine `Stock` sınıfında tutulur. Dokümanın devamındaki
örnekler bu alternatif tasarımı varsayar.

### Consumer, aldığı türün `void` metodunu çağırabilir mi?

Evet. `Consumer<Stock>`, `accept()` ile aldığı `Stock` nesnesinin uygun bir
`void` metodunu çağırabilir:

```java
Consumer<Stock> sellOneAction = stock -> stock.sellOne();
```

Buradaki tip bağlantısı:

```text
Consumer<Stock>
         ↓
accept() bir Stock alır
         ↓
Lambda bu Stock nesnesinin sellOne() metodunu çağırır
         ↓
accept() sonuç döndürmez
```

Ancak “Consumer yalnızca `void` metot çağırabilir” demek doğru olmaz.
Consumer'ın zorunlu sözleşmesi, lambda davranışının çağırana bir sonuç
döndürmemesidir. Lambda birden fazla işlem yapabilir; bazı metotların ürettiği
değeri kullanmadan bırakabilir. Yine de Consumer ile method reference kullanırken
en açık ve doğal örnek, `Stock::sellOne` gibi davranış amacı taşıyan bir `void`
metottur.

## 2. Method reference ile aynı Consumer

Lambda yalnızca mevcut bir metodu çağırıyorsa daha kısa yazılabilir:

```java
Consumer<Stock> sellOne = Stock::sellOne;
```

Bu, kavramsal olarak şunun kısa halidir:

```java
Consumer<Stock> sellOne = stock -> stock.sellOne();
```

İki yazım da `Consumer<Stock>` oluşturur:

```java
sellOne.accept(iphone);
```

çalıştırıldığında:

```java
iphone.sellOne();
```

çağrılır.

Akış:

```text
sellOne.accept(iphone)
           ↓
Stock::sellOne
           ↓
iphone.sellOne()
```

Method reference lambda’dan farklı bir functional interface değildir. Aynı interface’in davranışını daha kısa biçimde ifade eder.

## `Stock::sellOne` içindeki iki `Stock` neyi anlatıyor?

```java
Consumer<Stock> sellOne = Stock::sellOne;
```

Sol taraftaki:

```java
Consumer<Stock>
```

Consumer’ın `accept()` metoduna bir `Stock` nesnesi geleceğini belirtir.

Sağ taraftaki:

```java
Stock::sellOne
```

“`accept()` metoduna gelen `Stock` nesnesinin `sellOne()` metodunu çağır” anlamına gelir.

Yani:

```java
sellOne.accept(iphone);
```

şuna dönüşür:

```java
iphone.sellOne();
```

## 3. Anonymous class ile Consumer

Lambda’lardan önce aynı şey şöyle yazılıyordu:

```java
Consumer<Stock> sellOne = new Consumer<Stock>() {
    @Override
    public void accept(Stock stock) {
        stock.sellOne();
    }
};
```

Bu da tamamen geçerli bir `Consumer<Stock>` nesnesidir.

Lambda bunun kısa yazımıdır:

```java
Consumer<Stock> sellOne =
        stock -> stock.sellOne();
```

Method reference ise lambda’nın daha kısa hali olabilir:

```java
Consumer<Stock> sellOne =
        Stock::sellOne;
```

Üçü kavramsal olarak aynı davranışı sağlar:

```java
// Anonymous class
public void accept(Stock stock) {
    stock.sellOne();
}

// Lambda
stock -> stock.sellOne()

// Method reference
Stock::sellOne
```

## Function için de aynı durum geçerli

Örneğin:

```java
Function<String, Integer> length =
        text -> text.length();
```

Method reference olarak:

```java
Function<String, Integer> length =
        String::length;
```

Her ikisi de:

```text
String alır → Integer döndürür
```

Kullanımı:

```java
int result = length.apply("Java");
```

Sonuç:

```text
4
```

## Predicate örneği

Lambda:

```java
Predicate<String> isEmpty =
        text -> text.isEmpty();
```

Method reference:

```java
Predicate<String> isEmpty =
        String::isEmpty;
```

Her ikisi de:

```text
String alır → boolean döndürür
```

Kullanımı:

```java
isEmpty.test("");
```

## Temel zihinsel model

Functional interface davranışın sözleşmesidir:

```text
Consumer<Stock>
Stock al → işlem yap → void
```

Lambda ve method reference ise bu davranışı nasıl yazdığımızdır:

```text
Functional interface → Sözleşme
Lambda               → Sözleşmenin kısa uygulama biçimi
Method reference     → Uyumlu mevcut metoda yönlendirme
Anonymous class      → Sözleşmenin açık ve uzun uygulama biçimi
```

Kısa cevap:

```java
Consumer<Stock> sellOne = stock -> stock.sellOne();
```

ve

```java
Consumer<Stock> sellOne = Stock::sellOne;
```

aynı `Consumer<Stock>` davranışını temsil eder. Functional interface’in lambda olması gerekmez; lambda yalnızca onu oluşturmanın yöntemlerinden biridir.

---

Evet, lambda ifadeleri Java 8 ile geldi.

Şu kullanım:

```java
Consumer<Stock> sellOneAction =
        stock -> stock.sellOne();
```

ilk bakışta yalnızca yazım kolaylığı gibi görünüyor. Çünkü Java 8’den önce aynı davranış anonymous class ile yazılabiliyordu:

```java
Consumer<Stock> sellOneAction = new Consumer<Stock>() {
    @Override
    public void accept(Stock stock) {
        stock.sellOne();
    }
};
```

Lambda ile:

```java
Consumer<Stock> sellOneAction =
        stock -> stock.sellOne();
```

Bu açıdan cevap:

> Daha önce yapılamayan bir işlemi mümkün kılmaktan çok, var olan davranış nesnesi yaklaşımını çok daha kısa ve kullanılabilir hale getirdi.

Fakat lambda yalnızca karakter sayısını azaltan basit bir syntax kolaylığı da değildir.

## Lambda’nın getirdiği temel zihinsel değişiklik

Java’da daha önce çoğunlukla nesneleri metotlara gönderiyorduk:

```java
process(stock);
```

Lambda ile yapılacak davranışı da bir metoda göndermek pratik hale geldi:

```java
stocks.forEach(stock -> stock.sellOne());
```

Burada gönderdiğimiz şey `sellOne()` metodunun sonucu değildir. Daha sonra her `Stock` için çalıştırılabilecek davranıştır:

```text
“Bana bir Stock geldiğinde onun sellOne() metodunu çağır.”
```

Bu nedenle lambda, Java’da davranışları veri gibi:

- Değişkene atamayı
- Metoda parametre olarak göndermeyi
- Metottan döndürmeyi
- Başka davranışlarla birleştirmeyi

kolaylaştırdı.

## Önceden de davranış gönderilebiliyordu

Java 8’den önce:

```java
stocks.forEach(new Consumer<Stock>() {
    @Override
    public void accept(Stock stock) {
        stock.sellOne();
    }
});
```

Lambda ile:

```java
stocks.forEach(stock -> stock.sellOne());
```

İkisi de kavramsal olarak aynı `Consumer<Stock>` sözleşmesini uygular:

```java
void accept(Stock stock);
```

Ancak anonymous class kullanımı çok uzun olduğu için küçük davranışları metoda geçirmek zahmetliydi. Lambda bu kullanım tarzını günlük Java kodunda uygulanabilir hale getirdi.

Stream API’nin rahat kullanılabilmesinde de bunun büyük etkisi vardır:

```java
products.stream()
        .filter(product -> product.getStock() > 0)
        .map(Product::getName)
        .forEach(System.out::println);
```

Lambda olmasaydı her işlem için anonymous class yazmak gerekirdi ve bu akış okunabilirliğini büyük ölçüde kaybederdi.

## Lambda kendi başına bağımsız değildir

JavaScript gibi bazı dillerde fonksiyon doğrudan bir değer olabilir. Java’da lambda bir hedef tipe ihtiyaç duyar:

```java
stock -> stock.sellOne(); // Tek başına kullanılamaz
```

Şu şekilde bir functional interface hedefi verilmelidir:

```java
Consumer<Stock> action =
        stock -> stock.sellOne();
```

Burada Java şunları `Consumer<Stock>` üzerinden öğrenir:

```text
Lambda kaç parametre alacak? → 1
Parametrenin tipi ne?        → Stock
Sonuç döndürecek mi?         → Hayır, void
Hangi metodu temsil ediyor?  → accept(Stock)
```

Aynı lambda metot parametresi üzerinden de hedef tip kazanabilir:

```java
stocks.forEach(stock -> stock.sellOne());
```

Çünkü `forEach()` bir `Consumer<Stock>` bekler.

## Lambda, anonymous class’ın birebir kısa yazımı değildir

Öğrenirken anonymous class karşılığı üzerinden düşünmek faydalıdır:

```java
stock -> stock.sellOne()
```

yaklaşık olarak:

```java
new Consumer<Stock>() {
    @Override
    public void accept(Stock stock) {
        stock.sellOne();
    }
}
```

Ancak teknik olarak tamamen aynı değillerdir. Örneğin lambda yeni bir nesne scope’u oluşturmaz.

Anonymous class içinde:

```java
this
```

anonymous class nesnesini ifade eder.

Lambda içinde:

```java
this
```

lambdanın yazıldığı dış sınıfın nesnesini ifade eder.

Ayrıca Java, lambdaları derlerken normalde anonymous class için yapılanla birebir aynı biçimde ayrı bir sınıf oluşturmak zorunda değildir; JVM tarafında `invokedynamic` mekanizmasından yararlanır. Bu nedenle “lambda yalnızca derleyicinin anonymous class metnini kısaltmasıdır” demek teknik olarak eksik kalır.

## Senin örneğinde yenilik ne?

```java
Consumer<Stock> sellOneAction =
        stock -> stock.sellOne();
```

Buradaki `stock.sellOne()` zaten lambda öncesinde de çağrılabiliyordu:

```java
iphone.sellOne();
```

Lambda’nın getirdiği yenilik `sellOne()` metodunu çağırabilmek değildir. Yenilik, şu davranışı kolayca temsil edebilmektir:

```text
“Daha sonra bana hangi Stock verilirse onun sellOne() metodunu çağır.”
```

Sonra bu davranışı farklı yerlerde çalıştırabilirsin:

```java
sellOneAction.accept(iphone);
sellOneAction.accept(ipad);
stocks.forEach(sellOneAction);
```

Kısa sonuç:

```text
Lambda öncesi:
Davranış aktarımı mümkündü, fakat anonymous class gerektiriyordu.

Lambda sonrası:
Davranışlar kısa, hedef tipli ifadeler olarak yazılabilir hale geldi.

Asıl kazanım:
Yalnızca kısa syntax değil; davranış parametreleştirmeyi ve Stream gibi
fonksiyonel API’leri pratik, okunabilir ve yaygın hale getirmek.
```

---

Evet, tam olarak öyle çözerdik. Lambda, functional interface, anonymous class veya Stream bilmeden de bu soru klasik nesne yönelimli Java ile rahatlıkla çözülebilir.

`Stock` sınıfı kendi stok azaltma kuralını bilir:

```java
public class Stock {
    private String productName;
    private int quantity;

    public Stock(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public void sellOne() {
        if (quantity > 0) {
            quantity--;

            System.out.println(
                    "Sold 1 " + productName
                            + ". Remaining quantity: "
                            + quantity
            );
        } else {
            System.out.println(productName + " is out of stock.");
        }
    }

    public int getQuantity() {
        return quantity;
    }
}
```

Nesneyi oluşturup metodu doğrudan çağırırdık:

```java
Stock iphone = new Stock("iphone", 5);

iphone.sellOne();
```

Akış çok doğrudan:

```text
iphone nesnesini oluştur
        ↓
iphone.sellOne() çağır
        ↓
iphone nesnesinin quantity alanı 5'ten 4'e düşsün
```

Burada lambda’ya ihtiyaç yoktur.

## Birden fazla ürün varsa

Klasik bir döngü kullanırdık:

```java
List<Stock> stocks = List.of(
        new Stock("iphone", 5),
        new Stock("keyboard", 0),
        new Stock("ipad", 10)
);

for (Stock stock : stocks) {
    stock.sellOne();
}
```

Bu kod:

```java
for (Stock stock : stocks) {
    stock.sellOne();
}
```

kavramsal olarak şunları yapar:

```java
stocks.get(0).sellOne();
stocks.get(1).sellOne();
stocks.get(2).sellOne();
```

Yani listedeki her üründen bir tane satmayı dener.

Yalnızca iPhone satıldıysa:

```java
Stock iphone = stocks.get(0);
iphone.sellOne();
```

yeterlidir.

## Anonymous class ne zaman gerekirdi?

Yalnızca `sellOne()` metodunu çağırmak için anonymous class gerekmez.

Anonymous class, davranışı başka bir yere parametre olarak göndermek istediğimizde kullanılan eski yöntemlerden biridir.

Örneğin sistemde şöyle bir metot olduğunu varsayalım:

```java
static void processStock(
        Stock stock,
        Consumer<Stock> action
) {
    action.accept(stock);
}
```

Bu metot, yapılacak işlemi dışarıdan bekliyor. Java 8 öncesi benzer bir özel interface ve anonymous class kullanmak gerekebilirdi.

Ancak klasik çözümde davranışı parametre olarak göndermiyoruz:

```java
iphone.sellOne();
```

Doğrudan nesnenin metodunu çağırıyoruz. Bu nedenle anonymous class’a da ihtiyaç yok.

## Lambda geldiğinde ne değişti?

Klasik kullanım:

```java
iphone.sellOne();
```

Lambda ile davranışı bir değişkende tutabiliriz:

```java
Consumer<Stock> sellOneAction =
        stock -> stock.sellOne();
```

Daha sonra davranışı çalıştırabiliriz:

```java
sellOneAction.accept(iphone);
```

Method reference ile:

```java
Consumer<Stock> sellOneAction =
        Stock::sellOne;
```

Üç kullanım sonunda aynı business metodunu çağırabilir:

```java
// Doğrudan nesne üzerinden
iphone.sellOne();

// Lambda ile taşınan davranış
sellOneAction.accept(iphone);

// Liste üzerinden
stocks.forEach(sellOneAction);
```

Aralarındaki temel fark:

```text
iphone.sellOne()
→ Hangi nesnede hangi işlemin yapılacağı burada doğrudan belli.

Consumer<Stock> sellOneAction
→ Yapılacak işlem ayrı bir davranış olarak tutulabilir,
  başka metoda gönderilebilir ve farklı nesnelere uygulanabilir.
```

## Bu soru için en temel çözüm hangisidir?

Consumer öğrenme amacını bir kenara bırakırsak, en sade nesne yönelimli çözüm gerçekten şudur:

```java
Stock iphone = new Stock("iphone", 5);
iphone.sellOne();
```

Hatta stok miktarının sıfırın altına düşmemesi kuralını `Stock` sınıfındaki `sellOne()` metodunda tutmak güçlü bir tasarımdır. Çünkü kural nesnenin içinde korunur:

```java
public void sellOne() {
    if (quantity > 0) {
        quantity--;
    }
}
```

Böylece kodun başka bir yerinde yanlışlıkla negatif stok oluşturmak zorlaşır.

Consumer alıştırmasının amacı, bu problemi çözmenin tek yolunun Consumer olduğunu göstermek değildir. Aynı davranışı bir değer gibi taşıyabilmeyi öğretmektir:

```text
Klasik OOP:
Nesne üzerinden davranışı doğrudan çağır
→ iphone.sellOne()

Functional interface:
Davranışı ayrı olarak taşı, gerektiğinde nesne üzerinde çalıştır
→ sellOneAction.accept(iphone)

Collection:
Aynı davranışı bütün elemanlara uygula
→ stocks.forEach(sellOneAction)
```

Dolayısıyla sezgin doğru: Java’nın klasik OOP bilgisiyle `Stock` nesnesini oluşturup doğrudan `sellOne()` çağırmak hem mümkün hem de bu senaryo için son derece doğal bir çözümdür. Lambda burada stok azaltmayı mümkün hale getirmiyor; stok azaltma davranışını taşımayı ve başka API’lere vermeyi kolaylaştırıyor.

---

“Davranışı bir değer gibi taşımak” soyut bir ifade. Stok sorusunda somut karşılığı şudur:

> `sellOne()` işlemini hemen çağırmak yerine, “bir Stock verilirse `sellOne()` çağır” talimatını bir değişkende saklamak ve daha sonra kullanmak.

## Klasik kullanım: davranışı hemen çalıştırmak

```java
Stock iphone = new Stock("iphone", 5);

iphone.sellOne();
```

Java bu satıra geldiğinde `sellOne()` hemen çalışır:

```text
iphone quantity = 5
        ↓ iphone.sellOne()
iphone quantity = 4
```

Burada sakladığımız ayrı bir davranış yoktur. Belirli bir nesne üzerinde metodu doğrudan çağırıyoruz.

## Consumer kullanımı: davranışı saklamak

```java
Consumer<Stock> sellOneAction =
        stock -> stock.sellOne();
```

Java bu satıra geldiğinde herhangi bir ürün satılmaz. Burada yalnızca şu talimat saklanır:

```text
“Bana ileride bir Stock nesnesi verirsen,
 o nesnenin sellOne() metodunu çağır.”
```

Yani:

```java
Consumer<Stock> sellOneAction
```

değişkeni bir satış sonucunu tutmaz:

```text
quantity = 4
```

gibi bir değer saklamaz.

Yapılacak işi tutar:

```text
Stock al → o Stock üzerinde sellOne() çağır
```

Davranış daha sonra çalıştırılır:

```java
sellOneAction.accept(iphone);
```

Akış:

```text
sellOneAction içinde saklanan davranış
                  ↓
          iphone nesnesini alır
                  ↓
          iphone.sellOne()
                  ↓
       quantity 5'ten 4'e düşer
```

## İki satır arasındaki temel fark

Doğrudan çağrı:

```java
iphone.sellOne();
```

anlamı:

```text
Bu iPhone'u şimdi sat.
```

Davranışı değişkene atamak:

```java
Consumer<Stock> sellOneAction =
        stock -> stock.sellOne();
```

anlamı:

```text
Herhangi bir ürünü satmak için kullanılabilecek davranışı hazırla.
Henüz hangi ürün üzerinde çalışacağını söylemedim.
```

Daha sonra hangi üründe çalışacağını belirleriz:

```java
sellOneAction.accept(iphone);
sellOneAction.accept(ipad);
```

Aynı davranış farklı nesnelere uygulanır:

```text
sellOneAction + iphone → iphone.sellOne()
sellOneAction + ipad   → ipad.sellOne()
```

“Taşımak” derken kastedilen budur: Yapılacak iş `sellOneAction` değişkeni üzerinden farklı yerlere ve farklı nesnelere aktarılabilir.

## Neden “değer gibi” diyoruz?

Normal bir nesneyi değişkende tutabiliriz:

```java
Stock selectedStock = iphone;
```

Sonra başka bir metoda gönderebiliriz:

```java
process(selectedStock);
```

Consumer davranışını da değişkende tutabiliriz:

```java
Consumer<Stock> selectedAction =
        stock -> stock.sellOne();
```

Sonra başka bir metoda gönderebiliriz:

```java
process(selectedStock, selectedAction);
```

Örnek metot:

```java
static void process(
        Stock stock,
        Consumer<Stock> action
) {
    action.accept(stock);
}
```

Kullanımı:

```java
process(iphone, sellOneAction);
```

Bu çağrıda iki şey gönderiyoruz:

```text
iphone        → Hangi veri üzerinde çalışılacak?
sellOneAction → O veri üzerinde ne yapılacak?
```

`process()` metodunun içine geldiğimizde:

```java
action.accept(stock);
```

şu çağrıya dönüşür:

```java
iphone.sellOne();
```

İşte davranışı taşımak burada çok somut hale gelir: `process()` metoduna yalnızca ürünü değil, ürün üzerinde yapılacak işi de gönderdik.

## Bunun faydası ne?

Aynı `process()` metoduna farklı davranışlar gönderebiliriz.

Satış davranışı:

```java
Consumer<Stock> sellOneAction =
        stock -> stock.sellOne();
```

Bilgi yazdırma davranışı:

```java
Consumer<Stock> printAction =
        stock -> System.out.println(
                stock.getProductName()
                        + ": "
                        + stock.getQuantity()
        );
```

Aynı metodu değiştirmeden iki farklı iş yaptırabiliriz:

```java
process(iphone, sellOneAction);
process(iphone, printAction);
```

Akış:

```text
process(iphone, sellOneAction)
→ iphone üzerinde satış davranışını çalıştır

process(iphone, printAction)
→ iphone üzerinde yazdırma davranışını çalıştır
```

`process()` hangi işlemin ayrıntısını yapacağını bilmez:

```java
static void process(Stock stock, Consumer<Stock> action) {
    action.accept(stock);
}
```

Yalnızca dışarıdan gelen davranışı çalıştırır.

## `forEach()` örneğinde davranış nasıl taşınıyor?

```java
stocks.forEach(sellOneAction);
```

Burada `sellOneAction` davranışını `forEach()` metoduna parametre olarak gönderiyoruz.

`forEach()` kavramsal olarak şunu yapar:

```java
for (Stock stock : stocks) {
    sellOneAction.accept(stock);
}
```

Yani `forEach()` metoduna şunu söyledik:

```text
Listeyi nasıl dolaşacağını sen biliyorsun.

Her elemanda ne yapılacağını ise
sana verdiğim sellOneAction belirlesin.
```

## En kısa zihinsel model

Şu satır davranışı hemen çalıştırır:

```java
iphone.sellOne();
```

Şu satır davranışı tanımlar ve saklar:

```java
Consumer<Stock> sellOneAction =
        stock -> stock.sellOne();
```

Şu satır saklanan davranışı belirli bir nesne için çalıştırır:

```java
sellOneAction.accept(iphone);
```

Şu satır davranışı başka bir metoda taşır:

```java
stocks.forEach(sellOneAction);
```

Kısaca:

```text
iphone.sellOne()
→ İş şimdi yapılıyor.

stock -> stock.sellOne()
→ Daha sonra yapılabilecek iş tarif ediliyor.

sellOneAction
→ Bu iş tarifini tutan değişken.

sellOneAction.accept(iphone)
→ İş tarifi iPhone üzerinde çalıştırılıyor.

stocks.forEach(sellOneAction)
→ İş tarifi bütün listeye gönderiliyor.
```

Bu nedenle lambda için “davranışı bir değer gibi taşıyabiliriz” diyoruz: Yapılacak işi hemen çalıştırmak zorunda kalmadan bir değişkende tutabiliyor, başka metoda gönderebiliyor ve daha sonra seçtiğimiz nesneler üzerinde çalıştırabiliyoruz.
