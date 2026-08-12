# Array'den Stream Oluşturma: `.stream()` Neden Çalışmıyor?

`List`, `Set` gibi `Collection` tipleri kendi üzerlerinde doğrudan
`.stream()` metoduna sahiptir (bu metot `Collection` arayüzünden gelir).
**Array bir `Collection` değildir** — Java'nın yerleşik, ilkel bir veri
yapısıdır. Bu yüzden bir array üzerinde `.stream()` çağırmak **derleme
hatası** verir.

```java
String[] words = {"apple", "banana"};

words.stream(); // Derleme hatası: String[] içinde stream() metodu yoktur
```

## Çözüm: `Arrays.stream(...)`

Bir array'den Stream oluşturmak için `java.util.Arrays` sınıfındaki
static `stream(...)` metodunu kullanırız:

```java
String[] words = {"apple", "banana"};

Stream<String> streamOfWords = Arrays.stream(words);
```

### İlkel (primitive) array'lerde tip farkı

Array **ilkel tip** (`int[]`, `double[]`, ...) ise `Arrays.stream(...)`
sıradan bir `Stream<T>` değil, o ilkel tipe özel bir stream döner —
`int[]` için `IntStream`:

```java
int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

Arrays.stream(numbers)   // IntStream döner
        .skip(5)
        .forEach(System.out::println);
```

## Alternatif: `Stream.of(...)`

Nesne (object) array'leri için `Stream.of(...)` de aynı sonucu verir —
ikisi de aynı işi yapar, `Arrays.stream()` daha ilkel-tip-dostu ve
büyük array'lerde biraz daha performanslıdır, `Stream.of()` ise varargs
üzerinden değer geçmekte de kullanılabilir (`Stream.of(1, 2, 3)` gibi
doğrudan değerlerle):

```java
String[] words = {"hello", "world"};

Stream<String> viaStreamOf   = Stream.of(words);
Stream<String> viaArraysStream = Arrays.stream(words);
```

## Bonus: `Map` de bir `Collection` değildir

Aynı mantık `Map` için de geçerli — `Map.stream()` diye bir metot
YOKTUR, çünkü `Map` de `Collection` arayüzünü uygulamaz. Hangi kısmı
Stream'e çevirmek istediğinizi açıkça belirtmeniz gerekir:

```java
map.keySet().stream()    // sadece anahtarlar
map.values().stream()    // sadece değerler
map.entrySet().stream()  // anahtar-değer çiftleri
```

## Özet

| Veri yapısı | Doğrudan `.stream()` | Doğru kullanım |
|---|---|---|
| `List`, `Set` (Collection) | ✅ Var | `list.stream()` |
| `int[]`, `String[]` (array) | ❌ Yok | `Arrays.stream(array)` veya `Stream.of(array)` |
| `Map` | ❌ Yok | `map.keySet()/.values()/.entrySet().stream()` |

## Projede geçtiği yerler

- `src/main/java/org/practice/fpij/usingcollections/streamcontinuebreak/skippingvalues/SkipElements.java`
- `src/main/java/org/practice/fpij/usingcollections/streamcontinuebreak/skippingvalues/SkipValues.java`
- `src/main/java/org/practice/streamsandfunctionalinterfaces/streams/basics/StreamCreationDemo.java`
- `src/main/java/org/practice/streamsandfunctionalinterfaces/streams/flatMap/FlatMapDemo.java`
