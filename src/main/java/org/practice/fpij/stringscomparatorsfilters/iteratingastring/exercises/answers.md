# Alıştırma Cevapları: Bir Metni Karakter Karakter İşlemek

Hepsinin ortak temeli: `metin.chars()` bir `IntStream` verir; bu akış
üzerinde `filter`, `map`/`mapToObj`, `count`, `anyMatch`/`allMatch`,
`forEach` ve method reference'lar (`Character::isDigit`,
`Character::isWhitespace`, ...) ile ihtiyaca göre süzme/sayma/dönüştürme
yapılır.

Karar şeması:

```text
Tüm karakterler bir şartı sağlıyor mu?  → allMatch(...)
Hiç karakter bir şartı sağlıyor mu?     → anyMatch(...)
Şartı sağlayan karakter SAYISI          → filter(...).count()
Şartı sağlayan karakterleri METİN yap   → filter(...).mapToObj(...).collect(...)/toString
Her karakter için bir işlem yap         → forEach(...)
```

---

### 1. PIN Doğrulama

```java
boolean gecerliPin = pin.chars().allMatch(Character::isDigit);
```

**Neden:** "SADECE rakamlardan oluşmalı" -- her karakterin şartı
sağlaması gerekiyor. Bu, `allMatch`'in tanımı: akıştaki HER eleman şartı
sağlıyorsa `true` döner (boş akışta da `true` döner, ama PIN boş
olamayacağı için bu bir sorun değil).

---

### 2. Şifre Güç Kontrolü

```java
long rakamSayisi = sifre.chars().filter(Character::isDigit).count();
boolean yeterinceGuclu = rakamSayisi >= 2;
```

**Neden:** Burada evet/hayır değil, önce bir SAYI (rakam sayısı) lazım,
sonra bu sayı bir eşikle karşılaştırılıyor -- `filter(...).count()`
kalıbı tam olarak bunu yapar.

---

### 3. Kategori Kodu Raporu

```java
String sadeceHarfler = urunKodu.chars()
        .filter(Character::isLetter)
        .mapToObj(ch -> String.valueOf((char) ch))
        .collect(java.util.stream.Collectors.joining());
```

**Neden:** Sonuç bir sayı ya da evet/hayır değil, süzülmüş yeni bir
METİN. `filter` ile harf olmayanları eledikten sonra, int'leri gerçek
karakterlere çevirmek için `mapToObj` gerekiyor (aynı
`IterateStringAsCharacters`'taki gibi), sonra bunları tek bir String'te
birleştirmek için `joining()`.

---

### 4. Telefon Numarasını Normalleştirme

```java
String temizNumara = numara.chars()
        .filter(ch -> !Character.isWhitespace(ch))
        .mapToObj(ch -> String.valueOf((char) ch))
        .collect(java.util.stream.Collectors.joining());
```

**Neden:** Soru 3 ile aynı kalıp (süzülmüş metni yeniden oluşturma), ama
burada tersine bir şart var -- "boşluk OLMAYANLARI" tut, yani `filter`
içinde `!Character.isWhitespace(ch)`.

---

### 5. Bağırarak Yazılmış Başlığı Tespit Etme

```java
long buyukHarfSayisi = baslik.chars().filter(Character::isUpperCase).count();
boolean bagirarakYazilmis = buyukHarfSayisi > 5;
```

**Neden:** Yine bir EŞİKLE karşılaştırılacak bir SAYI lazım -- Soru 2 ile
birebir aynı mantık, sadece şart `isUpperCase`.

---

### 6. Başlık İçin Sesli Harf Sayacı

```java
String sesliler = "aeiouAEIOU";
long sesliSayisi = baslik.chars()
        .filter(ch -> sesliler.indexOf(ch) >= 0)
        .count();
```

**Neden:** `Character` sınıfında hazır bir "sesli harf mi" metodu
olmadığı için burada `filter`'a kendi lambda'mızı yazıyoruz (method
reference kullanamayacağımız bir durum) -- yine de kalıp aynı:
`filter(...).count()`.

---

### 7. Zayıf Güvenlik Sorusu Cevabını Tespit Etme

```java
boolean rakamIceriyor = cevap.chars().anyMatch(Character::isDigit);
boolean zayif = rakamIceriyor;
```

**Neden:** "HİÇBİR rakam BULUNMAMALI" cümlesinin tespiti aslında "en az
bir rakam VAR MI?" sorusuna eşdeğer -- bu da `anyMatch`'in tanımı: akışta
şartı sağlayan EN AZ BİR eleman varsa `true` döner. `allMatch` burada
YANLIŞ olurdu (tüm karakterlerin rakam olmasını değil, hiç rakam
olmamasını arıyoruz).

---

### 8. Seri Numarası Format Doğrulama

```java
long rakamSayisi = seriNo.chars().filter(Character::isDigit).count();
boolean gecerliFormat = rakamSayisi == 6;
```

**Neden:** Soru 2 ve 5 ile aynı kalıp -- burada eşitlik kontrolü var
(`== 6`), eşik değil, ama yine `filter(...).count()` temelinde.

---

### 9. Token'ı Log İçin Okunabilir Yazdırma

```java
token.chars()
        .mapToObj(ch -> (char) ch)
        .forEach(System.out::println);
```

**Neden:** Amaç bir sonuç DEĞİL, her karakter için bir YAN ETKİ (ekrana
yazdırma) -- `forEach` burada doğru terminal işlem. `chars()` int
döndürdüğü için doğrudan `forEach(System.out::println)` yazarsak sayılar
görünür (`IterateStringNumericOutput`'taki sürpriz); okunabilir çıktı
için önce `mapToObj` ile gerçek karaktere çevrilmeli.

---

### 10. Boşluksuz Kullanıcı Adı Kontrolü

```java
boolean gecerliKullaniciAdi = kullaniciAdi.chars().noneMatch(Character::isWhitespace);
```

**Neden:** "HİÇBİR boşluk OLMAMALI" -- `noneMatch`, `anyMatch`'in tam
tersidir: şartı sağlayan HİÇBİR eleman yoksa `true` döner. Aynı sonuç
`!kullaniciAdi.chars().anyMatch(Character::isWhitespace)` ile de elde
edilebilirdi, ama `noneMatch` niyeti daha açık ifade eder.

---

## Genel özet

```text
chars()                          → String'i IntStream'e çevirir (her karakterin int kodu)
mapToObj(ch -> (char) ch)        → int'i gerçek char/Character'a çevirir
filter(sart).count()             → şartı sağlayan karakter SAYISI
filter(sart).mapToObj(...).join  → şartı sağlayan karakterlerden yeni bir METİN
allMatch(sart)                   → TÜM karakterler şartı sağlıyor mu?
anyMatch(sart)                   → EN AZ BİR karakter şartı sağlıyor mu?
noneMatch(sart)                  → HİÇBİR karakter şartı sağlamıyor mu?
forEach(...)                     → her karakter için bir yan etki (yazdırma vb.)
```
