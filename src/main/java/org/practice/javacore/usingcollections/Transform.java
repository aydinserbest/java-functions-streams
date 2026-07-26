package org.practice.javacore.usingcollections;

import java.util.ArrayList;
import java.util.List;
/*
business ihtiyacı şu:

  > Uygulamadaki arkadaş isimlerini değiştirmeden,
  ekranda büyük harfli olarak göstermek veya büyük harfli yeni bir liste oluşturmak.

  Başlangıç listesi:   [Alice, Bob, Charlie, David]

  Beklenen dönüştürülmüş değerler:   [ALICE, BOB, CHARLIE, DAVID]
 */
/*
 Business ihtiyacı şudur:

  > Her ismi büyük harfli yeni bir isme dönüştür ve sonuçları listeye topla.

  Bu doğrudan map() ve toList() ihtiyacıdır:
 */
public class Transform {
    public static void main(String[] args) {
        List<String> friends = List.of("Alice", "Bob", "Charlie", "David");
        //1. way
        friends.stream()
                .map(String::toUpperCase) //function interface'i alır //name -> name.toUpperCase())
                .forEach(System.out::println); //consumer interface'i alır //forEach(name -> System.out.print(name + " "))
        //2. way
        List<String> upperCase = new ArrayList<>();

        for (String name : friends) {
            upperCase.add(name.toUpperCase());
        }
        System.out.println(upperCase);
        //3. way
        List<String> uppercaseNames = new ArrayList<>();
        friends.forEach(name -> uppercaseNames.add(name.toUpperCase()));//BAD IDEA
        System.out.println(uppercaseNames);

        // --
        friends.stream()
                .map(name -> name.toUpperCase())
                .forEach(name -> System.out.print(name + " "));
        System.out.println();

        friends.stream()
                .map(String::length) //name -> name.length()
                .forEach(length -> System.out.print(length + " "));

    }
}
/*
.map() method is useful to map or transform an input collection into a new output collection.
 forEach() ise daha çok şunu ifade eder:

  > Her isim için sonuç döndürmeyen bir işlem yap.
 */
