package org.practice.javacore.consumer;

import java.util.List;
import java.util.function.Consumer;

/**
 * Requirement: String listesindeki her değeri büyük harfe çevirip konsola yazdır.
 * Yeni bir liste veya String sonucu döndürme; yazdırma işlemin kendisidir.
 */
public class StringListConsumerProcessor {

    public static void main(String[] args) {
        List<String> values = List.of("apple", "banana", "carrot");

        System.out.println("1. alternatif: for döngüsü + accept()");
        convertAndPrint(values);

        System.out.println("2. alternatif: forEach()");
        convertAndPrintWithForEach(values);
    }

    public static void convertAndPrint(List<String> inputStrings) {
        Consumer<String> printUppercase =
                value -> System.out.println(value.toUpperCase());

        // Consumer'ı her eleman için açıkça accept() ile çalıştırıyoruz.
        for (String value : inputStrings) {
            printUppercase.accept(value);
        }
    }

    public static void convertAndPrintWithForEach(List<String> inputStrings) {
        Consumer<String> printUppercase =
                value -> System.out.println(value.toUpperCase());

        // forEach(), Consumer'ın accept() metodunu her eleman için kendisi çağırır.
        inputStrings.forEach(printUppercase);
    }
}

