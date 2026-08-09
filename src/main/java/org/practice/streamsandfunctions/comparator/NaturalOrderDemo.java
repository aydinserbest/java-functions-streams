package org.practice.streamsandfunctions.comparator;

import java.util.Comparator;
import java.util.List;

public class NaturalOrderDemo {

    public static void main(String[] args) {
        List<Integer> numbers = List.of(40, 5, 20, 10);
        List<String> cities = List.of("Paris", "Amsterdam", "Berlin");

        /*
         * Bir listeyi sıralayabilmek için Java'nın iki elemandan hangisinin
         * önce, hangisinin sonra geleceğini bilmesi gerekir.
         *
         * Comparable<T>, bir nesnenin kendisini aynı türde başka bir nesneyle
         * karşılaştırabilmesini sağlayan interface'tir. Temel metodu:
         *
         * int compareTo(T other)
         *
         * compareTo() sonucu:
         * negatif -> bu nesne diğer nesneden önce gelir
         * sıfır   -> iki nesne sıralama bakımından eşittir
         * pozitif -> bu nesne diğer nesneden sonra gelir
         *
         * Bir sınıf Comparable'ı implement edip compareTo() metodunu yazmışsa
         * o sınıfın elemanları için bir "doğal sıra" tanımlanmış olur.
         * sorted() gibi sıralama işlemleri bu doğal sırayı kullanabilir.
         *
         * Integer ve String sınıfları Java tarafından yazılmıştır ve
         * Comparable interface'ini zaten implement eder.
         *
         * Bu nedenle Java onların "doğal sırasını" bilir:
         * Integer -> küçük sayıdan büyük sayıya
         * String  -> sözlük/karakter sırasına göre
         *
         * Burada bizim karşılaştırma kuralı yazmamız gerekmez.
         * sorted() parametresiz çağrılınca elemanların compareTo() metodunu
         * kullanır.
         */
        List<Integer> sortedNumbers = numbers.stream()
                .sorted()
                .toList();

        List<String> sortedCities = cities.stream()
                .sorted()
                .toList();

        System.out.println("Natural number order: " + sortedNumbers);
        System.out.println("Natural String order: " + sortedCities);

        /*
         * Comparator.naturalOrder() aynı doğal sıralamayı açıkça ifade eder.
         * reversed() ise mevcut karşılaştırma kuralının yönünü tersine çevirir.
         */
        List<Integer> descendingNumbers = numbers.stream()
                .sorted(Comparator.reverseOrder())
                .toList();

        List<String> descendingCities = cities.stream()
                .sorted(Comparator.<String>naturalOrder().reversed())
                .toList();

        System.out.println("Descending numbers: " + descendingNumbers);
        System.out.println("Descending cities: " + descendingCities);

        /*
         * Önemli:
         * Integer ve String, Comparable'ı extend etmez; implement eder.
         * Çünkü Comparable bir interface'tir:
         *
         * public final class Integer implements Comparable<Integer>
         * public final class String implements Comparable<String>
         */
    }
}
