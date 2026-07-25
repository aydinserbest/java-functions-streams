package org.practice.javacore.streams.limit;

import java.util.List;

public class NewsLimitDemo {
    public static void main(String[] args) {
        List<String> news = List.of(
                "Yeni Java sürümü yayınlandı",
                "Teknoloji şirketinden yeni yatırım",
                "Hava sıcaklıkları yükseliyor",
                "Takım final maçına hazırlanıyor",
                "Yeni eğitim programı başladı",
                "Şehir içi ulaşım tarifesi değişti",
                "Yeni film vizyona girdi"
        );
        news.stream()
                .limit(3)
                .forEach(System.out::println);
    }
}
