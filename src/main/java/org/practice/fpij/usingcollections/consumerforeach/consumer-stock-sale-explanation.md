# Stok Satışı Üzerinden `Consumer`, `forEach()` ve `accept()`

Bu not, bir ürün satıldığında stok miktarını bir azaltma sorusundaki temel
ayrımları açıklar.

Örnek model şu şekilde düşünülebilir:

```java
public class Stock {
    private String productName;
    private int quantity;

    public Stock(String productName, int quantity) {
        this.productName = productName;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
```

## 1. Satış davranışını doğrudan lambda olarak yazmak

`forEach()` parametre olarak bir `Consumer<Stock>` bekler. Bu nedenle lambda
doğrudan `forEach()` içine yazılabilir:

```java
stocks.forEach(stock -> {
    if (stock.getQuantity() > 0) {
        stock.setQuantity(stock.getQuantity() - 1);
        System.out.println(
                stock.getProductName() + " yeni stok: " + stock.getQuantity()
        );
    } else {
        System.out.println(stock.getProductName() + " stokta yok.");
    }
});
```

Buradaki lambda otomatik olarak `Consumer<Stock>` sözleşmesine oturur:

```text
Stock nesnesi alır
       ↓
Nesnenin stok miktarını günceller
       ↓
Yeni bir değer döndürmez
```

Davranış yalnızca bir yerde kullanılacaksa lambda doğrudan `forEach()` içine
yazılabilir.

## 2. Aynı davranışı `Consumer` değişkenine atamak

Davranışa anlamlı bir ad vermek veya onu tekrar kullanmak için lambda önce bir
`Consumer<Stock>` değişkenine atanabilir:

```java
Consumer<Stock> sellOne = stock -> {
    if (stock.getQuantity() > 0) {
        stock.setQuantity(stock.getQuantity() - 1);
        System.out.println(
                stock.getProductName() + " yeni stok: " + stock.getQuantity()
        );
    } else {
        System.out.println(stock.getProductName() + " stokta yok.");
    }
};
```

Bu satırlar davranışı tanımlar. Lambda henüz çalışmaz:

```text
sellOne = "Bir Stock nesnesinden bir adet satılırsa ne yapılacak?"
```

Davranış, `accept()` veya `forEach()` tarafından çağrıldığında gerçekten çalışır.

## 3. `accept()` ile `forEach()` arasındaki business farkı

Örnek liste:

```java
List<Stock> stocks = List.of(
        new Stock("iphone", 5),
        new Stock("macbook", 3),
        new Stock("ipad", 10)
);
```

Yalnızca bir iPhone satıldıysa Consumer tek bir `Stock` nesnesi için
çalıştırılır:

```java
Stock soldProduct = stocks.get(0);
sellOne.accept(soldProduct);
```

Sonuç:

```text
iphone:  5  → 4
macbook: 3  → 3
ipad:   10  → 10
```

Consumer bütün listeye verilirse:

```java
stocks.forEach(sellOne);
```

`forEach()` kavramsal olarak şunları yapar:

```java
sellOne.accept(stocks.get(0));
sellOne.accept(stocks.get(1));
sellOne.accept(stocks.get(2));
```

Sonuç:

```text
iphone:  5  → 4
macbook: 3  → 2
ipad:   10  → 9
```

Bu kullanım, listedeki her üründen birer tane satıldığı anlamına gelir.

Kısa ayrım:

```text
sellOne.accept(soldProduct) → Yalnızca seçilen üründen bir tane sat
stocks.forEach(sellOne)     → Listedeki her üründen birer tane sat
```

## 4. Yerel değişken adı ile nesnenin ürün adı aynı şey değildir

Şu satırda `iphone`, bir `Stock` nesnesini tutan yerel değişkenin adıdır:

```java
Stock iphone = stocks.get(0);
```

Nesnenin içindeki ürün adı ise `productName` alanında saklanır:

```java
iphone.getProductName()
```

Değişkenin adı farklı seçilebilir:

```java
Stock selectedProduct = stocks.get(0);
sellOne.accept(selectedProduct);
```

Hatta aşağıdaki kod da teknik olarak çalışır:

```java
Stock kalem = new Stock("iphone", 5);
sellOne.accept(kalem);
```

Burada:

```text
Yerel değişkenin adı = kalem
Nesnenin productName değeri = "iphone"
```

Java, Consumer'ı çalıştırırken yerel değişkenin adına bakmaz. Önemli olan
`accept()` metoduna verilen nesnenin türüdür.

## 5. `accept()` metoduna tam olarak ne geçirilir?

Consumer şu şekilde tanımlandı:

```java
Consumer<Stock> sellOne
```

`<Stock>`, `accept()` metodunun bir `Stock` nesnesi alacağını belirtir:

```java
sellOne.accept(/* buraya bir Stock nesnesi gelir */);
```

Örneğin:

```java
Stock iphone = new Stock("iphone", 5);
sellOne.accept(iphone);
```

`iphone` değişkeninin tuttuğu nesne iki bilgiyi birlikte taşır:

```text
Stock nesnesi
├── productName = "iphone"
└── quantity = 5
```

Lambda içindeki `stock` parametresi, `accept()` metoduna verilen aynı nesneyi
karşılar:

```java
Consumer<Stock> sellOne = stock -> {
    System.out.println(stock.getProductName());

    if (stock.getQuantity() > 0) {
        stock.setQuantity(stock.getQuantity() - 1);
    }
};
```

Veri akışı:

```text
sellOne.accept(iphone)
               │
               ▼
Lambda içindeki stock
               │
               ├── getProductName()
               ├── getQuantity()
               └── setQuantity(...)
```

Burada `accept()` metoduna yalnızca `"iphone"` metni gönderilmez. Ürün adının
yanında stok miktarına da ihtiyaç olduğu için bütün `Stock` nesnesi gönderilir.

Yalnızca ürün adını işleyen bir Consumer olsaydı tipi farklı olurdu:

```java
Consumer<String> printProductName =
        name -> System.out.println(name);

printProductName.accept("iphone");
```

Fakat `String` yalnızca ürün adını taşır. Stok miktarını azaltabilmek için
`Consumer<Stock>` gerekir.

## Kısa özet

```text
Consumer<Stock> sellOne
        │
        ├── Stock nesnesi alır
        ├── quantity değerini kontrol eder
        ├── stok varsa quantity değerini bir azaltır
        └── sonuç döndürmez

sellOne.accept(stock) → Tek Stock nesnesi üzerinde çalışır
stocks.forEach(sellOne) → Listedeki her Stock için accept() çağırır
```

Türlere göre `accept()` örnekleri:

```text
Consumer<Stock>  → accept() bir Stock nesnesi alır
Consumer<String> → accept() bir String alır
Consumer<Long>   → accept() bir Long alır
```
