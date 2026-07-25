# Functional Interface Kullanımları — Toplu Hap Özet

Bu doküman, projede şimdiye kadar kullanılan functional interface'leri aynı
kalıpla yan yana gösterir.

Her örnekte şu dört sorunun cevabı bulunur:

```text
1. Abstract metodun imzası nedir?
2. Kaç değer alır, hangi tipi döndürür?
3. Örnek lambda uygulaması nedir?
4. Davranış hangi metot çağrısıyla çalıştırılır?
```

## Temel çalışma mantığı

```text
Functional interface tipindeki referans
        =
Tek abstract metodun uygulamasını veren lambda
```

Örnek:

```java
Predicate<Integer> isEven = number -> number % 2 == 0;
```

Bu satırda sonuç henüz oluşmaz. Davranış burada çalışır:

```java
boolean result = isEven.test(4);
```

---

# 0. `BaseSyntax` içindeki temel lambda şekilleri

Kaynak: `functional/lambda/basics/BaseSyntax.java`

Bu örneklerde hedef functional interface ayrıca gösterilmemiştir; yalnızca lambda
gövdesinin alabileceği temel biçimler karşılaştırılır.

## 0.1 Parametre almayan, sonuç döndürmeyen davranış

Klasik metot yapısı:

```java
public static void printHello() {
    System.out.println("Hello");
}
```

Kısa tip akışı:

```text
() → void
```

Lambda şekli:

```java
() -> System.out.println("Hello")
```

## 0.2 İki parametre alan, sonuç döndürmeyen davranış

Klasik metot yapısı:

```java
public void add(int first, int second) {
    System.out.println(first + second);
}
```

Kısa tip akışı:

```text
(int, int) → void
```

Lambda şekli:

```java
(first, second) -> System.out.println(first + second)
```

## 0.3 İki parametre alan ve sonuç döndüren davranış

Klasik metot yapısı:

```java
public int add(int first, int second) {
    return first + second;
}
```

Kısa tip akışı:

```text
(int, int) → int
```

Lambda şekli:

```java
(first, second) -> first + second
```

Bu lambda şekillerinin kullanılabilmesi için aynı parametre/sonuç sözleşmesine
uyan bir functional interface hedef tipi gerekir.

---

# 1. Kendi yazdığımız functional interface'ler

## 1.1 `Hello` — Parametresiz, sonuç döndürmeyen davranış

Kaynak: `functional/lambda/custominterface/Hello.java`

Interface ve abstract metot:

```java
@FunctionalInterface
public interface Hello {
    void sayHello();
}
```

Tek abstract metodun yapısı:

```text
Parametre almaz → sonuç döndürmez
```

Kısa gösterim:

```text
() → void
```

Lambda uygulaması:

```java
Hello hello = () -> System.out.println("Hello, World!");
```

Davranışı çalıştıran çağrı:

```java
hello.sayHello();
```

Metoda davranış olarak gönderme:

```java
process(hello);
```

`process()` içinde çalışan çağrı:

```java
public static void process(Hello hello) {
    hello.sayHello();
}
```

Lambda doğrudan da gönderilebilir:

```java
process(() -> System.out.println("Hello, Amsterdam!"));
```

## 1.2 `ArithmeticOperation` — İki sayıdan bir sonuç üretme

Kaynak: `functional/lambda/custominterface/ArithmeticOperation.java`

Interface ve abstract metot:

```java
@FunctionalInterface
public interface ArithmeticOperation {
    int operation(int a, int b);
}
```

Tek abstract metodun yapısı:

```text
İki int alır → bir int döndürür
```

Kısa gösterim:

```text
(int, int) → int
```

Toplama lambda'sı ve çağrısı:

```java
ArithmeticOperation addition = (a, b) -> a + b;
int result = addition.operation(1, 2); // 3
```

Çıkarma lambda'sı ve çağrısı:

```java
ArithmeticOperation subtraction = (a, b) -> a - b;
int result = subtraction.operation(5, 3); // 2
```

