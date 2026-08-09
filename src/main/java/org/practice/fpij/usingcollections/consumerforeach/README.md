# Consumer<T> ve forEach(): Davranışı Değer Gibi Taşımak

Bu paketteki tüm örnekler tek bir çekirdek fikri işliyor: bir işlemi hemen
çalıştırmak yerine, `Consumer<T>` ile **davranışın kendisini** bir değişkende
tutup istediğimiz zaman (`accept()` ile tek bir nesneye, `forEach()` ile bir
koleksiyonun tamamına) çalıştırabiliriz.

```text
Consumer<T>: T alır, bir işlem yapar, sonuç döndürmez.
```

## Okuma sırası

### 1. Temel: davranışı tutmak ve çalıştırmak

- **`CustomerDemo`** — en yalın hâli: `Consumer<String>` oluşturup
  `accept()` ile elle çağırma. Henüz `forEach()` yok, koleksiyon yok;
  sadece "Consumer nedir, `accept()` ne işe yarar" sorusuna cevap.
- **`OrderDemo`** — bir adım ileri: aynı `Consumer`, artık `forEach()` ile
  bir `List<Long>`'un her elemanına otomatik uygulanıyor. `forEach()`'in
  arka planda her eleman için `accept()`'i nasıl çağırdığı yorumlarda
  anlatılıyor.
- **`CustomerEmailDemo`** — `OrderDemo` ile aynı kalıp, bu sefer bir
  `record Customer(name, email)` üzerinde; iş senaryosu biraz daha
  gerçekçi (toplu e-posta gönderimi).

### 2. Dolaşmanın evrimi

- **`Iteration`** — klasik `for` → enhanced `for` → `forEach` + anonymous
  `Consumer` → lambda → method reference sıralamasını TEK dosyada,
  hepsi aynı çıktıyı üretecek şekilde gösteriyor. Anonymous class'ın
  neden bu kadar "kalabalık" göründüğünü satır satır açıklayan bir not
  içeriyor.
  Detaylı anlatım: **`iteration-evolution.md`** (external vs internal
  iteration ayrımı dahil).
- **`functional-interface-implementation-forms.md`** — aynı `Consumer`
  davranışının (`Stock::sellOne` örneği üzerinden) lambda / method
  reference / anonymous class / normal class ile nasıl yazılabileceğini,
  ve lambda'nın asıl getirdiği şeyin "kısa syntax" değil "davranışı veri
  gibi taşıyabilmek" olduğunu tartışıyor.

### 3. Side-effect taşıyan bir Consumer: stok satışı

- **`Stock`** / **`StockDemo`** — `Consumer<Stock>`'un, kendisine verilen
  nesneyi (`setQuantity` ile) **mutasyona uğratan bir yan etki**
  taşıdığı, gerçekçi bir iş senaryosu (ürün satıldıkça stok azaltma).
  `sellOne.accept(tekBirÜrün)` ile `stocks.forEach(sellOne)` arasındaki
  fark ("bir tanesini sat" vs "listedeki her üründen birer tane sat")
  class içindeki yorumlarda örnekleniyor.
  Detaylı anlatım: **`consumer-stock-sale-explanation.md`**.
- **`choosing-consumer-or-function-for-stock-sale.md`** — bu senaryo
  için `Consumer<Stock>` yerine `Function<Stock, Stock>` /
  `UnaryOperator<Stock>` / `Predicate<Stock>` de kullanılabilir miydi
  sorusunu karşılaştırmalı olarak inceleyen bir tasarım notu:

  ```text
  Mevcut nesneyi değiştir, sonuç döndürme  → Consumer<Stock>
  Yeni, güncellenmiş bir nesne üret        → Function<Stock,Stock> / UnaryOperator<Stock>
  Sadece evet/hayır sorusu sor             → Predicate<Stock>
  ```

  Kısaca: `Stock` **mutable** bir sınıf olduğu ve setter kullanıldığı için
  `Consumer` en doğal seçim; ama `Stock` bir `record` (immutable) olsaydı
  yeni nesne döndüren `UnaryOperator<Stock>` daha uygun olurdu. Notun
  sonunda gerçek bir üretim sisteminde bunun muhtemelen sonuç tipi olan
  bir servis metoduna (`SaleResult sellOne(ProductId id)`) evrileceği de
  tartışılıyor.

## Kısa özet

```text
accept(tekNesne)        → davranışı YALNIZCA o nesne için çalıştır
forEach(consumer)       → davranışı koleksiyondaki HER eleman için çalıştır
Consumer<T>              → T alır, iş yapar (çoğunlukla side effect), sonuç döndürmez
Function<T,R>/UnaryOperator<T> → T alır, YENİ bir sonuç üretip döndürür
```
