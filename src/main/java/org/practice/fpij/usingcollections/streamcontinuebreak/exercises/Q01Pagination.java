package org.practice.fpij.usingcollections.streamcontinuebreak.exercises;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class Q01Pagination {
    public static void main(String[] args) {
        /*
        Soru 1 - Sayfalama (Pagination)

        Bir e-ticaret sitesinde ürün listesi tarih sırasına göre geliyor.
        Kullanıcı 3. sayfayı istedi ve her sayfada 10 ürün gösteriliyor.
        Elinizde tüm ürünlerin tam listesi var; kullanıcıya sadece 3.
        sayfaya denk gelen 10 ürünü göstermeniz gerekiyor (yani ilk 20
        ürünü atlayıp ondan sonraki 10 tanesini almalısınız).

        TODO: Çözümü burada uygula.

         */
        /*
         record'da alanları header'da yazdığın anda derleyici otomatik olarak:

  - canonical constructor'ı (Product(String name, LocalDate addedDate)),
  - accessor'ları (name(), addedDate() — dikkat: getName() değil, sadece name()),
  - equals(), hashCode(), toString()

  kendisi üretiyor. Sen sadece record Product(String name, LocalDate addedDate) {} yazman yeterli, ekstra constructor yazmana gerek yok.
         */
        Random random = new Random();
        record Product(String productName, LocalDate addDate) {}
        String[] productNames = {"Laptop", "TV", "USB", "Phone", "Tablet", "Monitor", "Keyboard", "Mouse", "Headphone", "Charger", "Camera", "Printer", "Speaker", "Router", "Smartwatch", "Projector", "Scanner", "Microphone", "Webcam", "Game Console"};

        // "Elimizdeki veri": 40 ürünlük, tarihe göre henüz sıralanmamış liste.
        List<Product> products = IntStream.range(0, 40)
                .mapToObj(i -> new Product(
                        productNames[random.nextInt(productNames.length)],
                        LocalDate.now().plusDays(random.nextInt(60))))
                .toList();

        // 3. sayfa, sayfa başı 10 ürün -> ilk 20 ürünü atla, sonraki 10 tanesini al.
        products.stream()
                .sorted(Comparator.comparing(Product::addDate))
                .skip(20)
                .limit(10)
                .forEach(p -> System.out.println(p.productName() + " - " + p.addDate()));
    }
}
