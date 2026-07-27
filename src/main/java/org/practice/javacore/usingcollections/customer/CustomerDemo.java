package org.practice.javacore.usingcollections.customer;

import java.util.function.Consumer;

public class CustomerDemo {
    static void main() {
        Consumer<String> welcome = name -> System.out.println("Welcome customer: " + name);
        //Consumer<String> welcome = System.out::println;

        welcome.accept( "Alice");
        welcome.accept( "Bob");
    }
}
