package org.practice.streamsandfunctionalinterfaces.comparable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class NaturalOrder {
    static void main() {
        List<Person> personList = Arrays.asList(
                new Person("John", 30),
                new Person("Sara", 21),
                new Person("Jane", 41),
                new Person("Greg", 35)
        );
        //personList.sort(null); // natural ordering (Comparable)

        personList.sort(Comparator.naturalOrder()); // natural ordering (Comparable)

        //İkisi de sonunda Person.compareTo() tarafından tanımlanan natural order'a gider.

        /*
        Peki naturalOrder() nereden biliyor yaşa göre sıralayacağını?
Bilmiyor!
Bu çok önemli.
Comparator.naturalOrder() sadece:
"Bu nesnenin kendi doğal sıralamasını kullan."

diyor.
Doğal sıralamanın yaş olduğunu söyleyen yer hâlâ Person içindeki:
@Override
public int compareTo(Person other) {
    return Integer.compare(this.age, other.age);
}
         */
    }


}
