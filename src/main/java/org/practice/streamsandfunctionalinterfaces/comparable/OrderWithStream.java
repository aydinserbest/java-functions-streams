package org.practice.streamsandfunctionalinterfaces.comparable;

import java.util.List;

public class OrderWithStream {
    static void main() {
        List<Person> personList = List.of(
                new Person("John", 30),
                new Person("Sara", 21),
                new Person("Jane", 41),
                new Person("Greg", 35)
        );
        //personList.sort(null); // ORİJİNAL LİSTE DEĞİŞİR
        //void 'dir, yani çağrıldığı listeyi yerinde değiştirir
        /*
            * Stream kullanarak sıralama yaptığımızda, mevcut listeyi değiştirmeyiz.
            * Bunun yerine, sıralanmış yeni bir liste üretiriz.
            */

        List<Person> newOrdererdList = personList.stream() // STREAM BAŞLAT
                .sorted() // SIRALA | natural order (Comparable)
                .toList(); // LİSTEYE TOPLA

        System.out.println("Natural order: " + newOrdererdList);
        newOrdererdList.forEach(System.out::println);
        /*
        Buradaki:
.sorted()
parametresiz.
Java yine diyor ki:
"Bana özel bir Comparator verilmemiş. O zaman elemanların natural order'ını kullanacağım."

Person'ın natural order'ı nerede?
@Override
public int compareTo(Person other) {
    return Integer.compare(this.age, other.age);
}
Burada.
         */
    }
}
