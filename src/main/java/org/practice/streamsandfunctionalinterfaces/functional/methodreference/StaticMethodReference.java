package org.practice.streamsandfunctionalinterfaces.functional.methodreference;

public class StaticMethodReference {
    public static void main(String[] args) {
        //Lambda can be replaced with method reference :
        //        AritmethicOperations addition = (a, b) -> a + b;
        /*
        lambda’nın yaptığı iş Java’da zaten var olan bir static metotla
        birebir ifade edilebilir
        Java’nın Integer sınıfında sum isminde hazır bir static metot var
         */

        ArithmeticOperations addition = Integer::sum;

        /*
        Diğer lambdalar neden aynı şekilde değişmedi?
        Java’da toplama için doğrudan:

  Integer.sum(int, int)

  bulunur.

  Çıkarma ve çarpma için Math sınıfında şunlar vardır:

  Math::subtractExact
  Math::multiplyExact

  Fakat bunlar taşma durumunda exception fırlatabilir; ham a - b ve a * b ile davranışları tamamen aynı değildir. Bu nedenle otomatik değiştirme her zaman önerilmeyebilir.

  Bölme için Integer::divide şeklinde bir hazır static metot yoktur. Bu yüzden lambda uygun kalır:

         */
        ArithmeticOperations subtraction = (a, b) -> a - b;
        ArithmeticOperations multiplication = (a, b) -> a * b;
        ArithmeticOperations division = (a, b) -> a / b;

        System.out.println("Addition: " + addition.performOperation(10, 5));
        System.out.println("Subtraction: " + subtraction.performOperation(10, 5));
        System.out.println("Multiplication: " + multiplication.performOperation(10, 5));
        System.out.println("Division: " + division.performOperation(10, 5));
    }
}
