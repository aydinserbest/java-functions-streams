# Alıştırma Cevapları: Lambda İfadelerini Yeniden Kullanma

Bu paketin tamamı tek bir fikre dayanıyor: kontrol mantığı SABİTSE (hiçbir
dışarıdan parametreye ihtiyaç duymuyorsa), onu bir kere bir
`Predicate<T>` değişkeninde tanımlayıp, kaç listede kullanılırsa
kullanılsın AYNI değişkeni `filter(...)`'a vermek yeterlidir.

```java
final Predicate<T> kontrol = eleman -> ... ;

liste1.stream().filter(kontrol)...
liste2.stream().filter(kontrol)...
liste3.stream().filter(kontrol)...
```

(Not: eğer kontrolün kendisi de dışarıdan bir DEĞERE göre değişiyorsa —
örn. "hangi şehir" gibi — bu artık `reusinglambdas` değil,
`lexicalscopingandclosures` paketindeki konudur.)

---

### 1. Aktif Müşteri Sayımı

```java
final Predicate<Customer> isActive = customer -> customer.isActive();

long vipActive = vipCustomers.stream().filter(isActive).count();
long standardActive = standardCustomers.stream().filter(isActive).count();
long corporateActive = corporateCustomers.stream().filter(isActive).count();
```

**Neden:** `isActive` üç listede de birebir aynı kural; tek bir
`Predicate<Customer>` değişkeni üç `filter()` çağrısında paylaşılır.

---

### 2. Stokta Olan Ürünleri Filtreleme

```java
final Predicate<Product> inStock = product -> product.quantity() > 0;

List<Product> electronicsInStock = electronics.stream().filter(inStock).toList();
List<Product> clothingInStock = clothing.stream().filter(inStock).toList();
List<Product> booksInStock = books.stream().filter(inStock).toList();
```

**Neden:** Soru 1 ile aynı kalıp; kontrol tek yerde, üç kategoride
paylaşılıyor.

---

### 3. Kıdemli Çalışanları Bulma

```java
final Predicate<Employee> isSenior = e -> e.yearsOfService() > 5;

List<Employee> istanbulSeniors = istanbulOffice.stream().filter(isSenior).toList();
List<Employee> ankaraSeniors = ankaraOffice.stream().filter(isSenior).toList();
List<Employee> izmirSeniors = izmirOffice.stream().filter(isSenior).toList();
```

---

### 4. Sınıfı Geçen Öğrencileri Sayma

```java
final Predicate<Student> hasPassed = s -> s.grade() >= 60;

long classAPassed = classA.stream().filter(hasPassed).count();
long classBPassed = classB.stream().filter(hasPassed).count();
long classCPassed = classC.stream().filter(hasPassed).count();
```

---

### 5. Süresi Geçmiş Ürünleri Bulma

```java
final Predicate<Product> isExpired = p -> p.expiryDate().isBefore(LocalDate.now());

List<Product> expiredInMain = mainWarehouse.stream().filter(isExpired).toList();
List<Product> expiredInBranch = branchWarehouse.stream().filter(isExpired).toList();
```

**Neden:** `LocalDate.now()` her çağrıda "bugün" değerini ürettiği için
kontrolün kendisi hâlâ SABİT bir kural (dışarıdan parametre almıyor);
sadece iki depoda paylaşılıyor.

---

### 6. Uzaktan Çalışanları Filtreleme

```java
final Predicate<Employee> isRemote = e -> e.workMode() == WorkMode.REMOTE;

List<Employee> remoteFrontend = frontendTeam.stream().filter(isRemote).toList();
List<Employee> remoteBackend = backendTeam.stream().filter(isRemote).toList();
List<Employee> remoteQa = qaTeam.stream().filter(isRemote).toList();
```

---

### 7. İade Edilen Siparişleri Sayma

```java
final Predicate<Order> isReturned = order -> order.status() == OrderStatus.RETURNED;

long thisWeekReturns = thisWeekOrders.stream().filter(isReturned).count();
long lastWeekReturns = lastWeekOrders.stream().filter(isReturned).count();
```

---

### 8. Çözülmemiş Şikayetleri Bulma

```java
final Predicate<Complaint> isUnresolved = c -> !c.isResolved();

List<Complaint> branch1Open = branch1Complaints.stream().filter(isUnresolved).toList();
List<Complaint> branch2Open = branch2Complaints.stream().filter(isUnresolved).toList();
```

---

### 9. Dolu Seferleri Bulma

```java
final Predicate<Trip> isFull = trip -> trip.passengerCount() >= trip.capacity();

List<Trip> fullMorningTrips = morningTrips.stream().filter(isFull).toList();
List<Trip> fullEveningTrips = eveningTrips.stream().filter(isFull).toList();
```

---

### 10. Değerlendirmesi Tamamlanmamış Çalışanları Sayma

```java
final Predicate<Employee> reviewPending = e -> !e.isReviewCompleted();

long salesPending = salesTeam.stream().filter(reviewPending).count();
long marketingPending = marketingTeam.stream().filter(reviewPending).count();
```

---

## Genel özet

```text
Kontrol SABİT (parametre almıyor), birden fazla listede kullanılacak
        ↓
final Predicate<T> kontrol = eleman -> ...;
        ↓
liste1.stream().filter(kontrol)
liste2.stream().filter(kontrol)
liste3.stream().filter(kontrol)
```

Bu sayede kural DEĞİŞİRSE (örn. "5 yıl" yerine "7 yıl" olursa), tek bir
satırı güncellemek yeterli olur — tüm `filter()` çağrıları otomatik olarak
güncel kuralı kullanır.
