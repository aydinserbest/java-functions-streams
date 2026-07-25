package org.practice.javacore.functional.methodreference;

public class InstanceMethodReferenceDemo {
    public static void main(String[] args) {
        /*
        Instance method reference kısa özeti:

        1. Lambda yalnızca var olan uygun bir instance metodu çağırıyorsa,
           lambda yerine method reference kullanılabilir.
        2. Referans verilen metot bizim daha önce yazdığımız bir instance metot (ör. add) olabilir.
        3. Yazım biçimi: instance::methodName
           Metot çağrılmadığı için parantez yazılmaz.
        4. Functional interface metodu ile instance metodun parametre ve dönüş
           tipleri uyumlu olmalıdır; metot isimlerinin aynı olması gerekmez.
         */

        // Lambda yaklaşımı: performOperation(int, int) metodunun gövdesini
        // toplama işlemiyle burada doğrudan tanımlıyoruz.
        ArithmeticOperations addition = (a, b) -> a + b;

        InstanceMethodReferenceDemo instanceMethodReferenceDemo = new InstanceMethodReferenceDemo();
        ArithmeticOperations additionMethodRef = instanceMethodReferenceDemo::add;

        // Davranışlar performOperation() çağrıldığında gerçekten çalışır.
        System.out.println("Lambda result: " + addition.performOperation(1, 2));
        System.out.println("Method reference result: "
                + additionMethodRef.performOperation(1, 2));

        ArithmeticOperations subtractionMethodRef = instanceMethodReferenceDemo::substraction;
        System.out.println("Substraction result: "
                + subtractionMethodRef.performOperation(5, 3));

        ArithmeticOperations multiplicationMethodRef = instanceMethodReferenceDemo::multiply;
        System.out.println("Multiplication result: "
                + multiplicationMethodRef.performOperation(4, 6));
    }

    // İmza uyumu:
    // ArithmeticOperations.performOperation(int, int) -> int
    // InstanceMethodReferenceDemo.add(int, int)       -> int
    public int add(int a, int b) {
        return a + b;
    }
    public int substraction(int a, int b) {
        return a - b;
    }
    public int multiply(int a, int b) {
        return a * b;
    }
}
