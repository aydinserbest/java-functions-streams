# Alıştırma Cevapları: Elemanları Birleştirme

Karar şeması:

```text
Elemanları OLDUĞU GİBİ, sadece ayraçla birleştir        → String.join(ayraç, liste)
Önce dönüştür (map), SONRA birleştir                     → stream().map(...).collect(joining(ayraç))
Ayraca ek olarak başa/sona bir şey eklenecek              → joining(ayraç, prefix, suffix) ya da StringJoiner
Boş liste durumunda özel bir metin gösterilecek           → StringJoiner + setEmptyValue(...)
Ayrı ayrı biriktirilmiş iki sonucu birleştirmek gerekiyor → StringJoiner + merge(...)
```

---

### 1. Rapor Satırı

```java
System.out.println(String.join(", ", soldProductNames));
```

**Neden:** Elemanlar (ürün isimleri) dönüştürülmeden, olduğu gibi
birleştirilecek. En kısa ve doğrudan araç `String.join`.

---

### 2. Boş Sepet Mesajı

```java
StringJoiner joiner = new StringJoiner(", ", "[", "]");
joiner.setEmptyValue("Sepetiniz boş");
cartItems.forEach(joiner::add);
System.out.println(joiner);
```

**Neden:** Hem prefix/suffix (`[...]`) hem de "liste boşsa TAMAMEN farklı
bir metin göster" ihtiyacı var. `String.join` ve `Collectors.joining` boş
koleksiyonda sadece `[]` (ya da hiç prefix/suffix yoksa boş string) verir,
özel bir metin göstermez. Bu ihtiyacı karşılayan tek araç `StringJoiner`'ın
`setEmptyValue(...)`'udur.

---

### 3. Toplu Sorgu İfadesi

```java
String ids = orderIds.stream()
        .map(String::valueOf)
        .collect(Collectors.joining(", ", "(", ")"));
```

**Neden:** Sayıları önce metne çevirmek (`map`) ve sonuca hem ayraç hem de
başa/sona parantez eklemek gerekiyor — `joining(delimiter, prefix, suffix)`
overload'u tam olarak bunu yapar.

---

### 4. Parti Referans Kodu

```java
String batchCode = productCodes.stream()
        .map(String::toUpperCase)
        .collect(Collectors.joining("-"));
```

**Neden:** Önce her kod DÖNÜŞTÜRÜLÜYOR (büyük harfe), sonra birleştiriliyor.
`String.join` dönüştürme yapamaz (elemanları olduğu gibi alır), bu yüzden
`map(...).collect(joining(...))` gerekiyor.

---

### 5. Dosya Yolu Oluşturma

```java
System.out.println(String.join("/", folderNames));
```

**Neden:** Klasör isimleri dönüştürülmeden, sadece `/` ile ayrılacak.
Basit `String.join` yeterli.

---

### 6. Log Satırı

```java
String logLine = String.join(" | ", date, user, action);
```

**Neden:** Üç ayrı String değeri (varargs olarak) sabit bir ayraçla
birleştirilecek; `String.join`'in varargs overload'u tam bu ihtiyaç için.

---

### 7. Şubeler Arası Liste Birleştirme

```java
StringJoiner branch1 = new StringJoiner(", ");
branch1Vips.forEach(branch1::add);

StringJoiner branch2 = new StringJoiner(", ");
branch2Vips.forEach(branch2::add);

branch1.merge(branch2);
System.out.println(branch1);
```

**Neden:** Elimizde ZATEN AYRI AYRI biriktirilmiş iki sonuç var ve bunları
tek bir sonuçta toplamamız gerekiyor. `String.join`/`Collectors.joining`
ham koleksiyonlardan tek seferde birleştirmeye uygundur; iki ayrı
biriktirilmiş sonucu birleştirmek `StringJoiner.merge(...)`'ün işi.

---

### 8. Etiket Gösterimi

```java
String tags = tagList.stream()
        .map(tag -> "#" + tag)
        .collect(Collectors.joining(" "));
```

**Neden:** Her elemana ayrı ayrı bir önek (`#`) eklenmesi gerekiyor — bu,
`joining(delimiter, prefix, suffix)`'teki TEK BİR global prefix/suffix ile
karışmamalı; burada her elemanın kendisi dönüştürülüyor (`map`), sonra
sadece aralarına boşluk konularak birleştiriliyor.

---

### 9. Fazladan Ayraç Sorunu

```java
System.out.println(String.join(", ", names));
```

**Neden:** Bu, `joiningelements` paketindeki `PrintListImperative`'te
gösterilen klasik tuzağın ta kendisi — for döngüsünde her elemandan sonra
ayraç eklemek, son elemanda da fazladan ayraç bırakır. Çözüm elle son
elemanı ayırmak değil, `String.join`'in bu sorunu zaten çözmüş olmasıdır.

---

### 10. Kargo Takip Kodu

```java
String trackingCode = String.join("-", countryCode, year, sequenceNumber);
```

**Neden:** Üç sabit parça, sabit bir ayraçla birleştiriliyor; dönüştürme
yok, prefix/suffix yok — en yalın araç `String.join` varargs kullanımı.

---

## Genel özet

```text
String.join(ayraç, ...)                → dönüştürmeden, düz birleştirme
map(...).collect(joining(ayraç))       → önce dönüştür, sonra birleştir
joining(ayraç, prefix, suffix)         → TEK bir global başlangıç/bitiş eklemek
StringJoiner + setEmptyValue(...)      → boş koleksiyon için özel mesaj
StringJoiner + merge(...)              → ayrı biriktirilmiş sonuçları birleştirmek
```
