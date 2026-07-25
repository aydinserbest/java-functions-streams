package org.practice.javacore.streams;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class StreamCreationDemo {
    static void main() {
        List<String> department = new ArrayList<>();
        department.add("HR");
        department.add("Supply");
        department.add("Engineer");
        department.add("Computer Science");
        department.add("Marketing");
        department.add("Finance");
        department.add("Sales");

        Stream<String> departmentStream = department.stream();
        //iterate all the elements and display on the console

        //foreach consumer bekler
        //consumerdan kasıt kendisine atanabailecek lambda ifadesidir-davranış
        //ör:
        /*
        s -> System.out.println(s) bir davranış
        atanırken
        Consumer<String> sout = s -> System.out.println(s)
        sout.apply("John);
         */
        departmentStream.forEach(s -> System.out.println(s));
        //method reference ile
        //class instance'ı :: metot ismi:
        // departmentStream.forEach(System.out::println);

    }
}
