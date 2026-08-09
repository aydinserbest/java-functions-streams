package org.practice.fpij.usingcollections.consumerforeach;

import java.util.List;
import java.util.function.Consumer;

public class StockDemo {
    public static void main(String[] args) {
        List<Stock> stocks = List.of(
                new Stock("iphone", 5),
                new Stock("keyboard", 0),
                new Stock("ipad", 10)
        );

        /*
         Consumer<Stock>, accept() metodunun bir Stock nesnesi alacağını belirtir.
         Böylece lambda yalnızca ürün adına değil, aynı nesnenin stok miktarına da
         ulaşabilir.

         Lambda burada sadece davranışı tanımlar; sellOne henüz çalışmaz.
         Consumer yeni bir Stock döndürmez. setQuantity() ile kendisine verilen
         mevcut nesneyi değiştirmesi bir yan etkidir.
        */
        Consumer<Stock> sellOne = stock -> {
            if (stock.getQuantity() > 0) {
                stock.setQuantity(stock.getQuantity() - 1);
                System.out.println(
                        "Sold 1 " + stock.getProductName()
                                + ". Remaining quantity: "
                                + stock.getQuantity()
                );
            } else {
                System.out.println(stock.getProductName() + " is out of stock.");
            }
        };

        Stock iphone = stocks.get(0);
        Stock keyboard = stocks.get(1);

        // accept() davranışı yalnızca verilen Stock nesnesi için çalıştırır.
        sellOne.accept(iphone);   // 5 -> 4
        sellOne.accept(keyboard); // 0 -> 0; stok negatif olmaz

        /*
         Toplu senaryoda aşağıdaki çağrı kullanılabilir:

         stocks.forEach(sellOne);

         forEach(), listedeki her Stock için sellOne.accept(stock) çağırır.
         Bu nedenle bu kod "listedeki her üründen birer tane satmayı dene"
         anlamına gelir; yalnızca tek bir ürünün satışı anlamına gelmez.
        */
    }
}
