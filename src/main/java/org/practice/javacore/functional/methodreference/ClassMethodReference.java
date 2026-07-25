package org.practice.javacore.functional.methodreference;

import java.util.List;

public class ClassMethodReference {
    static void main() {
        var list = List.of("amsterdam", "new york", "london", "paris");
        //foreach() accepts Consumer

        //Lambda can be replaced with method reference
        list.forEach(s -> System.out.println(s));
        //this version is beter
        list.forEach(System.out::println);
        list.forEach(s -> System.out.println(s.toUpperCase()));
    }
}
