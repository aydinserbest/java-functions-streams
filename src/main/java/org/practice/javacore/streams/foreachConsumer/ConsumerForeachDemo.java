package org.practice.javacore.streams.foreachConsumer;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ConsumerForeachDemo {
    static void main() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Davranışı önce bir Consumer referansına atayıp forEach() metoduna verebiliriz.
        // forEach(), listedeki her eleman için Consumer'ın accept() metodunu çalıştırır.
        Consumer<Integer> numbersProcess = number -> System.out.println(number * 2);
        numbers.forEach(numbersProcess);

        // Ayrı bir Consumer referansı oluşturmadan lambdayı doğrudan forEach() metoduna verebiliriz.
        numbers.forEach(number -> System.out.println(number * 2));

        // printDouble() static ve Consumer<Integer> sözleşmesine uygun olduğu için
        // lambda yerine ClassName::methodName biçiminde method reference kullanabiliriz.
        numbers.forEach(ConsumerForeachDemo::printDouble);

        // System.out::println yalnızca sayıyı yazdırır; ikiyle çarpma işlemi yapmaz.
        // number -> System.out.println(number) ifadesinin method-reference karşılığıdır.
        numbers.forEach(System.out::println);
    }

    /*
     * number -> System.out.println(number * 2) lambdası iki işlem yapar:
     * sayıyı ikiyle çarpar ve sonucu yazdırır.
     * Aynı davranışı method reference ile kullanabilmek için bu metodu tanımladık.
     */
    private static void printDouble(int number) {
        System.out.println(number * 2);
    }
}
