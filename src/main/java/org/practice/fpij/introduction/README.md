# Giriş: İmperatif ve Fonksiyonel/Deklaratif Stil

Bu paket, kitabın (*Functional Programming in Java*, Venkat Subramaniam) ilk
bölümüyle başlar: Java'da **imperative** (nasıl yapılacağını adım adım
anlatan) ve **functional/declarative** (ne istediğini söyleyen) stilleri
karşılaştırmak.

Üç class, aynı temayı iki farklı senaryoda gösteriyor:

```text
DiscountImperative  ┐
DiscountFunctional  ┴─ Aynı iş kuralı, iki farklı stille hesaplanıyor
Cities              ── Aynı ayrım, "arama" senaryosunda
```

## DiscountImperative / DiscountFunctional

İş ihtiyacı: fiyatı 20'den yüksek ürünlere %10 indirim uygulayıp toplamı
bulmak.

- **`DiscountImperative`**: klasik `for` döngüsü + `if` + elle biriktirilen
  bir toplam değişkeni. Kodun kendisi, sonucun **NASIL** hesaplanacağını
  adım adım anlatıyor (döngüyü başlat, her elemanı kontrol et, şartı sağlayanı
  indirimle çarp, topla).
- **`DiscountFunctional`**: `stream().filter().mapToDouble().sum()` zinciri.
  Kod artık **NE** istediğimizi söylüyor ("20'den büyük olanları seç, %10
  indirim uygula, topla"); dolaşmanın, sayaç tutmanın, biriktirmenin detayını
  Stream API üstleniyor.

İkisi de **aynı sonucu** üretir; fark, niyetin kodda ne kadar açık ve kısa
ifade edildiğidir.

## Cities

İş ihtiyacı: bir şehir listesinde "Chicago" var mı, kontrol etmek.

- **`findChicagoImperative`**: `for` döngüsü + `equals` kontrolü + `break` +
  elle tutulan bir `found` bayrağı.
- **`findChicagoDeclarative`**: `cities.contains("Chicago")` — tek satır,
  niyeti dolaysızca ifade ediyor.

Burada Stream/lambda henüz yok (JDK'nın kendi `contains()` metodu yeterli),
ama gösterdiği fikir aynı: **declarative kod, "ne" sorusuna cevap verir;
imperative kod, "nasıl" sorusuna.**

## Bu paketin asıl amacı

Kitabın geri kalanında (`usingcollections` altındaki paketler) sürekli
karşımıza çıkacak olan `filter`, `map`, `reduce`, `findFirst` gibi Stream
metotları, aslında bu iki class'ta görülen dönüşümün genelleştirilmiş
hâlleridir:

```text
İmperatif: döngü + sayaç/bayrak + şart + elle biriktirme
        ↓
Deklaratif: "ne istediğimi" tarif eden bir işlem zinciri (Stream pipeline)
```

Bu paketteki örnekler, ilerideki bölümleri okurken "bu neden daha iyi?"
sorusunun cevabını akılda tutmak için bir referans noktasıdır.