Çarpma lambda'sı ve çağrısı:

```java
ArithmeticOperation multiplication = (a, b) -> a * b;
int result = multiplication.operation(2, 4); // 8
```

## 1.3 `ArithmeticOperations` — Aynı sözleşme, method reference örneği

Kaynak: `functional/methodreference/ArithmeticOperations.java`

Interface ve abstract metot:

```java
@FunctionalInterface
public interface ArithmeticOperations {
    int performOperation(int a, int b);
}
```

Tek abstract metodun yapısı:

```text
İki int alır → bir int döndürür
```

Kısa gösterim:

```text
(int, int) → int
```

Lambda uygulaması:

```java
ArithmeticOperations subtraction = (a, b) -> a - b;
```

Davranışı çalıştıran çağrı:

```java
int result = subtraction.performOperation(10, 5); // 5
```

Static method reference uygulaması:

```java
ArithmeticOperations addition = Integer::sum;
```

Davranışı çalıştıran çağrı:

```java
int result = addition.performOperation(10, 5); // 15
```

Eşleşme:

```text
ArithmeticOperations.performOperation(int, int) → int
Integer.sum(int, int)                           → int
```

İsimlerin aynı olması gerekmez; parametre ve sonuç sözleşmesinin uyumlu olması
gerekir.

---

# 2. `Predicate<T>` kullanımları

Hazır interface'in temel abstract metodu:

```java
boolean test(T value);
```

Genel yapı:

```text
Bir T alır → boolean döndürür
```

Kısa gösterim:

```text
T → boolean
```

Genel lambda ve çağrı kalıbı:

```java
Predicate<T> condition = value -> booleanCondition;
boolean result = condition.test(value);
```

## 2.1 `Predicate<Integer>` — Sayı çift mi?

Tek abstract metodun bu örnekteki hâli:

```text
Bir Integer alır → boolean döndürür
```

Kısa gösterim:

```text
Integer → boolean
```

Lambda uygulaması:

```java
Predicate<Integer> isEven = number -> number % 2 == 0;
```

Tek değer için çağrı:

```java
boolean result = isEven.test(4); // true
```

Liste için kullanım:

```java
List<Integer> evenNumbers = numbers.stream()
        .filter(isEven)
        .toList();
```

`filter(isEven)`, her sayı için içeride şunu çalıştırır:

```java
isEven.test(number);
```

## 2.2 `Predicate<Integer>` — Farklı sayı kuralları

Kaynak: `functional/predicate/exercises/NumberChecksDemo.java`

Pozitiflik:

```java
Predicate<Integer> isPositive = number -> number > 0;
boolean result = isPositive.test(12); // true
```

Belirli aralık:

```java
Predicate<Integer> isBetweenTenAndHundred =
        number -> number >= 10 && number <= 100;

boolean result = isBetweenTenAndHundred.test(12); // true
```

Üçe tam bölünme:

```java
Predicate<Integer> isDivisibleByThree =
        number -> number % 3 == 0;

boolean result = isDivisibleByThree.test(12); // true
```

Predicate'i genel metoda gönderme:

```java
static boolean checkNumber(
        int number,
        Predicate<Integer> condition
) {
    return condition.test(number);
}
```

Çağrı:

```java
boolean result = checkNumber(12, isEven); // true
```

## 2.3 `Predicate<String>` — Kullanıcı adı geçerli mi?

Kaynak: `functional/predicate/exercises/UsernameValidationDemo.java`

Tek abstract metodun bu örnekteki hâli:

```text
Bir String alır → boolean döndürür
```

Kısa gösterim:

```text
String → boolean
```

Lambda uygulaması:

```java
Predicate<String> isValidUsername = username ->
        username != null
        && !username.isBlank()
        && username.length() >= 5;
```

Davranışı çalıştıran çağrı:

```java
boolean result = isValidUsername.test("mehmet"); // true
```

## 2.4 `Predicate<String>` — Metin belirli harfle başlıyor mu?

Lambda uygulaması:

