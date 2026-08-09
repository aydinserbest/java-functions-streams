# Alıştırma Cevapları: Consumer<T> ve forEach()

`questions.md` içindeki 8 sorunun çözüm yaklaşımı ve gerekçesi aşağıda.
Karar vermeden önce kendinize şu soruyu sorun:

```text
Bu davranış bana YENİ bir değer mi verecek,
yoksa var olan bir şey üzerinde iş mi yapıp hiçbir şey döndürmeyecek?
```

- **Sonuç döndürmüyor, sadece iş yapıyor (yazdırma, mutasyon, loglama)**
  → `Consumer<T>`
- **Yeni bir değer/nesne üretip döndürüyor**
  → `Consumer` DEĞİL, `Function<T,R>` (koleksiyon için `map()`)

---

### 1. Sipariş Onay Bildirimi

**Çözüm:** `Consumer<Order>` + `forEach()`

```java
Consumer<Order> notify = order ->
        System.out.println("Siparişiniz hazırlanıyor: #" + order.id());

orders.forEach(notify);
```

**Neden:** Her sipariş için yapılan iş (ekrana yazdırma) bir sonuç
üretmiyor, sadece bir aksiyon. Bu, `Consumer`'ın tanımıyla birebir
örtüşüyor: "T al, işlem yap, sonuç döndürme". Koleksiyonun HER elemanına
uygulanacağı için `forEach()` gerekiyor — `accept()`'i tek tek elle
çağırmak aynı işi yapardı ama gereksiz tekrar olurdu.

---

### 2. Tek Müşteriye Karşılama Mesajı

**Çözüm:** `Consumer<Customer>` + tek başına `accept()`

```java
Consumer<Customer> welcome = customer ->
        System.out.println("Hoş geldin " + customer.name());

welcome.accept(newCustomer);
```

**Neden:** Burada bir KOLEKSİYON yok, sadece TEK bir nesne var. `forEach()`
bir `Iterable`/`Stream` üzerinde çalışır; elimizde dolaşılacak bir liste
olmadığı için `forEach()`'e gerek yok. Davranışı doğrudan o tek müşteri
için `accept()` ile çalıştırmak yeterli — `CustomerDemo`'daki kalıp.

---

### 3. Kampanya Kapsamında Fiyat Güncelleme

**Çözüm:** `Consumer<Product>` + `forEach()`, setter ile mutasyon

```java
Consumer<Product> applyDiscount = product ->
        product.setPrice(product.getPrice() * 0.85);

products.forEach(applyDiscount);
```

**Neden:** "Ürünü yeni bir nesneye dönüştürmeden, doğrudan kendi üzerinde
güncelle" cümlesi kritik — bu, mevcut nesnenin MUTASYONA uğratılacağını,
YENİ bir nesne/liste dönmeyeceğini açıkça söylüyor. Mevcut nesneyi
değiştirip sonuç döndürmemek tam olarak `Consumer`'ın işi (bkz.
`StockDemo` + `choosing-consumer-or-function-for-stock-sale.md`'deki
aynı ayrım).

---

### 4. Düşük Stok Uyarı Kaydı

**Çözüm:** `Consumer<Product>` + `forEach()`, içeride `if`

```java
Consumer<Product> warnIfLow = product -> {
    if (product.getQuantity() < 5) {
        System.out.println("Uyarı: " + product.getName() + " kritik seviyede");
    }
};

warehouse.forEach(warnIfLow);
```

**Neden:** İçeride bir `if` olması bunu `Predicate` yapmaz — `Predicate`
`boolean` DÖNDÜRMELİ, burada hiçbir şey döndürülmüyor. `if`, `Consumer`'ın
yapacağı işin içindeki bir iş kuralı (eşik altındaysa yazdır, değilse
sessiz kal); tıpkı `StockDemo`'daki `sellOne`'ın içindeki `if
(quantity > 0)` gibi. Karar: hâlâ `Consumer<Product>`.

---

### 5. Sepet Davranışının Hem Tekil Hem Toplu Çalıştırılması

**Çözüm:** Davranışı BİR KEZ bir `Consumer<CartItem>` değişkeninde
tanımlayıp, hem `accept()` hem `forEach()` ile kullanmak.

```java
Consumer<CartItem> reserveAndLog = item -> {
    item.reduceStock();
    System.out.println(item.productName() + " için stok düşüldü");
};

// Tek bir ürün anlık eklendiğinde:
reserveAndLog.accept(addedItem);

// Gün sonunda bekleyen tüm sepetler için:
pendingCartItems.forEach(reserveAndLog);
```

