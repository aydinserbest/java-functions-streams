# `List.of()` ile `Arrays.asList()` Farkı

İkisi de bir liste **oluşturur**, ama davranışları farklı.

## `List.of(...)` — Tamamen değiştirilemez (immutable)

```java
List<String> names = List.of("Alice", "Bob", "Charlie");

names.set(0, "Ayşe"); // UnsupportedOperationException
names.add("David");   // UnsupportedOperationException
```

`null` eleman da kabul etmez: `List.of("Alice", null)` → `NullPointerException`.

## `Arrays.asList(...)` — Sabit boyutlu, elemanları değiştirilebilir

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

names.set(0, "Ayşe"); // OK  → [Ayşe, Bob, Charlie]
names.add("David");   // UnsupportedOperationException (boyut sabit)
```

Bir array'i doğrudan liste olarak görmek için de kullanılır — ve bu
durumda **array ile liste birbirine bağlıdır** (biri değişince öteki de
değişir):

```java
String[] arr = {"Alice", "Bob"};
List<String> names = Arrays.asList(arr);

arr[0] = "Ayşe";
System.out.println(names); // [Ayşe, Bob]
```

## Pratikte fark nerede çıkıyor: `list.sort(...)`

`List.sort(Comparator)`, listeyi **yerinde (in place)** sıralar — arkada
her elemanı `set(...)` ile yeniden yerleştirir. `Arrays.asList()`
elemanların `set()` ile değiştirilmesine izin verdiği için burada
sorunsuz çalışır:

```java
List<Integer> numbers = Arrays.asList(40, 5, 20, 10);
numbers.sort(null); // OK → [5, 10, 20, 40]
```

Aynı çağrıyı `List.of(...)` üzerinde denersen `set()` desteklenmediği
için patlar:

```java
List<Integer> numbers = List.of(40, 5, 20, 10);
numbers.sort(null); // UnsupportedOperationException
```

## Özet

| | `List.of(...)` | `Arrays.asList(...)` |
|---|---|---|
| Eleman değiştirme (`set`) | ❌ | ✅ |
| Eleman ekleme/silme | ❌ | ❌ |
| `null` eleman | ❌ | ✅ |
| Array'e bağlı mı | Hayır (kopya) | Evet |

**Kısa kural:** sabit, hiç değişmeyecek bir örnek veri lazımsa
`List.of(...)`; elindeki bir array'i listeye çevirip elemanlarını
güncelleyebilmen gerekiyorsa `Arrays.asList(...)`.

## Projede geçtiği yer

`src/main/java/org/practice/fpij/usingcollections/consumerforeach/Iteration.java:15-20`