```java
Predicate<String> startsWithA = name -> name.startsWith("A");
```

Tek değer için çağrı:

```java
boolean result = startsWithA.test("Alice"); // true
```

Liste için çağrı:

```java
List<String> result = names.stream()
        .filter(startsWithA)
        .toList();
```

Lambda doğrudan `filter()` içine de verilebilir:

```java
List<String> result = names.stream()
        .filter(name -> name.startsWith("A"))
        .toList();
```

## 2.5 `Predicate<User>` — Kullanıcı aktif mi?

Kaynak: `functional/predicate/exercises/userfilter/UserDemo.java`

Tek abstract metodun bu örnekteki hâli:

```text
Bir User alır → boolean döndürür
```

Kısa gösterim:

```text
User → boolean
```

Lambda uygulaması:

```java
Predicate<User> isActiveUser = user -> user.isActive();
```

Tek değer için çalıştırılabilecek çağrı:

```java
boolean result = isActiveUser.test(user);
```

Projede liste üzerinde kullanım:

```java
List<User> activeUsers = users.stream()
        .filter(isActiveUser)
        .toList();
```

`filter()`, her User için içeride şunu çağırır:

```java
isActiveUser.test(user);
```

## 2.6 `Predicate<Person>` — Kişinin yaşı sınırdan büyük mü?

Kaynak: `functional/predicate/exercises/udemyperson/PersonFilterDemo.java`

Tek abstract metodun bu örnekteki hâli:

```text
Bir Person alır → boolean döndürür
```

Kısa gösterim:

```text
Person → boolean
```

Lambda uygulaması:

```java
Predicate<Person> isOlder = person -> person.getAge() > age;
```

Tek değer için düşünülen çağrı:

```java
boolean result = isOlder.test(person);
```

Projede genel filtre metoduna gönderilir:

```java
return filter(persons, isOlder);
```

Genel metot her Person için açıkça `test()` çağırır:

```java
for (Person person : persons) {
    if (isOlder.test(person)) {
        filteredPersons.add(person);
    }
}
```

## 2.7 `Predicate<Person>` — Kişi verilen şehirde mi?

Lambda uygulaması:

```java
Predicate<Person> livesInCity =
        person -> person.getCity().equals(city);
```

Davranışı tek değerde çalıştıran çağrı:

```java
boolean result = livesInCity.test(person);
```

Listeyle genel metoda gönderme:

```java
return filter(persons, livesInCity);
```

## 2.8 `Predicate<Map.Entry<String,Integer>>` — Öğrencinin yaşı büyük mü?

Kaynak: `functional/predicate/exercises/mapfilter/StudentAgeFilterDemo.java`

Tek abstract metodun bu örnekteki hâli:

```text
Bir Map.Entry<String,Integer> alır → boolean döndürür
```

Kısa gösterim:

```text
Map.Entry<String,Integer> → boolean
```

Lambda doğrudan `filter()` içine verilmiştir:

```java
.filter(entry -> entry.getValue() > 18)
```

`filter()` her entry için kavramsal olarak şunu çalıştırır:

```java
predicate.test(entry);
```

Burada:

```text
entry.getKey()   → öğrenci adı
entry.getValue() → öğrencinin yaşı
```

## 2.9 Predicate birleştirme — `and`, `or`, `negate`

Temel Predicate'ler:

```java
Predicate<Integer> isEven = number -> number % 2 == 0;
Predicate<Integer> isGreaterThan18 = number -> number > 18;
```

`and()` — iki koşul da doğru olmalı:

```java
boolean result = isEven
        .and(isGreaterThan18)
        .test(20); // true
```

`or()` — en az bir koşul doğru olmalı:

```java
boolean result = isEven
        .or(isGreaterThan18)
        .test(19); // true
```

`negate()` — sonucu tersine çevirir:

```java
Predicate<Integer> isOdd = isEven.negate();
boolean result = isOdd.test(15); // true
```

`Predicate.not()` — aynı tersine çevirmenin static biçimi:

