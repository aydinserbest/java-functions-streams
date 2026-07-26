package org.practice.javacore.streams.collectingAndThen;

import java.util.List;
import java.util.stream.Collectors;

public class SimpleCollectingAndThenDemo {

    public static void main(String[] args) {
        List<String> customerNames = List.of("Alice", "Mehmet", "John");

        /*
         * Business senaryosu:
         *
         * Kampanya e-postası gönderilecek müşterilerin isimlerini önce
         * virgülle ayrılmış tek bir metinde toplamak istiyoruz:
         *
         * Alice, Mehmet, John
         *
         * Ardından bu metni kullanıcıya gösterilecek bir rapor cümlesine
         * dönüştürmek istiyoruz:
         *
         * Kampanya alıcıları: Alice, Mehmet, John
         */

        /*
         * Önce aynı işlemi iki ayrı adımda yapalım.
         */
        String joinedNames = customerNames.stream()
                .collect(Collectors.joining(", "));

        String campaignReport = "Kampanya alıcıları: " + joinedNames;

        System.out.println(campaignReport);

        /*
         * collectingAndThen() yukarıdaki iki adımı tek collect işleminin
         * içinde birleştirir:
         *
         * 1. Collectors.joining(", ")
         *    İsimleri virgülle ayrılmış tek bir String içinde toplar.
         *
         *    Alice, Mehmet, John
         *
         * 2. joinedResult -> "Kampanya alıcıları: " + joinedResult
         *    Toplama işlemi tamamlandıktan sonra sonuca son bir işlem uygular.
         *
         *    Kampanya alıcıları: Alice, Mehmet, John
         */
        String reportWithCollectingAndThen = customerNames.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.joining(", "),
                        joinedResult -> "Kampanya alıcıları: " + joinedResult
                ));

        System.out.println(reportWithCollectingAndThen);

        /*
         * Kısa formül:
         *
         * collectingAndThen(
         *     nasıl toplanacak,
         *     toplandıktan sonra ne yapılacak
         * )
         *
         * Bu örnekte:
         *
         * Stream<String>
         *      ↓ joining(", ")
         * String: "Alice, Mehmet, John"
         *      ↓ son işlem
         * String: "Kampanya alıcıları: Alice, Mehmet, John"
         *
         * collect() terminal operation'dır.
         * collectingAndThen() ise collect() metoduna verilen Collector'ı
         * oluşturur.
         */
    }
}
