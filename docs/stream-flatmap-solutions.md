# Stream `flatMap()` Alıştırmaları — Örnek Çözümler

`flatMap()` intermediate operation'dır; aldığı fonksiyon genellikle
`T -> Stream<R>` tip akışına sahiptir. Akışı çalıştırmak için sonda `toList()`,
`collect()` veya başka bir terminal operation bulunur.

## 1. Telefon listesi

```java
List<String> phones = customers.stream()
        .flatMap(customer -> customer.phoneNumbers().stream())
        .toList();
```

`map()` kullanılsaydı `Stream<List<String>>`, burada ise `Stream<String>` oluşur.

## 2. Sipariş satırları

```java
List<OrderLine> allLines = orders.stream()
        .flatMap(order -> order.lines().stream())
        .toList();
```

Her sipariş bir satır Stream'i üretir; `flatMap()` bunları tek kuyruğa bağlar.

## 3. Çalışan e-postaları

```java
List<String> emails = departments.stream()
        .flatMap(department -> department.employees().stream())
        .map(Employee::email)
        .toList();
```

Önce çalışanlar düzleştirilir, sonra her çalışan e-postaya dönüştürülür.

## 4. Kelime indeksi

```java
Set<String> words = sentences.stream()
        .flatMap(sentence -> Arrays.stream(sentence.split("\\s+")))
        .map(word -> word.toLowerCase(Locale.ROOT))
        .collect(Collectors.toSet());
```

`Arrays.stream()` her kelime dizisini Stream'e çevirir.

## 5. Ders raporları

```java
List<String> registrations = students.stream()
        .flatMap(student -> student.courses().stream())
        .toList();

List<String> differentCourses = students.stream()
        .flatMap(student -> student.courses().stream())
        .distinct()
        .toList();
```

İlk listenin boyu kayıt sayısı, ikincinin boyu farklı ders sayısıdır.

## 6. Açık görev panosu

```java
List<Task> openTasks = projects.stream()
        .flatMap(project -> project.tasks().stream())
        .filter(task -> !task.completed())
        .toList();
```

`flatMap()` bütün görevleri getirir; `filter()` açık olanları seçer.

## 7. Benzersiz stok kodları

```java
List<String> skus = stores.stream()
        .flatMap(store -> store.productSkus().stream())
        .distinct()
        .toList();
```

Aynı SKU farklı mağazalarda olsa da merkez listede bir kez bulunur.

## 8. Efektif izinler

```java
Set<String> permissions = user.roles().stream()
        .flatMap(role -> role.permissions().stream())
        .collect(Collectors.toSet());
```

`Set`, roller arasında ortak olan izinleri doğal olarak tekilleştirir.

## 9. Sayfalı API sonucu

```java
List<Product> products = pages.stream()
        .flatMap(page -> page.content().stream())
        .toList();
```

Boş `content` boş Stream üretir ve toplam sonucu bozmaz.

## 10. İki seviye kategori

```java
List<Category> grandchildren = roots.stream()
        .flatMap(root -> root.children().stream())
        .flatMap(child -> child.children().stream())
        .toList();
```

İlk `flatMap()` çocuklara, ikincisi torun kategorilere ulaşır.

