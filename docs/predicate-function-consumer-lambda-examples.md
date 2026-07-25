# Predicate, Function ve Consumer — Lambda Örnekleri

## Predicate

`Predicate<T>` sözleşmesi:

```text
T → boolean
```

Tek bir değerde çalıştırma:

```java
boolean result = predicate.test(value);
```

Stream bağlantısı:

```java
List<T> filteredValues = values.stream()
        .filter(predicate)
        .toList();
```

> `filter(predicate)`, her eleman için Predicate'in `test()` metodunu içeride
> çalıştırır; `true` dönen elemanlar sonuçta kalır.

```java
number -> number % 2 == 0
```

> Bir sayı al; çift olup olmadığını boolean olarak döndür.

```java
number -> number > 0
```

> Bir sayı al; pozitif olup olmadığını boolean olarak döndür.

```java
number -> number >= 10 && number <= 100
```

> Bir sayı al; 10 ile 100 arasında olup olmadığını boolean olarak döndür.

```java
number -> number % 3 == 0
```

> Bir sayı al; 3'e tam bölünüp bölünmediğini boolean olarak döndür.

```java
number -> number > 18
```

> Bir sayı al; 18'den büyük olup olmadığını boolean olarak döndür.

```java
username -> username != null
        && !username.isBlank()
        && username.length() >= 5
```

> Bir kullanıcı adı al; null, boş olmayan ve en az 5 karakterli olup olmadığını
> boolean olarak döndür.

```java
name -> name.startsWith("A")
```

> Bir isim al; `"A"` harfiyle başlayıp başlamadığını boolean olarak döndür.

```java
user -> user.length() > 3
```

> Bir kullanıcı adı al; uzunluğunun 3'ten büyük olup olmadığını boolean olarak
> döndür.

```java
user -> user.isActive()
```

> Bir User al; aktif olup olmadığını boolean olarak döndür.

```java
person -> person.getAge() > age
```

> Bir Person al; yaşının verilen sınırdan büyük olup olmadığını boolean olarak
> döndür.

```java
person -> person.getCity().equals(city)
```

> Bir Person al; şehrinin verilen şehirle aynı olup olmadığını boolean olarak
> döndür.

```java
entry -> entry.getValue() > 18
```

> Bir öğrenci adı–yaş kaydı al; yaş değerinin 18'den büyük olup olmadığını boolean
> olarak döndür.

```java
Predicate.isEqual("John")
```

> Bir metin al; `"John"` değerine eşit olup olmadığını boolean olarak döndür.

## Function

`Function<T,R>` sözleşmesi:

```text
T → R
```

Tek bir değerde çalıştırma:

```java
R result = function.apply(value);
```

Stream bağlantısı:

```java
List<R> convertedValues = values.stream()
        .map(function)
        .toList();
```

> `map(function)`, her eleman için Function'ın `apply()` metodunu içeride
> çalıştırır ve dönen değerlerden yeni bir akış/liste oluşturur.

```java
text -> text.toUpperCase()
```

> Bir metin al; büyük harfli yeni String olarak döndür.

```java
text -> text.length()
```

> Bir metin al; karakter sayısını Integer olarak döndür.

```java
number -> number * number
```

> Bir sayı al; karesini Integer olarak döndür.

```java
number -> number * 2
```

> Bir sayı al; iki katını Integer olarak döndür.

```java
number -> number + number
```

> Bir sayı al; kendisiyle toplanmış değerini Integer olarak döndür.

```java
doubleValue.andThen(addValue)
```

> Bir sayı al; önce iki katına çıkar, ardından sonucu kendisiyle toplayıp Integer
> olarak döndür.

```java
doubleValue.compose(addValue)
```

> Bir sayı al; önce kendisiyle topla, ardından sonucu iki katına çıkarıp Integer
> olarak döndür.

```java
value -> value.toUpperCase()
```

> Bir String al; büyük harfli String'e dönüştürüp yeni listeye eklenebilecek sonuç
> olarak döndür.

## Consumer

`Consumer<T>` sözleşmesi:

```text
T → void
```

Tek bir değerde çalıştırma:

```java
consumer.accept(value);
```

Collection bağlantısı:

```java
values.forEach(consumer);
```

> `forEach(consumer)`, koleksiyondaki her eleman için Consumer'ın `accept()`
> metodunu içeride çalıştırır; yeni bir değer veya liste döndürmez.

```java
text -> System.out.println(text.toUpperCase())
```

> Bir metin al; büyük harfli biçimini ekrana yazdır ve sonuç döndürme.

```java
number -> System.out.println(number * number)
```

> Bir sayı al; karesini ekrana yazdır ve sonuç döndürme.

```java
text -> System.out.println(text.length())
```

> Bir metin al; karakter sayısını ekrana yazdır ve sonuç döndürme.

```java
text -> System.out.println("Hello: " + text)
```

> Bir metin al; başına `"Hello: "` ekleyerek ekrana yazdır ve sonuç döndürme.

```java
value -> System.out.println(value.toUpperCase())
```

> Listedeki bir String'i al; büyük harfli biçimini ekrana yazdır ve sonuç döndürme.

```java
first.andThen(second)
```

> Bir değer al; aynı değer üzerinde önce birinci, sonra ikinci Consumer işlemini
> çalıştır ve sonuç döndürme.
