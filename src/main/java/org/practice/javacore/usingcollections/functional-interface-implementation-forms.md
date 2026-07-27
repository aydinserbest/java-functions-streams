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
