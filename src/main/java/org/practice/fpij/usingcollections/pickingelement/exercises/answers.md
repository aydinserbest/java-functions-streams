# Alıştırma Cevapları: Tek Bir Elemanı Güvenle Seçmek

Karar şeması — "sonuç bulunamazsa ne olmalı?" sorusunun cevabına göre:

```text
Varsayılan bir değerle devam edilsin           → orElse(varsayılan)
Sadece VARSA bir işlem yapılsın, yoksa hiçbir şey → ifPresent(...)
Bulunamaması programlama hatası/veri tutarsızlığı → orElseThrow()
Var/yok bilgisiyle if/else yazılacak            → isPresent() + get() (ya da ifPresentOrElse)
```

Hepsinin ortak temeli: `koleksiyon.stream().filter(şart).findFirst()` —
sonuç bir `Optional<T>` olur, `null` DEĞİL.

---

### 1. E-postaya Göre Müşteri Bulma

```java
Optional<Customer> found = customers.stream()
        .filter(c -> c.email().equals(email))
        .findFirst();

System.out.println(found.map(Customer::toString).orElse("Böyle bir müşteri bulunamadı"));
```

**Neden:** Varsayılan bir METİN gösterilecek — `orElse(...)` bu ihtiyacın
doğrudan karşılığı: değer varsa onu, yoksa verilen varsayılanı kullanır.

---

### 2. Barkoda Göre Ürün Bulma

```java
String priceText = products.stream()
        .filter(p -> p.barcode().equals(scannedBarcode))
        .findFirst()
        .map(p -> String.valueOf(p.price()))
        .orElse("Ürün tanınmadı");
```

**Neden:** Yine "yoksa özel bir metinle devam et" senaryosu — `orElse`.
`map(...)` ile önce üründen fiyata dönüşüp SONRA `orElse` ile varsayılan
vermek, `Optional` boşken `get()` çağırma riskini tamamen ortadan kaldırır.

---

### 3. İptal Edilen İlk Siparişi Bildirme

```java
orders.stream()
        .filter(o -> o.status() == OrderStatus.CANCELLED)
        .findFirst()
        .ifPresent(order -> sendRefundNotification(order));
```

**Neden:** "VARSA bir işlem yap, YOKSA hiçbir şey yapma" cümlesi doğrudan
`ifPresent(Consumer<T>)`'in tanımı. `if (result.isPresent()) { ... }`
yazmaya gerek yok; `Optional` boşsa lambda hiç çalışmaz.

---

### 4. Öğrenci Numarasına Göre Kayıt Bulma

```java
Student student = students.stream()
        .filter(s -> s.number().equals(studentNumber))
        .findFirst()
        .orElseThrow();
```

**Neden:** "Öğrenci MUTLAKA sistemde kayıtlı olmalı, bulunamazsa bu ciddi
bir tutarsızlık" cümlesi, bunun normal bir "sonuç yok" durumu değil, bir
HATA durumu olduğunu söylüyor. `orElse` ile sessizce bir varsayılan
vermek yanlış olur (hatayı gizler); `orElseThrow()` sorunu açıkça
(`NoSuchElementException` ile) yüzeye çıkarır.

---

### 5. Alerjen İçermeyen Yemek Önerisi

```java
Optional<Dish> suggestion = menu.stream()
        .filter(dish -> !dish.allergens().contains(allergen))
        .findFirst();

System.out.println(suggestion.map(Dish::name).orElse("Size uygun bir yemek bulunamadı"));
```

**Neden:** Yine "yoksa varsayılan metin" — `orElse`. Şart burada "içermeme"
(`!contains(...)`), ama `findFirst` kullanımı aynı kalıp.

---

### 6. Yeterli Stoktaki İlk Ürünle Siparişi Karşılama

```java
shelves.stream()
        .filter(shelf -> shelf.quantity() > minimumRequired)
        .findFirst()
        .ifPresentOrElse(
                shelf -> fulfillOrderFrom(shelf),
                () -> notifyWarehouseManager()
        );
```

**Neden:** Burada hem "varsa yap" hem "yoksa BAŞKA bir şey yap" ihtiyacı
var — `ifPresent` tek başına yetmez (o sadece "varsa" kolunu karşılar).
`ifPresentOrElse(Consumer, Runnable)` iki durumu da tek çağrıda ifade eder.

---

### 7. Yetkinliğe Göre Çalışan Bulma

```java
Optional<Employee> candidate = employees.stream()
        .filter(e -> e.skills().contains("İngilizce") && e.isAvailable())
        .findFirst();

candidate.ifPresentOrElse(
        e -> assignToProject(e, project),
        () -> System.out.println("Uygun çalışan bulunamadı, dışarıdan destek gerekebilir")
);
```

**Neden:** Soru 6 ile aynı kalıp — iki farklı davranış (ata / uyar)
gerektiği için `ifPresentOrElse`.

---

### 8. Acil Destek Talebini Öne Çıkarma

```java
tickets.stream()
        .filter(t -> t.priority() == Priority.URGENT)
        .findFirst()
        .ifPresent(t -> showHighlighted(t));
```

**Neden:** "Varsa göster, yoksa alan hiç gösterilmesin" — tek kollu bir
davranış, `ifPresent` yeterli (Soru 3 ile aynı mantık).

---

### 9. Kayıtlı Kullanıcı Yoksa Misafir Olarak Devam Etme

```java
User currentUser = registeredUsers.stream()
        .filter(u -> u.email().equals(loginEmail))
        .findFirst()
        .orElse(GUEST_USER);
```

**Neden:** "Hiçbir zaman boş/tanımsız bir kullanıcıyla devam edilmemeli"
cümlesi, önceden tanımlı somut bir NESNENİN (varsayılan değerin) her
zaman hazır olması gerektiğini söylüyor — tam olarak `orElse(sabitDeğer)`
senaryosu. `orElseThrow` burada yanlış olurdu (kayıtsız kullanıcı bir hata
değil, misafir akışına yönlendirilecek normal bir durum).

---

### 10. Uçuş Numarasına Göre Kalkış Saati

```java
String departureInfo = flights.stream()
        .filter(f -> f.flightNumber().equals(inputNumber))
        .findFirst()
        .map(f -> f.departureTime().toString())
        .orElse("Bu numarayla bir uçuş bulunamadı");
```

**Neden:** Kullanıcıya gösterilecek bir metin bekleniyor, sonuç yoksa da
bilgilendirici bir metin gösterilecek — klasik `map(...).orElse(...)`
kombinasyonu.

---

## Genel özet

```text
findFirst()                → koşula uyan İLK elemanı bulur, Optional<T> döner (null değil)

orElse(varsayılan)         → yoksa sabit bir varsayılan değer kullan
map(...).orElse(varsayılan)→ önce dönüştür, sonra yoksa varsayılan kullan
ifPresent(consumer)        → sadece VARSA bir işlem yap, yoksa hiçbir şey yapma
ifPresentOrElse(c, runnable) → VARSA bir şey, YOKSA başka bir şey yap
orElseThrow()               → yokluk normal değil, bir hata durumu; açıkça patlat
isPresent() + get()         → mümkünse yukarıdakilerden biri tercih edilir
```
