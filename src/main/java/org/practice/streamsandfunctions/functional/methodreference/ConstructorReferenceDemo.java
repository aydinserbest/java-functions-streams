package org.practice.streamsandfunctions.functional.methodreference;

public class ConstructorReferenceDemo {
    public static void main(String[] args) {
        /*
        Constructor reference kısa özeti:

        ProductInterface.getProduct(String, int) bir Product döndürür;
        Product(String, int) constructor'ı da aynı iki değeri alıp Product üretir.
        İmzalar uyduğu için "new Product(name, price)" lambda'sı Product::new
        şeklinde kısaltılabilir. İki tanımda da nesne henüz oluşturulmaz;
        getProduct() çağrılınca constructor çalışır.
         */

        // 1. yaklaşım: Constructor'ı çağıran lambda.
        ProductInterface lambdaFactory =
                (name, price) -> new Product(name, price);

        // 2. yaklaşım: Aynı lambda'nın constructor reference ile kısa yazımı.
        // Constructor reference şablonu: ClassName::new
        ProductInterface constructorReferenceFactory = Product::new;

        // Referansın tipi ProductInterface olduğu için çağrıda interface'in
        // getProduct() metot adını kullanırız; çağrı Product constructor'ına gider.
        Product laptop = lambdaFactory.getProduct("Laptop", 1000);
        Product mouse = constructorReferenceFactory.getProduct("Mouse", 40);

        System.out.println("Lambda: " + laptop);
        System.out.println("Constructor reference: " + mouse);
    }
}
