package org.practice.javacore.methodReference;

public class MethodReferenceDemo {
    public static void main(String[] args) {
        /*
        Static method reference kısa özeti:

        1. Lambda yalnızca var olan uygun bir static metodu çağırıyorsa,
           lambda yerine method reference kullanılabilir.
        2. Referans verilen metot Java'nın hazır metodu (ör. Integer.sum) veya
           bizim daha önce yazdığımız bir static metot (ör. add) olabilir.
        3. Yazım biçimi: ClassName::staticMethodName
           Metot çağrılmadığı için parantez yazılmaz.
        4. Functional interface metodu ile static metodun parametre ve dönüş
           tipleri uyumlu olmalıdır; metot isimlerinin aynı olması gerekmez.
         */

        // Lambda yaklaşımı: performOperation(int, int) metodunun gövdesini
        // toplama işlemiyle burada doğrudan tanımlıyoruz.
        ArithmeticOperations addition = (a, b) -> a + b;

        // Method reference yaklaşımı: Aynı işi zaten yapan kendi static add()
        // metodumuzu yeniden lambda içinde çağırmak yerine doğrudan referans veriyoruz.
        // Bu satır add() metodunu henüz çalıştırmaz; davranışı referansta tutar.
        ArithmeticOperations additionMethodRef = MethodReferenceDemo::add;

        // Davranışlar performOperation() çağrıldığında gerçekten çalışır.
        System.out.println("Lambda result: " + addition.performOperation(1, 2));
        System.out.println("Method reference result: "
                + additionMethodRef.performOperation(1, 2));
    }

    // İmza uyumu:
    // ArithmeticOperations.performOperation(int, int) -> int
    // MethodReferenceDemo.add(int, int)               -> int
    public static int add(int a, int b) {
        return a + b;
    }
}
