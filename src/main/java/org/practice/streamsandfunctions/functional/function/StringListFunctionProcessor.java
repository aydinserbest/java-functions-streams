package org.practice.streamsandfunctions.functional.function;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Alternatif requirement: String listesindeki değerleri büyük harfe dönüştür ve
 * dönüştürülmüş değerlerden oluşan yeni bir List<String> döndür.
 */
public class StringListFunctionProcessor {

    public static void main(String[] args) {
        List<String> values = List.of("apple", "banana", "carrot");

        List<String> convertedWithLoop = convertWithLoop(values);
        System.out.println("Döngü + apply(): " + convertedWithLoop);

        List<String> convertedWithStream = convertWithStream(values);
        System.out.println("Stream map(): " + convertedWithStream);

        // Her iki yöntemde de kaynak liste değişmez.
        System.out.println("Orijinal liste: " + values);
    }

    public static List<String> convertWithLoop(List<String> inputStrings) {
        Function<String, String> toUppercase =
                value -> value.toUpperCase();

        List<String> convertedStrings = new ArrayList<>();

        // Function'ı her elemanda apply() ile çalıştırıp dönen String'i saklıyoruz.
        for (String value : inputStrings) {
            String convertedValue = toUppercase.apply(value);
            convertedStrings.add(convertedValue);
        }

        return convertedStrings;
    }

    public static List<String> convertWithStream(List<String> inputStrings) {
        Function<String, String> toUppercase =
                value -> value.toUpperCase();

        // map(), Function'ın apply() metodunu her eleman için kendisi çağırır.
        return inputStrings.stream()
                .map(toUppercase)
                .toList();
    }
}
