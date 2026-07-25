package org.practice.javacore.functional.predicate.exercises;

import java.util.function.Predicate;

public class UsernameValidationDemo {
    //Predicate'in orjinal abstract metodu boolean test(T t);
    // Lambda, Predicate'in abstract test(String) metodunu uygular.
    // test() bir kullanıcı adı alır ve kurallara uygunluğunu boolean olarak döndürür.
    // Koşullar soldan sağa ve kısa devreli çalışır. username null ise diğer
    // koşullar değerlendirilmez; böylece isBlank() çağrısı NullPointerException üretmez.
    private static final Predicate<String> IS_VALID_USERNAME = username ->
            username != null
            && !username.isBlank()
            && username.length() >= 5;

    // Bu metot Predicate döndürmez; Predicate'i çalıştırıp boolean döndürür.
    // Böylece çağıran kod Predicate ve test() ayrıntısını bilmeden
    // yalnızca "kullanıcı adı geçerli mi?" diye sorabilir.
    private static boolean isValidUsername(String username) {
        return IS_VALID_USERNAME.test(username);
    }

    static void main() {
        // Kullanım 1: Predicate'i saran, anlamlı isimli metodu çağırmak.
        System.out.println(isValidUsername("admin"));  // true
        System.out.println(isValidUsername("John"));   // false
        System.out.println(isValidUsername("mehmet")); // true

        // Kullanım 2: Predicate'in test() metodunu doğrudan çağırmak.
        // Predicate öğrenirken lambda davranışının nasıl çalıştırıldığını açıkça gösterir.
        System.out.println(IS_VALID_USERNAME.test(""));   // false
        System.out.println(IS_VALID_USERNAME.test(null)); // false
    }
}

/*
Requirement: Kullanıcı adı doğrulama

Bir kullanıcı adının kurallara uygun olup olmadığını kontrol eden bir
Predicate<String> tanımla.

Kurallar:

- null olmamalı.
- Boş veya yalnızca boşluklardan oluşmamalı.
- En az 5 karakter içermeli.

Test değerleri ve beklenen sonuçlar:

"admin"  -> true
"John"   -> false
"mehmet" -> true
""       -> false
null     -> false
*/
