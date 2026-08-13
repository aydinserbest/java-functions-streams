# `Arrays.asList()` ile `List.of()` Farkı — `sort()` Üzerinden

Java'daki `Arrays.asList()` ile `List.of()` arasındaki önemli bir fark,
`sort()` üzerinden çok güzel ortaya çıkıyor.

Şunu kullanmıştın:

```java
List<Person> people = Arrays.asList(
        new Person("John", 30),
        new Person("Sara", 21),
        new Person("Jane", 41),
        new Person("Greg", 35)
);

people.sort(null); // ✅ çalışıyor
```

Buradaki kilit nokta şu:

> `Arrays.asList()` ile oluşan listeye **yeni eleman ekleyemezsin veya
> eleman silemezsin**, ama mevcut elemanların yerlerini/değerlerini
> değiştirebilirsin.

---

## 1. `Arrays.asList()` → Fixed-Size

```java
List<String> names = Arrays.asList("John", "Sara", "Jane");
```

Şunlar olmaz:

```java
names.add("Greg");    // ❌ UnsupportedOperationException
names.remove("John"); // ❌ UnsupportedOperationException
```

Çünkü listenin boyutu sabit:

```text
[John, Sara, Jane]   3 eleman

add    ❌
remove ❌
```

Ama şu olur:

```java
names.set(0, "Greg"); // ✅
```

Sonuç: `[Greg, Sara, Jane]` — çünkü eleman sayısını değiştirmedik, sadece
`John → Greg` yaptık.

---

## 2. Bunun `sort()` ile Ne İlgisi Var?

`sort()` sıralama yaparken listeye eleman ekleyip silmek **zorunda
değil** — kabaca mevcut elemanların yerlerini değiştiriyor.

```text
Başlangıç:              Sıralama sonrası:
John 30                 Sara 21
Sara 21        ──►      John 30
Jane 41                 Greg 35
Greg 35                 Jane 41

Eleman sayısı: 4 → 4
```

```text
add()    gerekmedi
remove() gerekmedi

mevcut pozisyonlardaki elemanları değiştirmek → yeterli
```

`Arrays.asList()` bunu desteklediğinden `people.sort(null)` **çalışır**. ✅

---

## 3. Peki `List.of()` Neden Farklı?

Şunu yapalım:

```java
List<Person> people = List.of(
        new Person("John", 30),
        new Person("Sara", 21),
        new Person("Jane", 41),
        new Person("Greg", 35)
);

people.sort(null);
```

Bu sefer runtime'da:

```text
UnsupportedOperationException
```

alırsın. Çünkü `List.of()` ile oluşturulan liste **unmodifiable**dır:

```java
people.add(...);     // ❌
people.remove(...);  // ❌
people.set(...);     // ❌
people.sort(...);    // ❌
```

`sort()` listeyi değiştirmek istediği için çalışamaz.

---

## 4. En Önemli Fark Burada

```text
Arrays.asList(...)                    List.of(...)
─────────────────────                 ─────────────────────
Boyutu değiştir:                      add()       ❌
  add()       ❌                      remove()    ❌
  remove()    ❌                      set()       ❌
Mevcut elemanları değiştir:           sort()      ❌
  set()       ✅
  sort()      ✅
```

**`Arrays.asList()` = fixed-size** — **`List.of()` = unmodifiable**.
Bunlar aynı şey değil.

---

## 5. Basit Deney

Bunu çalıştır:

```java
List<String> names = Arrays.asList("John", "Sara", "Jane");
names.set(0, "Greg");
System.out.println(names);
```

Çalışır → `[Greg, Sara, Jane]`

Şimdi sadece ilk satırı değiştir:

```java
List<String> names = List.of("John", "Sara", "Jane");
names.set(0, "Greg");
System.out.println(names);
```

Bu kez → `UnsupportedOperationException`

Böylece farkı doğrudan görmüş olursun.

---

## 6. `List.of()` Kullanıp Yine de `sort()` Yapmak İstersem?

Çok sık kullanılan çözüm — `ArrayList`'e sarmak:

```java
List<Person> people = new ArrayList<>(List.of(
        new Person("John", 30),
        new Person("Sara", 21),
        new Person("Jane", 41),
        new Person("Greg", 35)
));
```

Artık gerçek bir değiştirilebilir `ArrayList` oluştu:

```java
people.sort(null); // ✅
people.add(...);    // ✅
people.remove(...); // ✅
people.set(...);    // ✅
people.sort(...);   // ✅
```

hepsi mümkün.

---

## Kafanda Şu Üçlüyü Ayır

| Oluşturma | `add`/`remove` | `set` | `sort` |
|---|:---:|:---:|:---:|
| `Arrays.asList(...)` | ❌ | ✅ | ✅ |
| `List.of(...)` | ❌ | ❌ | ❌ |
| `new ArrayList<>(...)` | ✅ | ✅ | ✅ |

Comparable örneğimizde `Arrays.asList(...)` kullanıp `people.sort(null)`
yapabilmemizin sebebi tam olarak bu: **`sort()` listenin boyutunu
değiştirmiyor ama mevcut elemanların konumlarını değiştirebiliyor;
`Arrays.asList()` da bu tür değişikliğe izin veriyor.**
