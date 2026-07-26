# Comparable ve Comparator Alıştırmaları — Requirement'lar

Bu alıştırmaların amacı `Comparable<T>`, `Comparator<T>`, `compareTo`,
`comparing`, `comparingInt`, `reversed`, `thenComparing`, `nullsLast`, `sorted`,
`min` ve `max` kullanımlarını gerçek uygulama senaryolarıyla çalışmaktır.

Karşılaştırma sonucunun temel sözleşmesi:

```text
negatif → birinci değer önce gelir
0       → iki değer sıralama bakımından eşittir
pozitif → birinci değer sonra gelir
```

## 1. Destek taleplerinin doğal sırasını belirleme

Şu modeli oluştur:

```java
SupportTicket(long id, LocalDateTime createdAt, String subject)
```

`SupportTicket`, `Comparable<SupportTicket>` implement etsin. Doğal sırası
oluşturulma tarihine göre eskiden yeniye olmalıdır. En az dört talebi karışık
sırada oluştur ve parametresiz `stream().sorted()` ile sırala.

Beklenen davranış:

```text
09:00 oluşturulan talep
10:30 oluşturulan talep
13:15 oluşturulan talep
16:00 oluşturulan talep
```

### Business açısından burada ne yapılıyor?

Destek sisteminin temel çalışma kuralı, daha uzun süredir bekleyen müşterinin
önce ele alınmasıdır. Bu sıra yalnızca belirli bir ekran tercihi değil, ticket
domain'inin varsayılan çalışma sırasıdır. Bu nedenle doğal sıra
`SupportTicket.compareTo()` içinde tanımlanır.

Sistemin kuralları:

- Eski tarihli ticket önce gelmeli.
- Yeni tarihli ticket sonra gelmeli.
- Kaynak liste silinmemeli veya kayıtlar değiştirilmemeli.
- Parametresiz `sorted()` doğal `compareTo()` kuralını kullanmalı.

## 2. Ürünleri iki farklı ekranda farklı sıralama

Şu modeli oluştur:

```java
Product(String name, int price)
```

Product hiçbir karşılaştırma interface'i implement etmesin. İki ayrı
`Comparator<Product>` tanımla:

- `byName`: ürünleri alfabetik sıraya koysun.
- `byPrice`: ürünleri ucuzdan pahalıya sıralasın.

Aynı ürün listesini iki Comparator ile ayrı ayrı sırala.

### Business açısından burada ne yapılıyor?

Müşteri kataloğunda “Fiyata göre sırala” seçeneği bulunurken depo yönetim ekranı
ürünleri alfabetik gösterebilir. Product nesnesinin tek bir tartışmasız doğal
sırası yoktur. Aynı domain sınıfı değiştirilmeden, ekranın business ihtiyacına
göre farklı Comparator davranışları kullanılır.

## 3. Adayları puana göre yüksekten düşüğe sıralama

Şu modeli oluştur:

```java
Candidate(String name, int interviewScore)
```

`Comparator.comparingInt()` ve `reversed()` kullanarak adayları en yüksek
puandan en düşük puana sırala.

Örnek:

```text
Alice: 82
Mehmet: 95
John: 74

Sonuç: Mehmet, Alice, John
```

### Business açısından burada ne yapılıyor?

İnsan kaynakları mülakat sonuçlarını başarılı adaydan başlayarak incelemek
ister. Sayısal doğal sıra düşükten yükseğe olduğu için business ekranının
istediği yön `reversed()` ile ters çevrilir. Aday kayıtları veya puanları
değiştirilmez; yalnızca rapor sırası değişir.

## 4. Çalışanları departman ve isim sırasına koyma

Şu modeli oluştur:

```java
Employee(String name, String department)
```

Çalışanları önce departmana göre alfabetik, departman aynıysa çalışan adına göre
alfabetik sırala. `comparing()` ve `thenComparing()` kullan.

Beklenen sıralama mantığı:

```text
Engineering: Alice
Engineering: Mehmet
Sales: Bob
Sales: John
```

### Business açısından burada ne yapılıyor?

Şirket rehberi çalışanları önce organizasyon birimi altında gruplu görünüme
yakın bir sırada sunar. Aynı departmandaki kişilerin rastgele sıralanmaması için
ikinci business kuralı olarak isim kullanılır. `thenComparing()` yalnızca ilk
karşılaştırma eşit sonuç verdiğinde devreye girer.

## 5. Teslimat seçeneklerini üç kuralla sıralama

Şu modeli oluştur:

```java
DeliveryOption(String company, int estimatedDays, int price)
```

Teslimat seçeneklerini:

1. Önce tahmini gün sayısına göre artan,
2. Gün sayısı aynıysa fiyata göre artan,
3. Gün ve fiyat aynıysa şirket adına göre alfabetik

