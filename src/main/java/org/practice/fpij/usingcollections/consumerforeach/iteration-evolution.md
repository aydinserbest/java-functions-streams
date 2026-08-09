# Java'da Koleksiyon Üzerinde Dolaşmanın Evrimi

`Iteration.java` sınıfındaki örnekler, bir listedeki elemanları dolaşma biçiminin
klasik `for` döngüsünden `forEach()` ve method reference kullanımına doğru nasıl
sadeleştiğini gösterir.

```text
Klasik for
    ↓
Enhanced for
    ↓
forEach + anonymous Consumer
    ↓
forEach + lambda
    ↓
forEach + method reference
```

## 1. Klasik `for` döngüsü

```java
for (int i = 0; i < friends.size(); i++) {
    System.out.println(friends.get(i));
}
```

Bu kullanımda dolaşmanın bütün ayrıntılarını geliştirici yönetir:

- Sayaç oluşturulur.
- Listenin boyutu kontrol edilir.
- `i` değeri her turda artırılır.
- Eleman `friends.get(i)` ile alınır.

Buradaki düşünce şöyledir:

> Listenin içinde hangi sırayla ilerleyeceğimi ben yöneteceğim.

## 2. Enhanced `for` döngüsü

```java
for (String name : friends) {
    System.out.println(name);
}
```

Sayaç, sınır kontrolü ve `get(i)` çağrısı ortadan kalkar. Java'ya artık daha sade
bir istek verilir:

> Listedeki her ismi sırayla bana ver.

Döngüyü hâlâ geliştirici yazar fakat listenin indeksleriyle ilgilenmek zorunda
kalmaz.

## 3. `forEach()` ve anonymous `Consumer`

```java
friends.forEach(new Consumer<String>() {
    @Override
    public void accept(String name) {
        System.out.println(name);
    }
});
```

Burada önemli bir düşünce değişimi gerçekleşir:

> Listeyi nasıl dolaşacağını ben yazmayayım. Koleksiyon kendi elemanlarını
> dolaşsın; ben yalnızca her elemana ne yapılacağını söyleyeyim.

`forEach()` parametre olarak bir `Consumer<String>` alır. Listedeki her isim için
Consumer'ın `accept()` metodunu çağırır:

```text
accept("Alice")   -> Alice yazdırılır
accept("Bob")     -> Bob yazdırılır
accept("Charlie") -> Charlie yazdırılır
accept("David")   -> David yazdırılır
```

Consumer bir değer döndürmez. Her ismi ekrana yazdırmak, yapılan işlemin
kendisidir.

Anonymous class yazımı uzundur fakat lambda'nın hangi interface ve metot
sözleşmesinden geldiğini açıkça gösterir:

```text
Consumer<String>
        ↓
void accept(String name)
```

## 4. `forEach()` ve lambda

```java
friends.forEach(name -> System.out.println(name));
```

Bu ifade, anonymous Consumer kullanımının kısa yazımıdır:

```text
Consumer<String>.accept(String name)
                  ↓
name -> System.out.println(name)
```

`forEach()` dolaşmayı yönetmeye devam eder. Lambda ise her isim geldiğinde
çalıştırılacak davranışı temsil eder.

Lambda referansı verildiği anda isimler yazdırılmaz. `forEach()`, listedeki
elemanları dolaşıp bu davranışı her eleman için çalıştırdığında çıktı oluşur.

## 5. Method reference

```java
friends.forEach(System.out::println);
```

Bu kullanım aşağıdaki lambdanın daha kısa yazımıdır:

```java
name -> System.out.println(name)
```

Lambda yalnızca aldığı değeri mevcut bir metoda gönderiyorsa method reference
kullanılabilir:

```text
forEach() ismi verir
        ↓
System.out.println(isim) çalışır
        ↓
İsim ekrana yazdırılır
```

Lambda ve method reference aynı business davranışını gerçekleştirir. Değişen
yalnızca Java'daki yazım biçimidir.

## External iteration ve internal iteration

Klasik ve enhanced `for` döngülerinde dolaşma kodu metodun içinde açıkça
bulunur. Geliştirici koleksiyondan elemanları dışarı alarak işlemi yönetir. Bu
yaklaşım **external iteration** olarak adlandırılır.

```text
Koleksiyonu nasıl dolaşacağımı ben yöneteyim.
```

`forEach()` kullanımında ise koleksiyona yalnızca yapılacak davranış verilir.
Elemanların dolaşılmasını `forEach()` yönetir. Bu yaklaşım **internal iteration**
olarak adlandırılır.

```text
Her elemana ne yapılacağını ben söyleyeyim;
dolaşmayı koleksiyon yönetsin.
```

## Kısa sonuç

Bu örneklerin tamamı aynı çıktıyı üretir:

```text
Alice
Bob
Charlie
David
```

Evrim sırasında değişen business sonucu değil, geliştiricinin dolaşma işlemini
ifade etme biçimidir:

```text
Nasıl dolaşılacağını ayrıntılı biçimde yazmak
                    ↓
Her elemana uygulanacak davranışı bildirmek
```