**Neden:** Bu sorunun asıl noktası "aynı davranışın tekrar yazılmaması" —
tam olarak `functional-interface-implementation-forms.md`'deki temayı
("davranışı bir değer gibi taşımak") uyguluyoruz. Davranış bir `Consumer`
değişkeninde saklanır; `accept()` onu TEK bir nesnede, `forEach()` bir
KOLEKSİYONUN tamamında çalıştırır — ikisi de aynı `Consumer` nesnesini
kullanır, kod tekrarı olmaz.

---

### 6. Fatura KDV Hesaplama Raporu — TUZAK

**Çözüm:** Bu `Consumer` DEĞİL, `Function<Invoice, BigDecimal>` (ve
`map()` + `toList()`).

```java
Function<Invoice, BigDecimal> withVat = invoice ->
        invoice.amount().multiply(BigDecimal.valueOf(1.20));

List<BigDecimal> amountsWithVat = invoices.stream()
        .map(withVat)
        .toList();
```

**Neden:** Senaryo açıkça "bu tutarların yer aldığı YENİ bir liste
istiyorlar" ve "mevcut fatura nesneleri hiç değişmeyecek" diyor. Bu iki
cümle birlikte `Consumer`'ın tanımına (sonuç döndürmez, mevcut nesneyi
değiştirir) TERS düşüyor. Burada ihtiyaç "her girdiden bir çıktı üret"
— bu `Function`'ın işi, koleksiyon seviyesinde karşılığı `map()`'tir.
`Consumer`'ı buraya zorlarsanız (örn. dışarıdaki bir listeye elle
`add()` yaparak) çalışır ama yanlış aracı zorlamış olursunuz — bkz. Soru 8.

---

### 7. Bildirimleri Okundu Olarak İşaretleme

**Çözüm:** `Consumer<Notification>` — ve muhtemelen method reference.

```java
Consumer<Notification> markRead = Notification::markAsRead;

notifications.forEach(markRead);
```

**Neden:** "Her bildirim nesnesinin KENDİ ÜZERİNDE zaten var olan
davranışın çalıştırılması" cümlesi, `Stock::sellOne` ile aynı kalıp
(bkz. `functional-interface-implementation-forms.md`). `markAsRead()`
zaten `void` döndüren, nesnenin kendi durumunu güncelleyen bir metot
olduğu için lambda yazmaya bile gerek yok — method reference doğrudan
`Consumer<Notification>` sözleşmesine uyuyor. Yeni liste istenmiyor,
mevcut nesneler güncelleniyor → `Consumer`, `Function` değil.

---

### 8. Şüpheli İşlem Listesi Biriktirme — TUZAK (anti-pattern)

**Çözüm:** Teknik olarak `Consumer` + `forEach()` ile ÇALIŞIR ama
ÖNERİLMEZ; doğru araç `filter()` + `collect()`/`toList()`'tir.

```java
// Çalışır ama kötü tasarım (BAD IDEA):
List<Transaction> suspicious = new ArrayList<>();
transactions.forEach(tx -> {
    if (tx.amount() > 10_000) {
        suspicious.add(tx);
    }
});

// Tercih edilen:
List<Transaction> suspicious = transactions.stream()
        .filter(tx -> tx.amount() > 10_000)
        .toList();
```

**Neden:** Sorudaki "listedeki her kayda bak, şartı sağlıyorsa dışarıdaki
başka bir listeye ekle" cümlesi, ilk bakışta bir `Consumer` gibi görünür
(sonuç döndürmüyor, iş yapıyor) — ve gerçekten de derlenir, çalışır. Ama
bu, `forEach()` içinde DIŞARIDAKİ mutable bir listeyi değiştirmek demektir
(`transformingcollection` paketindeki `Transform.java`'da "BAD IDEA" diye
işaretlenen kalıbın birebir aynısı). Sorun: niyet ("koşula uyanları seç")
`filter()` ile çok daha doğrudan ifade edilir, ve paralel stream'lerde bu
`forEach` + dış liste kalıbı güvenli değildir. Bu soru, "her yeni liste
ihtiyacı `Consumer`'a zorlanmamalı" mesajını pekiştirmek için bilinçli
olarak eklendi.

---

## Genel özet

```text
Tek nesne, sonuç yok           → Consumer<T> + accept()
Koleksiyonun tamamı, sonuç yok → Consumer<T> + forEach()
Yeni bir değer/liste isteniyor → Function<T,R> + map()/collect()
Mevcut nesnenin kendi metodu   → method reference: Consumer<T> = Type::method
Davranış birden fazla yerde    → Consumer'ı değişkende sakla, tekrar kullan
"Şarta göre seç" ihtiyacı      → filter(), forEach() içine gizlenmiş bir if + add() değil
```