sırala.

### Business açısından burada ne yapılıyor?

Ödeme ekranında müşteriye en hızlı teslimat önce gösterilir. İki şirket aynı
sürede teslim ediyorsa ucuz olan öne çıkar; fiyat da aynıysa ekranın kararlı ve
öngörülebilir olması için şirket adı son eşitlik bozucu kuraldır. Bu soru
Comparator zincirinin bir business öncelik listesi olduğunu gösterir.

## 6. En ucuz ve en pahalı ürünü aynı Comparator ile bulma

`Product(String name, int price)` listesindeki fiyat karşılaştırmasını bir kez:

```java
Comparator<Product> byPrice
```

olarak tanımla. Aynı Comparator'ı `stream().min()` ve `stream().max()` içinde
kullanarak en ucuz ve en pahalı ürünü bul. Boş liste ihtimalini `Optional`
üzerinden güvenli ele al.

### Business açısından burada ne yapılıyor?

Katalog servisi iki ayrı özet kartı üretir:

```text
En ekonomik ürün
En pahalı ürün
```

İki kartta fiyatın ne anlama geldiği farklı kurallarla tanımlanmamalıdır. Tek
Comparator tekrar kullanılarak karşılaştırma davranışının tutarlı kalması
sağlanır.

## 7. Eksik müşteri soyadlarını listenin sonuna koyma

Şu modeli oluştur:

```java
Customer(String firstName, String lastName)
```

Bazı müşterilerin `lastName` değeri `null` olsun. Müşterileri soyadına göre
alfabetik sırala; soyadı eksik olanları listenin sonuna yerleştir.
`Comparator.nullsLast()` kullan.

Soyadı aynıysa isim ikinci karşılaştırma kuralı olsun.

### Business açısından burada ne yapılıyor?

CRM sisteminde eski veya dış kaynaktan gelen bazı müşteri kayıtlarında soyadı
eksik olabilir. Rapor bu nedenle çökmemeli ve eksik kayıtları kaybetmemelidir.
Tam veriler alfabetik gösterilir, veri tamamlama gerektiren kayıtlar listenin
sonunda görünür.

## 8. Klasik ayrı Comparator sınıfı oluşturma

Şu POJO'yu oluştur:

```java
Invoice(long number, int amount)
```

Invoice herhangi bir karşılaştırma interface'i implement etmesin. Ayrı bir:

```java
InvoiceAmountComparator implements Comparator<Invoice>
```

sınıfı yaz ve faturaları tutara göre küçükten büyüğe sırala. Aynı davranışın
`Comparator.comparingInt(Invoice::getAmount)` karşılığını da yazıp sonuçları
karşılaştır.

### Business açısından burada ne yapılıyor?

Eski bir kurumsal projede karşılaştırma davranışları isimlendirilmiş ayrı
sınıflarda tutulabilir. Yeni kodda aynı davranış factory metotla daha kısa
kurulabilir. Geliştirici iki kod stilini de okuyabilmeli ve bunların aynı
business kuralını taşıdığını anlayabilmelidir.

## 9. Sipariş önceliği için özel Comparator yazma

Şu modeli oluştur:

```java
Order(long id, String priority, LocalDateTime createdAt)
```

Öncelik değerleri:

```text
HIGH, NORMAL, LOW
```

Siparişleri önce `HIGH`, sonra `NORMAL`, sonra `LOW` gelecek şekilde sırala.
Aynı öncelikteki siparişlerde eski kayıt önce gelsin. Öncelik metinlerinin
alfabetik sırasını doğrudan kullanma; business sırasını açıkça tanımla.

### Business açısından burada ne yapılıyor?

Depo toplama kuyruğunda acil siparişler normal siparişlerden önce hazırlanır.
Alfabetik sıralama `HIGH, LOW, NORMAL` gibi yanlış bir operasyon sırası
üretebilir. Comparator teknik karakter sırasını değil işletmenin öncelik
politikasını temsil etmelidir.

## 10. Genel sıralama metodu geliştirme

Aşağıdaki generic metodu oluştur:

```java
<T> List<T> sort(
    List<T> values,
    Comparator<? super T> comparator
)
```

Metot kaynak listeyi değiştirmeden yeni sıralı liste döndürsün. Aynı metodu:

- Ürünleri fiyata göre,
- Çalışanları isme göre,
- Siparişleri tarihe göre

sıralamak için kullan.

### Business açısından burada ne yapılıyor?

Farklı raporlar aynı “değerleri verilen kurala göre sırala” algoritmasını
kullanır. Generic metot Product, Employee veya Order ayrıntısını bilmez. Değişen
business davranışı `Comparator` parametresi olarak metoda gönderilir.

Tip akışı:

```text
List<T> + Comparator<? super T>
              ↓
         List<T>
```