```java
Predicate<Integer> isOdd = Predicate.not(isEven);
boolean result = isOdd.test(15); // true
```

## 2.10 `Predicate.isEqual()` — Hazır eşitlik Predicate'i

```java
Predicate<String> isJohn = Predicate.isEqual("John");
```

Tek abstract metodun yapısı değişmez:

```text
String → boolean
```

Davranışı çalıştıran çağrılar:

```java
isJohn.test("John");   // true
isJohn.test("Madame"); // false
```

---

# 3. `Function<T,R>` kullanımları

Hazır interface'in temel abstract metodu:

```java
R apply(T value);
```

Genel yapı:

```text
Bir T alır → bir R döndürür
```

Kısa gösterim:

```text
T → R
```

Genel lambda ve çağrı kalıbı:

```java
Function<T, R> mapper = value -> convertedValue;
R result = mapper.apply(value);
```

## 3.1 `Function<String,String>` — Metni büyük harfe dönüştürme

Tek abstract metodun bu örnekteki hâli:

```text
Bir String alır → bir String döndürür
```

Kısa gösterim:

```text
String → String
```

Lambda uygulaması:

```java
Function<String, String> toUppercase =
        text -> text.toUpperCase();
```

Davranışı çalıştıran çağrı:

```java
String result = toUppercase.apply("Madame"); // MADAME
```

Method reference karşılığı:

```java
Function<String, String> toUppercase = String::toUpperCase;
```

Çağrı yine aynıdır:

```java
String result = toUppercase.apply("John"); // JOHN
```

## 3.2 `Function<String,Integer>` — Metni uzunluğuna dönüştürme

Tek abstract metodun bu örnekteki hâli:

```text
Bir String alır → bir Integer döndürür
```

Kısa gösterim:

```text
String → Integer
```

Method reference uygulaması:

```java
Function<String, Integer> getLength = String::length;
```

Davranışı çalıştıran çağrı:

```java
Integer result = getLength.apply("HELLO WORLD"); // 11
```

Lambda karşılığı:

```java
Function<String, Integer> getLength = text -> text.length();
```

## 3.3 `Function<Integer,Integer>` — Sayının karesi

Tek abstract metodun bu örnekteki hâli:

```text
Bir Integer alır → bir Integer döndürür
```

Kısa gösterim:

```text
Integer → Integer
```

Lambda uygulaması:

```java
Function<Integer, Integer> square = number -> number * number;
```

Davranışı çalıştıran çağrı:

```java
Integer result = square.apply(5); // 25
```

## 3.4 `Function<Integer,Integer>` — Sayıyı iki katına çıkarma

Lambda uygulaması:

```java
Function<Integer, Integer> doubleValue = number -> number * 2;
```

Davranışı çalıştıran çağrı:

```java
Integer result = doubleValue.apply(5); // 10
```

## 3.5 Function birleştirme — `andThen()`

```java
Function<Integer, Integer> doubleValue = number -> number * 2;
Function<Integer, Integer> addValue = number -> number + number;
```

Yeni, birleşik Function:

```java
Function<Integer, Integer> combined =
        doubleValue.andThen(addValue);
```

Kısa akış:

```text
Integer → doubleValue → Integer → addValue → Integer
```

Davranışı çalıştıran çağrı:

```java
Integer result = combined.apply(5); // 20
```

Çalışma sırası:

```text
5 → 10 → 20
```

## 3.6 Function birleştirme — `compose()`

```java
Function<Integer, Integer> composed =
        doubleValue.compose(addValue);
```

`compose()` içinde parantezdeki Function önce çalışır:

```text
Integer → addValue → Integer → doubleValue → Integer
```

Davranışı çalıştıran çağrı:

```java
Integer result = composed.apply(5); // 20
```

Çalışma sırası:

```text
5 → 10 → 20
```

Projede iki Function aynı matematiksel etkiyi yaptığı için sonuç aynıdır; farklı
işlemlerde sıra farklı sonuç üretebilir.

