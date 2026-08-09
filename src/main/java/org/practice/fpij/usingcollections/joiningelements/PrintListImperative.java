package org.practice.fpij.usingcollections.joiningelements;

import java.util.List;

public class PrintListImperative {
    public static void main(String[] args) {
        /*
        Konu: "Joining Elements" -- bir koleksiyonu, aralarına ayraç (ör.
        virgül) koyarak TEK bir String'e birleştirmek. Basit görünen bu
        işlem, eski JDK araçlarıyla yapılırsa şaşırtıcı derecede can sıkıcı
        hâle geliyor.
         */
        final List<String> friends =
                List.of("Brian", "Nate", "Neal", "Raju", "Sara", "Scott");

        // ------------------------------------------------------------
        // 1. deneme: for-each ile her ismin arkasına ", " ekle
        // ------------------------------------------------------------
        System.out.println("1) Saf for-each:");
        for (String name : friends) {
            System.out.print(name + ", ");
        }
        System.out.println();
        // Çıktı: Brian, Nate, Neal, Raju, Sara, Scott,
        /*
        SONDA FAZLADAN BİR VİRGÜL VAR ("Scott," ile bitiyor). Döngü her
        eleman için AYNI kalıbı uyguluyor; son elemanı diğerlerinden ayırt
        etmenin kolay bir yolu yok -- döngü, "bu son eleman mı?" sorusunu
        bilmiyor.
         */

        // ------------------------------------------------------------
        // 2. deneme: indeksli (klasik) for döngüsüyle son elemanı ayır
        // ------------------------------------------------------------
        System.out.println("\n2) İndeksli for ile düzeltme:");
        for (int i = 0; i < friends.size() - 1; i++) {
            System.out.print(friends.get(i) + ", ");
        }
        if (friends.size() > 0) {
            System.out.println(friends.get(friends.size() - 1));
        }
        // Çıktı: Brian, Nate, Neal, Raju, Sara, Scott
        /*
        Çıktı artık doğru, ama bunun bedeli ağır:

        - Listeyi son elemandan BİR ÖNCESİNE kadar gezip virgüllü yazdırıp,
          SON elemanı AYRI bir if bloğuyla (virgülsüz) yazdırmak zorunda
          kaldık.
        - "friends.size() > 0" kontrolünü UNUTURSAK, boş bir listede
          friends.get(friends.size() - 1) çağrısı IndexOutOfBoundsException
          fırlatır.
        - Bu, sadece elemanları virgülle ayırmak gibi TRİVİYAL bir iş için
          hiç orantısız miktarda kod ve hataya açık indeks aritmetiği.

        Modern Java'da buna hiç gerek yok -- bkz. StringJoinDemo ve
        CollectJoiningDemo.
         */
    }
}
