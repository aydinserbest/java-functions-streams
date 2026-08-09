package org.practice.fpij.usingcollections.streamcontinuebreak.exercises.helperClasses;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

public class LocalDateBasics {
    public static void main(String[] args) {
        /*
        Bu class, alıştırmalarda (Q01Pagination, Q02TrialInvoices, Q04...)
        sürekli karşımıza çıkacak olan LocalDate ve ilgili tarih işlemlerini
        sorulardan bağımsız, sade örneklerle gösteriyor.

        Önemli genel kural: LocalDate (tıpkı String gibi) IMMUTABLE'dır
        (değiştirilemez). "plusDays", "minusDays" gibi metotlar orijinal
        nesneyi DEĞİŞTİRMEZ, her seferinde YENİ bir LocalDate döndürür.
         */

        // ------------------------------------------------------------
        // 1) LocalDate.now(): bugünün tarihi
        // ------------------------------------------------------------
        LocalDate today = LocalDate.now();
        System.out.println("1) Bugün: " + today);
        /*
        LocalDate.now() sistem saatinden bugünün tarihini (yıl-ay-gün)
        okur. Saat/dakika/saniye bilgisi YOKTUR — sadece tarih. Saat bilgisi
        de gerekiyorsa LocalDateTime kullanılır, bu class'ın konusu değil.
         */

        // ------------------------------------------------------------
        // 2) plusDays / minusDays / plusMonths / plusYears: IMMUTABLE dönüşüm
        // ------------------------------------------------------------
        LocalDate in10Days = today.plusDays(10);
        LocalDate tenDaysAgo = today.minusDays(10);
        LocalDate nextMonth = today.plusMonths(1);
        LocalDate lastYear = today.minusYears(1);

        System.out.println("\n2) plus/minus örnekleri:");
        System.out.println("10 gün sonra : " + in10Days);
        System.out.println("10 gün önce  : " + tenDaysAgo);
        System.out.println("1 ay sonra   : " + nextMonth);
        System.out.println("1 yıl önce   : " + lastYear);
        System.out.println("today hâlâ aynı mı? " + today);
        /*
        Dikkat: today.plusDays(10) çağrısından SONRA bile "today" değişkeni
        hâlâ bugünün tarihini gösteriyor — çünkü plusDays orijinali
        değiştirmedi, YENİ bir LocalDate nesnesi ÜRETİP döndürdü. Bu yüzden
        dönen değeri MUTLAKA bir değişkene atamak (ya da zincirlemeye devam
        etmek) gerekir; sadece "today.plusDays(10);" yazıp sonucu
        kullanmazsanız o hesaplama boşa gider.
         */

        // ------------------------------------------------------------
        // 3) isBefore / isAfter / isEqual: iki tarihi karşılaştırma
        // ------------------------------------------------------------
        System.out.println("\n3) Karşılaştırma:");
        System.out.println("today, in10Days'ten önce mi? " + today.isBefore(in10Days));
        System.out.println("today, tenDaysAgo'dan sonra mı? " + today.isAfter(tenDaysAgo));
        System.out.println("today, today ile eşit mi? " + today.isEqual(LocalDate.now()));
        /*
        Bu üç metot, tam olarak dropWhile()/takeWhile() gibi yerlerde
        predicate yazarken işimize yarayacak: örneğin
            record -> record.getDate().isBefore(sinirTarih)
        gibi bir lambda, "sınır tarihinden önceki kayıtları" ifade eder.
         */

        // ------------------------------------------------------------
        // 4) Comparator.comparing(...): LocalDate alanına göre sıralama
        // ------------------------------------------------------------
        record Event(String name, LocalDate date) {}
        List<Event> events = List.of(
                new Event("Lansman", today.plusDays(5)),
                new Event("Kayıt Başlangıcı", today.minusDays(3)),
                new Event("Kapanış", today.plusDays(1))
        );

        System.out.println("\n4) Tarihe göre artan sıralama:");
        events.stream()
                .sorted(Comparator.comparing(Event::date))
                .forEach(e -> System.out.println(e.name() + " -> " + e.date()));

        System.out.println("\n   Tarihe göre azalan (en yeni önce) sıralama:");
        events.stream()
                .sorted(Comparator.comparing(Event::date).reversed())
                .forEach(e -> System.out.println(e.name() + " -> " + e.date()));
        /*
        Comparator.comparing(Event::date) -> Event nesnelerini "date" alanına
        bakarak KÜÇÜKTEN BÜYÜĞE (eskiden yeniye) sıralar. .reversed() eklenirse
        sıralama tersine döner (yeniden eskiye). Bunu Q01Pagination'da da
        birebir bu şekilde kullandık.
         */

        // ------------------------------------------------------------
        // 5) İki tarih arasındaki farkı hesaplama: ChronoUnit / Period
        // ------------------------------------------------------------
        long daysBetween = ChronoUnit.DAYS.between(tenDaysAgo, today);
        Period period = Period.between(lastYear, today);

        System.out.println("\n5) Fark hesaplama:");
        System.out.println("tenDaysAgo ile today arasında kaç gün var? " + daysBetween);
        System.out.println("lastYear ile today arasındaki tam fark: "
                + period.getYears() + " yıl, "
                + period.getMonths() + " ay, "
                + period.getDays() + " gün");
        /*
        ChronoUnit.DAYS.between(a, b) -> sadece GÜN cinsinden ham bir sayı
        verir (örn. Q02TrialInvoices'te "deneme süresi kaç gün sürdü" gibi
        bir hesap için kullanışlı).
        Period.between(a, b) -> farkı "X yıl Y ay Z gün" şeklinde, insanın
        okuyacağı formatta parçalara ayırır.
         */

        // ------------------------------------------------------------
        // 6) Ekstra bilgiler: haftanın günü, ayın uzunluğu, artık yıl mı
        // ------------------------------------------------------------
        System.out.println("\n6) Ekstra bilgiler:");
        System.out.println("Bugün haftanın hangi günü? " + today.getDayOfWeek());
        System.out.println("Bu ay kaç gün çekiyor? " + today.lengthOfMonth());
        System.out.println("Bu yıl artık yıl mı? " + today.isLeapYear());

        // ------------------------------------------------------------
        // 7) Ekrana/rapora yazarken tarihi biçimlendirme
        // ------------------------------------------------------------
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        System.out.println("\n7) Biçimlendirilmiş tarih: " + today.format(formatter));
        /*
        LocalDate'in varsayılan toString()'i ISO formatındadır (yyyy-MM-dd,
        örn. 2026-08-09). Kullanıcıya gösterirken genelde farklı bir format
        istenir (örn. gün.ay.yıl); bunun için DateTimeFormatter kullanılır.
         */
    }
}