## 3.7 `Function<String,String>` — Listeyi yeni listeye dönüştürme

Kaynak: `functional/function/StringListFunctionProcessor.java`

Lambda uygulaması:

```java
Function<String, String> toUppercase =
        value -> value.toUpperCase();
```

Klasik döngüde açık çağrı:

```java
for (String value : inputStrings) {
    String convertedValue = toUppercase.apply(value);
    convertedStrings.add(convertedValue);
}
```

Stream ile kullanım:

```java
List<String> converted = inputStrings.stream()
        .map(toUppercase)
        .toList();
```

`map(toUppercase)`, her String için içeride şunu çağırır:

```java
toUppercase.apply(value);
```

---

# 4. `Consumer<T>` kullanımları

Hazır interface'in temel abstract metodu:

```java
void accept(T value);
```

Genel yapı:

```text
Bir T alır → bir işlem yapar → sonuç döndürmez
```

Kısa gösterim:

```text
T → void
```

Genel lambda ve çağrı kalıbı:

```java
Consumer<T> action = value -> doSomething(value);
action.accept(value);
```

## 4.1 `Consumer<String>` — Büyük harfle yazdırma

Tek abstract metodun bu örnekteki hâli:

```text
Bir String alır → işlem yapar → sonuç döndürmez
```

Kısa gösterim:

```text
String → void
```

Lambda uygulaması:

```java
Consumer<String> printUppercase =
        text -> System.out.println(text.toUpperCase());
```

Davranışı çalıştıran çağrı:

```java
printUppercase.accept("hello world");
```

Ekran çıktısı:

```text
HELLO WORLD
```

Consumer dışarı bir String döndürmez; yazdırma işlemin kendisidir.

## 4.2 `Consumer<Integer>` — Sayının karesini yazdırma

Tek abstract metodun bu örnekteki hâli:

```text
Bir Integer alır → işlem yapar → sonuç döndürmez
```

Kısa gösterim:

```text
Integer → void
```

Lambda uygulaması:

```java
Consumer<Integer> printSquare =
        number -> System.out.println(number * number);
```

Tek değer için çağrı:

```java
printSquare.accept(5); // Ekrana 25 yazar
```

Liste için kullanım:

```java
numbers.forEach(printSquare);
```

`forEach()`, her sayı için içeride şunu çağırır:

```java
printSquare.accept(number);
```

## 4.3 `Consumer<String>` — Metin uzunluğunu yazdırma

Lambda uygulaması:

```java
Consumer<String> printLength =
        text -> System.out.println(text.length());
```

Tek değer için çağrı:

```java
printLength.accept("Alice"); // Ekrana 5 yazar
```

Liste için kullanım:

```java
names.forEach(printLength);
```

## 4.4 `Consumer<String>` — Listedeki değerleri büyük harfle yazdırma

Kaynak: `functional/consumer/StringListConsumerProcessor.java`

Lambda uygulaması:

```java
Consumer<String> printUppercase =
        value -> System.out.println(value.toUpperCase());
```

Birinci kullanım — döngü içinde açık `accept()`:

```java
for (String value : inputStrings) {
    printUppercase.accept(value);
}
```

İkinci kullanım — `forEach()`:

```java
inputStrings.forEach(printUppercase);
```

İki kullanım da her eleman için aynı `accept()` davranışını çalıştırır.

## 4.5 Consumer birleştirme — `andThen()`

İki Consumer:

```java
Consumer<String> printWithMessage =
        text -> System.out.println("Hello: " + text);

Consumer<String> printUppercase =
        text -> System.out.println(text.toUpperCase());
```

Birleştirme ve çağrı:

```java
printWithMessage
        .andThen(printUppercase)
        .accept("world");
```

Kısa akış:

```text
                 ┌→ printWithMessage.accept("world")
"world" aynı girdi
                 └→ printUppercase.accept("world")
```

Çıktı:

```text
Hello: world
WORLD
```

İlk Consumer'ın çıktısı ikinciye aktarılmaz. İki Consumer da aynı String'i alır;
çünkü Consumer'ın dönüş tipi `void`dur.

