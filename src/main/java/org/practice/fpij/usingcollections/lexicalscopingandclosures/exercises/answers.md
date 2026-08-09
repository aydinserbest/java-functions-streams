# Alıştırma Cevapları: Lexical Scoping ve Closure'lar

Ortak kalıp: "değeri hatırlayan bir davranış üretmek" istediğinizde, bir
parametre alıp bir `Predicate`/`Function` DÖNDÜREN bir yapı yazarsınız
(static metot, ya da `Function<Girdi, Predicate<Eleman>>` gibi bir
değişken). Döndürülen lambda, dıştaki parametreyi (harf, eşik, oran...)
**kapatır (closure)** — o parametre artık lambda'nın kendi hafızasında
taşınır.

```text
static Predicate<T> factory(X parametre) {
    return eleman -> ... parametre ...;
}

// veya

Function<X, Predicate<T>> factory = parametre -> eleman -> ... parametre ...;
```

---

### 1. Şehre Göre Müşteri Kontrolü

```java
static Predicate<Customer> isFromCity(String city) {
    return customer -> customer.city().equals(city);
}

long istanbulCount = customers.stream().filter(isFromCity("İstanbul")).count();
long ankaraCount = customers.stream().filter(isFromCity("Ankara")).count();
```

**Neden:** Üç şehir için de kontrol mantığı (`equals` karşılaştırması)
BİREBİR aynı; değişen tek şey karşılaştırılan şehir adı. `isFromCity`
metodu bu değeri parametre olarak alıp bir `Predicate` üretir; döndürülen
lambda içindeki `city`, `StartsWithHigherOrderFunction`'daki `letter` ile
aynı rolü oynar — dıştaki kapsamdan gelen, lambda tarafından hatırlanan
bir değer.

---

### 2. Sipariş Tutarı Eşiği

```java
static Predicate<Order> isBigOrder(double threshold) {
    return order -> order.amount() > threshold;
}

orders.stream().filter(isBigOrder(500)).forEach(...);
```

**Neden:** Eşik kampanyaya göre değişiyor ama "tutar eşikten büyük mü"
sorusu hep aynı. Eşiği parametre alıp `Predicate` döndüren bir yapı,
mantığı tek yerde tutar.

---

### 3. KDV Dahil Fiyat Hesaplama

```java
Function<Double, Function<Double, Double>> withVat =
        rate -> price -> price * (1 + rate);

Function<Double, Double> vat20 = withVat.apply(0.20);
double total = vat20.apply(100.0); // 120.0
```

**Neden:** Burada iki aşamalı bir ihtiyaç var: önce oranı ver, SONRA
istediğin kadar fiyat besle. Bu, `CurriedFunctionRefactor`'daki
`startsWithLetterConcise` ile birebir aynı kalıp — `letter -> name -> ...`
yerine `rate -> price -> ...`. `withVat.apply(0.20)` bize oranı zaten
"hatırlayan" bir `Function<Double, Double>` verir; onu istediğimiz kadar
farklı fiyatla çağırabiliriz.

---

### 4. Müşteri Tipine Göre İndirim

```java
Function<Double, Function<Double, Double>> applyDiscount =
        percentage -> price -> price * (1 - percentage);

Function<Double, Double> studentDiscount = applyDiscount.apply(0.10);
double discountedPrice = studentDiscount.apply(200.0); // 180.0
```

**Neden:** Soru 3 ile aynı curry kalıbı: yüzde önce sabitlenir, sonra
istenen fiyatlara uygulanır. Üç ayrı `Function` (öğrenci, çalışan, VIP)
aynı `applyDiscount` fabrikasından üretilir, mantık tek yerde kalır.

---

### 5. Kategoriye Göre Ürün Filtreleme

```java
static Predicate<Product> hasCategory(String category) {
    return product -> product.category().equals(category);
}

products.stream().filter(hasCategory("Elektronik")).forEach(...);
```

**Neden:** Soru 1 ile aynı yapı, farklı alan (`category` yerine `city`).
Kategori adı dışarıdan geliyor, kontrol mantığı sabit.

---

### 6. Geçme Notuna Göre Öğrenci Ayırma

```java
static Predicate<Student> hasPassed(int passingGrade) {
    return student -> student.grade() >= passingGrade;
}

List<Student> passed = students.stream().filter(hasPassed(60)).toList();
```

**Neden:** Geçme notu sınava göre değişiyor; `passingGrade`'i parametre
alıp `Predicate` üreten bir yapı, her sınav için ayrı bir kontrol yazmayı
gereksiz kılar.

---

### 7. Kurumsal E-posta Domainine Göre Kullanıcı Ayırma

```java
static Predicate<User> hasEmailDomain(String domain) {
    return user -> user.email().endsWith(domain);
}

users.stream().filter(hasEmailDomain("@firma1.com")).forEach(...);
```

**Neden:** Domain dışarıdan parametre; kontrol mantığı (`endsWith`) sabit.
Aynı `isFromCity`/`hasCategory` kalıbının bir başka uygulaması.

---

### 8. Para Birimine Göre Tutar Gösterimi

```java
Function<String, Function<Double, String>> formatWithSymbol =
        symbol -> amount -> symbol + amount;

Function<Double, String> tlFormatter = formatWithSymbol.apply("₺");
System.out.println(tlFormatter.apply(150.0)); // ₺150.0
```

**Neden:** Bu bir `Predicate` değil, bir DÖNÜŞÜM (`Function`) — girdi
(tutar) alıp yeni bir `String` üretiyor. Yine de aynı closure fikri
geçerli: sembol önce sabitlenir, sonra istenen tutarlara uygulanır.

---

### 9. Yöneticiye Göre Ekip Filtreleme

```java
static Predicate<Employee> reportsTo(String managerName) {
    return employee -> employee.managerName().equals(managerName);
}

employees.stream().filter(reportsTo("Ahmet Yılmaz")).forEach(...);
```

**Neden:** Yönetici adı sorgudan sorguya değişir, karşılaştırma mantığı
sabittir — yine parametreyi kapatan bir `Predicate` fabrikası.

---

### 10. Kayıt Yılına Göre Kullanıcı Filtreleme

```java
static Predicate<User> registeredAfter(int year) {
    return user -> user.registrationDate().getYear() > year;
}

List<User> recentUsers = users.stream().filter(registeredAfter(2023)).toList();
```

**Neden:** Yıl her sorguda değişebiliyor; `registeredAfter(2023)` ve
`registeredAfter(2024)` aynı kontrol MANTIĞINI paylaşır, sadece kapattığı
değer farklıdır. Bu, sorunun kendisinin de vurguladığı "aynı kontrolü her
yıl için elle tekrar yazmak istemiyorum" ihtiyacına birebir cevap verir.

---

## Genel özet

```text
"Aynı kontrol, farklı bir sabit değerle tekrar tekrar lazım" sinyalini görünce:

static Predicate<T> factory(X sabitDeğer) {
    return eleman -> ... sabitDeğer ...;
}

ya da curry ile:

Function<X, Predicate<T>> factory = sabitDeğer -> eleman -> ...;
Function<X, Function<Y, R>> factory = birinciDeğer -> ikinciDeğer -> ...;
```

`factory(deger)` çağrısı HEMEN çalışmaz; size `deger`'i hatırlayan (closure
kuran), daha sonra `filter`/`apply` ile kullanılacak bir davranış döndürür.
