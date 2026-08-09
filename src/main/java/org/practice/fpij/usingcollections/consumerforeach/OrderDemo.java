package org.practice.fpij.usingcollections.consumerforeach;

import java.util.List;
import java.util.function.Consumer;

public class OrderDemo {
    public static void main(String[] args) {
        List<Long> orderIds = List.of(1L, 2L, 3L, 4L, 5L);
        Consumer<Long> order = id -> System.out.println("Order ID: " + id);

        orderIds.forEach(order); //foreach Consumer bekler
        //direk consumer lambdasını da yazabilirdik:
        orderIds.forEach(id -> System.out.println("Order ID: " + id));
        /*
        order.accept(1L); işlemini, herbir orderIds elementi için
        foreach arka planda yapar

        Consumer’ı tek bir değer üzerinde kendimiz çalıştırırken accept() kullanırız:

  consumer.accept(value);

  Bir koleksiyon veya stream üzerinde forEach() kullandığımızda ise forEach(), Consumer’ın accept() metodunu her eleman için kendisi çağırır.

  forEach(), arka planda kavramsal olarak şunu yapar:

for(Long id : orderIds) {
    order.accept(id);
}
yani:
order.accept(1L);
order.accept(2L);
order.accept(3L);

Burada accept() yazmadık; forEach() bizim yerimize her eleman için çağırdı.
         */
    }
}