---

# 5. Method reference hızlı özeti

Method reference, yalnızca mevcut bir metodu çağıran lambda'nın kısa yazımıdır.

## 5.1 Static method reference

Şablon:

```text
ClassName::staticMethodName
```

Lambda:

```java
(a, b) -> Integer.sum(a, b)
```

Method reference:

```java
Integer::sum
```

Functional interface ve çağrı:

```java
ArithmeticOperations addition = Integer::sum;
int result = addition.performOperation(10, 5);
```

## 5.2 Belirli olmayan nesnenin instance metoduna reference

Şablon:

```text
ClassName::instanceMethodName
```

Lambda:

```java
text -> text.toUpperCase()
```

Method reference:

```java
String::toUpperCase
```

Functional interface ve çağrı:

```java
Function<String, String> toUppercase = String::toUpperCase;
String result = toUppercase.apply("John");
```

Başka örnek:

```java
Function<String, Integer> getLength = String::length;
Integer result = getLength.apply("Alice");
```

## 5.3 Belirli bir nesnenin instance metoduna reference

Projede `System.out::println` kullanılır:

```java
values.forEach(System.out::println);
```

Kavramsal lambda karşılığı:

```java
values.forEach(value -> System.out.println(value));
```

Burada `System.out` belirli bir nesnedir; referans verilen instance metodu
`println`dır.

---

# 6. Hepsini tek tabloda gör

| Functional interface | Abstract metot | Kısa tip akışı | Örnek lambda | Çalıştırma |
|---|---|---|---|---|
| `Hello` | `void sayHello()` | `() → void` | `() -> println(...)` | `hello.sayHello()` |
| `ArithmeticOperation` | `int operation(int,int)` | `(int,int) → int` | `(a,b) -> a+b` | `addition.operation(1,2)` |
| `ArithmeticOperations` | `int performOperation(int,int)` | `(int,int) → int` | `(a,b) -> a-b` | `subtraction.performOperation(10,5)` |
| `Predicate<Integer>` | `boolean test(Integer)` | `Integer → boolean` | `n -> n % 2 == 0` | `isEven.test(4)` |
| `Predicate<String>` | `boolean test(String)` | `String → boolean` | `s -> s.length() >= 5` | `predicate.test("mehmet")` |
| `Predicate<User>` | `boolean test(User)` | `User → boolean` | `u -> u.isActive()` | `isActive.test(user)` |
| `Predicate<Person>` | `boolean test(Person)` | `Person → boolean` | `p -> p.getAge() > 29` | `isOlder.test(person)` |
| `Function<String,String>` | `String apply(String)` | `String → String` | `s -> s.toUpperCase()` | `function.apply("John")` |
| `Function<String,Integer>` | `Integer apply(String)` | `String → Integer` | `s -> s.length()` | `function.apply("Alice")` |
| `Function<Integer,Integer>` | `Integer apply(Integer)` | `Integer → Integer` | `n -> n*n` | `square.apply(5)` |
| `Consumer<String>` | `void accept(String)` | `String → void` | `s -> println(s)` | `consumer.accept("Hello")` |
| `Consumer<Integer>` | `void accept(Integer)` | `Integer → void` | `n -> println(n*n)` | `consumer.accept(5)` |

# 7. En kısa ezber

```text
Özel interface       → Interface'te hangi tek abstract metot varsa onu çağır.
Predicate<T>         → T alır, boolean döndürür → test()
Function<T,R>        → T alır, R döndürür       → apply()
Consumer<T>          → T alır, void döndürür    → accept()
```

Koleksiyon bağlantısı:

```text
filter(predicate) → Her eleman için predicate.test(...)
map(function)     → Her eleman için function.apply(...)
forEach(consumer) → Her eleman için consumer.accept(...)
```

Method reference:

```text
Lambda yalnızca hazır bir metodu çağırıyorsa daha kısa referans yazımı mümkün olabilir.
```
