# Alıştırma Cevapları: Koleksiyonu Dönüştürmek

Karar şeması:

```text
Her elemandan yeni bir değer üret, YENİ bir liste lazım → map(...).toList()
Sadece ekrana yazdır, liste lazım değil                → map(...).forEach(...)
Elemanın TÜRÜ değişiyor (String -> int, Product -> Dto) → map() (int için mapToInt)
"map henüz çalışmadı, çalıştırılmadı" farkı             → terminal operation gelene kadar LAZY
```

---

### 1. Ürün İsimlerini Büyük Harfe Çevirme

```java
List<String> upperCaseNames = productNames.stream()
        .map(String::toUpperCase)
        .toList();
```

**Neden:** "YENİ bir liste" ve "başka bir modüle gönderilecek" ifadeleri,
sonucun bir KOLEKSİYON olarak toplanması gerektiğini söylüyor —
`forEach` ile yazdırmak yeterli olmaz, `toList()` (terminal operation,
sonuç toplayan) gerekir.

---

### 2. Çalışan İsimlerini Ayıklama

```java
List<String> employeeNames = employees.stream()
        .map(Employee::name)
        .toList();
```

**Neden:** Bu, kitaptaki "projection" kavramı — nesnenin TAMAMINI değil,
tek bir alanını (`name`) çıkarıp yeni bir listede topluyoruz. Girdi tipi
(`Employee`) ile çıktı tipi (`String`) farklı; `map()` bunu doğal olarak
destekler.

---

### 3. Sipariş Tutarlarından Yeni Bir Sayı Listesi

```java
List<Double> orderTotals = orders.stream()
        .map(Order::totalAmount)
        .toList();
```

**Neden:** Soru 2 ile aynı kalıp; her siparişten tek bir sayısal değer
çıkarılıp yeni bir listede toplanıyor.

---

### 4. Döviz Kuruyla Fiyat Güncelleme

```java
List<Double> pricesInUsd = pricesInTl.stream()
        .map(price -> price / exchangeRate)
        .toList();
```

**Neden:** "Orijinal TL listesi değişmemeli" cümlesi kritik — `map()`
zaten kaynak listeye DOKUNMAZ, her elemandan YENİ bir değer üretip YENİ
bir stream/liste oluşturur. Kaynak `pricesInTl` bu işlemden sonra da
aynen durur.

---

### 5. İsim Uzunluklarından İstatistik Listesi

```java
List<Integer> nameLengths = usernames.stream()
        .map(String::length)
        .toList();

// ya da doğrudan sayısal işlem yapılacaksa autoboxing'den kaçınmak için:
int[] lengths = usernames.stream()
        .mapToInt(String::length)
        .toArray();
```

**Neden:** Girdi (`String`) ile çıktı (`int`/`Integer`) tipi farklı — bu,
`Transform.java`'daki `map(String::length)` örneğiyle birebir aynı. Sonuç
üzerinde toplama/ortalama gibi sayısal işlemler de yapılacaksa
`mapToInt` autoboxing maliyetinden kaçınır.

---

### 6. Yanlış Kurgulanmış Bir Rapor Listesi — TUZAK

```java
// Sorudaki (çalışan ama önerilmeyen) yaklaşım:
List<String> result = new ArrayList<>();
productNames.forEach(name -> result.add(name.toUpperCase()));

// Tercih edilen:
List<String> result = productNames.stream()
        .map(String::toUpperCase)
        .toList();
```

**Neden:** Bu, `Transform.java`'da "BAD IDEA" diye işaretlenen kalıbın
birebir aynısı — `forEach()` içinde DIŞARIDAKİ mutable bir listeyi
değiştirmek. Çalışır, ama niyeti ("her elemandan yeni bir değer üret ve
topla") `map().toList()` çok daha doğrudan ifade eder; ayrıca paralel
stream'lerde bu kalıp güvenli değildir (`ArrayList` thread-safe değildir).

---

### 7. Kelime Sayısı Listesi

```java
List<Integer> wordCounts = sentences.stream()
        .map(sentence -> sentence.split(" ").length)
        .toList();
```

**Neden:** Her cümleden TEK bir sayı (kelime sayısı) hesaplanıp yeni bir
listede toplanıyor — Soru 5 ile aynı "projection" mantığı, farklı
hesaplama.

---

### 8. Doğum Tarihinden Yaş Listesi

```java
List<Integer> ages = employees.stream()
        .map(e -> Period.between(e.birthDate(), LocalDate.now()).getYears())
        .toList();
```

**Neden:** "Çalışan nesnelerinin kendisi değişmeyecek" cümlesi, `map()`'in
zaten garanti ettiği bir şeyi (kaynak elemanları değiştirmez, yeni değer
üretir) vurguluyor — doğrudan `map().toList()`.

---

### 9. Farklı Bir Görüntüleme Nesnesine Dönüştürme

```java
record ProductView(String name, double price) {}

List<ProductView> views = products.stream()
        .map(p -> new ProductView(p.name(), p.price()))
        .toList();
```

**Neden:** Burada `map()`'in en genel hâli kullanılıyor: girdiden
(`Product`) TAMAMEN FARKLI bir nesne (`ProductView`) üretiliyor. Bu,
`map()`'in sadece "aynı tipte küçük bir değişiklik" değil, "herhangi bir
dönüşüm" için kullanılabildiğini gösteren en net örnek.

---

### 10. Henüz Çalışmayan Bir Dönüşüm — TUZAK

**Cevap:** Hiçbiri. `map(...)` bir ara işlemdir (intermediate operation)
ve LAZY çalışır — `forEach()`, `toList()`, `collect(...)` gibi bir
terminal işlem çağrılmadığı sürece pipeline hiç çalıştırılmaz, kaynak
liste hiç dolaşılmaz.

```java
Stream<String> upperCaseStream = productNames.stream()
        .map(String::toUpperCase);
// Bu satırdan sonra TEK BİR isim bile büyük harfe çevrilmiş değildir.
```

**Neden:** Bu, `TransformStreamExample.java` ve `transform-explanation.md`
içinde ayrıntılıca işlenen konu — `map()` bir "dönüşüm tarifi" kurar,
gerçek dolaşma yalnızca bir terminal operation geldiğinde başlar. Terminal
operation hiç çağrılmazsa (soruda olduğu gibi), tarif kurulmuş ama HİÇ
UYGULANMAMIŞ olur.

---

## Genel özet

```text
map(fonksiyon)              → her elemandan yeni bir değer üretir (ara işlem, lazy)
map(...).toList()           → sonuçları YENİ bir listede toplar (terminal)
map(...).forEach(...)       → sonuçları toplamadan işler/yazdırır (terminal)
mapToInt/mapToDouble(...)   → primitive sonuç, autoboxing'den kaçınmak için
forEach + dış listeye ekleme → çalışır ama BAD IDEA; map().toList() tercih edilir
terminal operation çağrılmazsa → dönüşüm hiç gerçekleşmez
```
