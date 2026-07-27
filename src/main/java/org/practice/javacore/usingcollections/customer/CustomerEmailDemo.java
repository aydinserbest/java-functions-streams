package org.practice.javacore.usingcollections.customer;

import java.util.List;
import java.util.function.Consumer;

public class CustomerEmailDemo {
    //record, yalnızca Customer modelini tanımlar; müşteri listesini kendiliğinden oluşturmaz.
    // Customer veri tipini tanımlar.
    record Customer(String name, String email) {}
    static void main() {
        // Gerçek müşteri verilerini oluşturur.
        List<Customer> customers = List.of(
                new Customer("Alice", "alice@example.com"),
                new Customer("Bob", "bob@example.com"),
                new Customer("Charlie", "charlie@example.com")
        );
        Consumer<Customer> emailSender = customer -> System.out.println("Sending email to: " + customer.email());
        customers.forEach(emailSender);
        //customers.forEach(customer -> System.out.println(customer.email()));
    }
}
