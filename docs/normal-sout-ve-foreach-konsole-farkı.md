static void main() {

       
Person p1 = new Person("John", 30);
Person p2 = new Person("Sara", 21);
Person p3 = new Person("Jane", 41);
Person p4 = new Person("Greg", 35);

List<Person> personList = Arrays.asList(p1, p2, p3, p4);

System.out.println("After sorting:");

        System.out.println(personList); //[Sara - 21, John - 30, Greg - 35, Jane - 41] yazar
        personList.forEach(System.out::println);
        /*
        Sara - 21
        John - 30
        Greg - 35
        Jane - 41
        yazar

İkisi de aynı `Person` nesnelerini yazdırıyor ama **yazdırma şekilleri farklı**.

### 1. Listenin tamamını tek seferde yazdırıyorsun

```java
System.out.println(personList);
```

Çıktı:

```text
[Sara - 21, John - 30, Greg - 35, Jane - 41]
```

Burada `println`'e verdiğin şey:

```java
personList
```

yani **List nesnesinin tamamı**.

Java kabaca:

```text
List'in toString()'i
        ↓
[
 eleman1.toString(),
 eleman2.toString(),
 eleman3.toString()
]
```

şeklinde çalışır.

Bu yüzden:

- `[` `]` görürsün
- elemanlar `,` ile ayrılır
- hepsi aynı satırdadır.

---

### 2. Elemanları tek tek yazdırıyorsun

```java
personList.forEach(System.out::println);
```

Çıktı:

```text
Sara - 21
John - 30
Greg - 35
Jane - 41
```

Burada `println`'e **listeyi değil**, her seferinde bir `Person` gönderiyorsun.

Mantığı:

```java
personList.forEach(person -> System.out.println(person));
```

Java kabaca:

```text
Sara nesnesi → println → Sara - 21

John nesnesi → println → John - 30

Greg nesnesi → println → Greg - 35

Jane nesnesi → println → Jane - 41
```

`println` her seferinde yeni satıra geçtiği için alt alta görüyorsun.

### En kısa fark

```java
System.out.println(personList);
```

→ **LISTEYİ yazdır**

```text
[Sara - 21, John - 30, Greg - 35, Jane - 41]
```

```java
personList.forEach(System.out::println);
```

→ **LİSTENİN ELEMANLARINI TEK TEK yazdır**

```text
Sara - 21
John - 30
Greg - 35
Jane - 41
```

Ve ikisinde de senin `Person.toString()` metodun devreye giriyor:

```java
@Override
public String toString() {
    return name + " - " + age;
}
```

Fark şu:

```text
println(personList)
        ↓
List.toString()
        ↓
içeride her Person'ın toString()'i


forEach(println)
        ↓
her Person ayrı ayrı println'e gider
        ↓
her Person'ın toString()'i
```

Yani ilkinde **List'in `toString()`i Person'ların `toString()`lerini topluca kullanıyor**,
ikincisinde **her Person doğrudan ayrı ayrı yazdırılıyor**.
*/
}