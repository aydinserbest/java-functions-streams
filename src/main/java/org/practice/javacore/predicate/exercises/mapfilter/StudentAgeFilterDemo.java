package org.practice.javacore.predicate.exercises.mapfilter;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class StudentAgeFilterDemo {

    static void main() {
        // Map'te key öğrenci adını, value öğrencinin yaşını temsil eder.
        Map<String, Integer> students = Map.of(
                "Ali", 17,
                "Ayşe", 20,
                "Mehmet", 16,
                "Zeynep", 22
        );

        // Predicate'i önceden isimlendirirsek aynı koşulu tekrar kullanabiliriz.
        Predicate<Integer> isOlderThan18 = age -> age > 18;

        // values() yalnızca yaşları verir. Bu nedenle sonuçta isimler bulunmaz.
        List<Integer> adultAges = students.values().stream()
                .filter(isOlderThan18)
                .toList();

        System.out.println("18 yaşından büyük yaşlar: " + adultAges);

        // entrySet() isim-yaş çiftlerini korur. Lambda doğrudan filter'a verildiği
        // için ayrıca Predicate<Map.Entry<String, Integer>> değişkeni tanımlanmadı.
        List<Map.Entry<String, Integer>> adultStudents = students.entrySet().stream()
                .filter(entry -> entry.getValue() > 18)
                .toList();

        System.out.println("18 yaşından büyük öğrenciler: " + adultStudents);

        // Önce yaşa göre filtreler, sonra map() ile Entry nesnesini isme dönüştürürüz.
        List<String> adultStudentNames = students.entrySet().stream()
                .filter(entry -> entry.getValue() > 18)
                .map(entry -> entry.getKey())
                .toList();

        System.out.println("18 yaşından büyük öğrenci isimleri: " + adultStudentNames);
    }
}
