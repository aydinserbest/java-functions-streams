package org.practice.streamsandfunctionalinterfaces.comparable;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
/*
        Person p1 = new Person("John", 30);
        Person p2 = new Person("Sara", 21);
        Person p3 = new Person("Jane", 41);
        Person p4 = new Person("Greg", 35);

        List<Person> people = Arrays.asList(p1, p2, p3, p4);

 */
        List<Person> people = Arrays.asList(
                new Person("John", 30),
                new Person("Sara", 21),
                new Person("Jane", 41),
                new Person("Greg", 35)
        );
        System.out.println("Before sorting:");
        people.forEach(System.out::println); //toString metodu nasıl tarif ettiyse, çıktıda o tarz görünür

        people.sort(null); // natural ordering (Comparable)
        /*
        null ile şunu söylüyoruz:
"Ben sana ayrıca bir sıralama kuralı vermiyorum. Person'ın natural order'ını kullan."

Fakat bizim Person şu anda şunu bilmiyor:
Person'ın doğal sıralaması ne?

Yaş mı?
İsim mi?
Boy mu?
Maaş mı?

Java: Bilmiyorum.
İşte Comparable tam burada devreye giriyor.

Bunun Türkçesi:
Person nesneleri birbirleriyle karşılaştırılabilir.

Ama Java şimdi senden bir şey isteyecek:
compareTo(...)
Çünkü Comparable bir interface ve temel sözleşmesi şu:
public interface Comparable<T> {

    int compareTo(T other);
}
Biz:
Comparable<Person>
dediğimiz için:
int compareTo(Person other)
yazmamız gerekiyor.
4. compareTo() metodunu ekle
         */

        System.out.println("After sorting:");
        people.forEach(System.out::println);

        //ARA NOT:
        /*
        üstteki people.sort(null); satırı
        Person sınıfın henüz:
implements Comparable<Person>
olmadığı için ClassCastException hatası verir

sort(null) şu anlama geliyordu:
Ekstra Comparator vermiyorum, Person nesnelerinin kendi natural order'ını kullan.

Ama Java Person'a bakıyor ve diyor ki:
"Bu sınıf Comparable değil, kendi kendini nasıl karşılaştıracağını bilmiyor."
         */

        /*
        Buradaki iki kritik şey lazım:
implements Comparable<Person>
ve:
@Override
public int compareTo(Person other) {
    return Integer.compare(this.age, other.age);
}
         */
        /*
        people.sort(null)
        ↓
natural order kullan
        ↓
Person Comparable mı?
        ↓
EVET → compareTo() kullan
HAYIR → ClassCastException
         */

        /*
        compareTo() ne demek?
Bir Person başka bir Person ile karşılaştırılıyor.
Örneğin:
Person john = new Person("John", 20);
Person greg = new Person("Greg", 35);
Şunu yapabilirim:
john.compareTo(greg);
Burada:
john.compareTo(greg)
 ↑                ↑
this             other
Yani metodun içinde:
this.age
John'un yaşı:
20
ve:
other.age
Greg'in yaşı:
35
Dolayısıyla:
Integer.compare(this.age, other.age);
şuna dönüşüyor:
Integer.compare(20, 35);
ve negatif bir sonuç verir.
         */
    }
}
