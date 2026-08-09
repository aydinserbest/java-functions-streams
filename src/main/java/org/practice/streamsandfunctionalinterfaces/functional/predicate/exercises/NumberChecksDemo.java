package org.practice.streamsandfunctionalinterfaces.functional.predicate.exercises;

import java.util.function.Predicate;

public class NumberChecksDemo {
    public static void main(String[] args) {
        Predicate<Integer> isPositive =
                number -> number > 0;

        Predicate<Integer> isEven =
                number -> number % 2 == 0;

        Predicate<Integer> isBetweenTenAndHundred =
                number -> number >= 10 && number <= 100;

        Predicate<Integer> isDivisibleByThree =
                number -> number % 3 == 0;

        System.out.println(checkNumber(12, isPositive)); // true
        System.out.println(checkNumber(12, isEven)); // true
        System.out.println(checkNumber(12, isBetweenTenAndHundred)); // true
        System.out.println(checkNumber(12, isDivisibleByThree)); // true

    }
    static boolean checkNumber(int number, Predicate<Integer> condition) {
        return condition.test(number);
    }
}
/*

Business açısından NumberChecksDemo örneğinde yapılan şey
Buradaki gerçek hedef tek başına “sayı çift mi?” sorusu değildir. Asıl hedef şudur:

  > Aynı kontrol mekanizmasına farklı kurallar vererek farklı kararlar alabilmek.

  ## Gerçek bir senaryo düşünelim

  Bir ödeme veya taksit sistemi düşünelim. Kullanıcı bir ödeme tutarı giriyor:

  Ödeme tutarı: [ 12 ]
                [ Kontrol Et ]

  Sistem bu sayı üzerinde farklı kontroller yapmak isteyebilir:

  - Tutar pozitif mi?
  - Sayı çift mi?
  - İzin verilen aralıkta mı?
  - Belirli bir sayıya tam bölünüyor mu?

  Bu kontrollerin her biri farklı bir business rule, yani iş kuralıdır.


  Predicate<Integer> isPositive =
          number -> number > 0;

  Predicate<Integer> isEven =
          number -> number % 2 == 0;

  Predicate<Integer> isBetweenTenAndHundred =
          number -> number >= 10 && number <= 100;

  Predicate<Integer> isDivisibleByThree =
          number -> number % 3 == 0;

  Her Predicate bir soruyu temsil ediyor:

  isPositive              → Bu sayı pozitif mi?
  isEven                  → Bu sayı çift mi?
  isBetweenTenAndHundred  → Bu sayı izin verilen aralıkta mı?
  isDivisibleByThree      → Bu sayı 3'e tam bölünebilir mi?

  checkNumber() şunların hiçbirini bilmez:

  - Pozitif sayının ne olduğu
  - Çift sayının ne olduğu
  - Geçerli sayı aralığının ne olduğu
  - Sayının kaça bölünmesi gerektiği

  Bunları dışarıdan verilen Predicate belirler.
 */
