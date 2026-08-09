package org.practice.streamsandfunctions.streams.reduce;

import java.util.Arrays;
import java.util.List;

public class StreamReduceDemo {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        numbers.stream()
                .reduce(Integer::sum) //((a, b) -> a + b)
                .ifPresent(sum -> System.out.println("Sum: " + sum));

        Integer sum = numbers.stream()
                .reduce(0, Integer::sum);
        System.out.println("Sum: " + sum);

        int result = numbers.stream()
                .mapToInt(num -> num).sum();
        System.out.println("Sum: " + result);

        /*
        Sadece listenin toplam boyutunu istiyorsan:

  int count = strings.size();

  daha doğrudur.

  Ama Stream işlemlerinden sonra kalan elemanları sayıyorsan count() anlamlıdır:

count(), long döner,
 size() int döner
         */
        List<String> cities = Arrays.asList("amsterdam", "paris", "berlin", "new york");
        System.out.println(cities.stream()
                .count()); //System.out.println((long) cities.size());

        cities.stream()
                .filter(city -> city.length() > 5)
                .forEach(city -> System.out.println("City with more than 5 characters: " + city));
    }
}
