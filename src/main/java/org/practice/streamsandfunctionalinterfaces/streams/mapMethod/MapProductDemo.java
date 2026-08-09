package org.practice.streamsandfunctionalinterfaces.streams.mapMethod;

import org.practice.streamsandfunctionalinterfaces.streams.collectingAndThen.Product;

import java.util.Comparator;
import java.util.List;

/*
 business amacı şudur:

  > Ürün kataloğundaki fiyatı en yüksek ürünü bul
  ve ekranda göstermek üzere yalnızca ürünün adını üret.
   Listede hiç ürün yoksa kullanıcıya "No products found" mesajını ver.

 */
public class MapProductDemo {
    static void main() {
        List<Product> products = List.of(
                new Product("Laptop", 1200),
                new Product("Smartphone", 800),
                new Product("Tablet", 600),
                new Product("Monitor", 300),
                new Product("Keyboard", 100));

        /*
         * max() Stream'i bitirir ve en pahalı ürünü Optional<Product> içinde döndürür.
         * Bu nedenle aşağıdaki map(), Stream.map() değil Optional.map() metodudur.
         * Optional doluysa map(), içindeki Product nesnesine Product::getName uygular.
         * Böylece Product("Laptop", 1200) değeri "Laptop" String'ine dönüştürülür.
         * Tip akışı Optional<Product> -> Optional<String> şeklindedir.
         * Optional boşsa Product::getName çalışmaz ve Optional boş kalır.
         * orElse(), boş Optional durumunda "No products found" sonucunu verir.
         */
        String mostExpensiveProductName = products.stream()
                .max(Comparator.comparing(Product::getPrice))
                .map(Product::getName)
                .orElse("No products found");
        System.out.println(mostExpensiveProductName);
    }
}
/*
Senin ürün listen için max() çalıştıktan sonra oluşan Optional<Product> kabaca şöyle görünür:

  Optional[
      Product {
          name = "Laptop",
          price = 1200
      }
  ]

  Daha kısa gösterirsek:

  Optional[Product("Laptop", 1200)]
  Yani Optional’ın içinde en pahalı ürün nesnesi vardır:
  Optional<Product>
           ↓
        Product
        name:  "Laptop"
        price: 1200

  Sonraki işlem:
  .map(Product::getName)
  Optional içindeki Product nesnesinden adı alır:
  Önce:
  Optional[
      Product {
          name = "Laptop",
          price = 1200
      }
  ]
  Sonra:
  Optional[
      "Laptop"
  ]
  Tip değişimi:
  Optional<Product>
          ↓ map(Product::getName)
  Optional<String>
 */
