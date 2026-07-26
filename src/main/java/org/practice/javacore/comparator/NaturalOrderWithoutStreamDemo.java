package org.practice.javacore.comparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NaturalOrderWithoutStreamDemo {

    public static void main(String[] args) {
        /*
         * Stream kullanmadan sıralama yaptığımızda mevcut liste sıralanır.
         * List.of() ile oluşan liste değiştirilemediği için sıralama öncesinde
         * değerleri değiştirilebilir bir ArrayList içine koyuyoruz.
         */
        List<Integer> numbers =
                new ArrayList<>(List.of(40, 5, 20, 10));

        List<String> cities =
                new ArrayList<>(List.of("Paris", "Amsterdam", "Berlin"));

        /*
         * Collections.sort(list), elemanların doğal sırasını kullanır.
         *
         * Integer ve String Comparable'ı zaten implement ettiği için
         * ayrıca Comparator vermemiz gerekmez.
         *
         * Bu işlem yeni bir liste döndürmez; mevcut ArrayList'in sırasını
         * doğrudan değiştirir.
         */
        Collections.sort(numbers);
        Collections.sort(cities);

        System.out.println("Natural number order: " + numbers);
        System.out.println("Natural String order: " + cities);

        /*
         * Ters sıralama için mevcut listeleri Comparator.reverseOrder()
         * kullanarak tekrar sıralıyoruz.
         *
         * List.sort(comparator), Collections.sort(list, comparator)
         * kullanımının modern ve doğrudan List üzerindeki karşılığıdır.
         */
        numbers.sort(Comparator.reverseOrder());
        cities.sort(Comparator.reverseOrder());

        System.out.println("Descending numbers: " + numbers);
        System.out.println("Descending cities: " + cities);

        /*
         * Kısa fark:
         *
         * Stream yaklaşımı:
         * Yeni sıralı bir liste üretir; kaynak listeyi değiştirmez.
         *
         * Collections.sort() / List.sort():
         * Mevcut değiştirilebilir listenin sırasını değiştirir.
         */
    }
}
