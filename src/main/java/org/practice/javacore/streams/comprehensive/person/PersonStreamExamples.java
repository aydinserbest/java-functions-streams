package org.practice.javacore.streams.comprehensive.person;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PersonStreamExamples {

    public static void main(String[] args) {
        List<Person> people = createPeople();

        filterActiveAmsterdamEmployees(people);
        mapPeopleToDirectoryLabels(people);
        flatMapAllSkills(people);
        sortEmployeesBySalary(people);
        createSecondDirectoryPage(people);
        countActiveEmployees(people);
        calculateSalaryValues(people);
        collectPeopleIntoReports(people);
        createActiveEmployeeMessage(people);
        findYoungestAndHighestPaidEmployee(people);
    }

    private static List<Person> createPeople() {
        return List.of(
                new Person(1, "Alice", 30, "Amsterdam", true, 5200,
                        List.of("Java", "SQL", "Docker")),
                new Person(2, "Mehmet", 35, "Rotterdam", true, 5800,
                        List.of("Java", "Spring", "AWS")),
                new Person(3, "John", 22, "Amsterdam", false, 3200,
                        List.of("JavaScript", "React")),
                new Person(4, "Eva", 28, "Utrecht", true, 4600,
                        List.of("UX", "Figma")),
                new Person(5, "David", 42, "Amsterdam", true, 6500,
                        List.of("Leadership", "Java", "Kubernetes")),
                new Person(6, "Sophie", 26, "Rotterdam", false, 3900,
                        List.of("Python", "SQL")),
                new Person(7, "Bob", 31, "Amsterdam", true, 4800,
                        List.of("Java", "Angular")),
                new Person(8, "Mila", 24, "Eindhoven", true, 4100,
                        List.of("C#", "Azure", "SQL"))
        );
    }

    private static void filterActiveAmsterdamEmployees(List<Person> people) {
        /*
         * Business ihtiyacı:
         * Amsterdam ofisindeki aktif çalışanlara yüz yüze eğitim verilecek.
         *
         * Predicate<Person>, her kişi için "aktif ve Amsterdam'da mı?" kararını
         * taşır. filter() intermediate, toList() terminal operation'dır.
         */
        Predicate<Person> isActiveInAmsterdam = person ->
                person.isActive() && person.getCity().equals("Amsterdam");

        List<Person> participants = people.stream()
                .filter(isActiveInAmsterdam)
                .toList();

        System.out.println("\n1. Active Amsterdam employees:");

        /*
         * forEach() terminal operation'dır ve listedeki her kişi için
         * Consumer<Person> davranışını çalıştırır.
         */
        participants.forEach(System.out::println);
    }

    private static void mapPeopleToDirectoryLabels(List<Person> people) {
        /*
         * Business ihtiyacı:
         * Şirket rehberi tam Person nesnesi yerine "isim - şehir" etiketi ister.
         *
         * map() her Person'ı yeni bir String değerine dönüştürür:
         * Stream<Person> -> Stream<String>
         */
        List<String> directoryLabels = people.stream()
                .map(person -> person.getName() + " - " + person.getCity())
                .toList();

        System.out.println("\n2. Directory labels: " + directoryLabels);
    }

    private static void flatMapAllSkills(List<Person> people) {
        /*
         * Business ihtiyacı:
         * Eğitim ekibi çalışan kartlarındaki ayrı yetenek listelerinden şirketin
         * benzersiz yetenek envanterini oluşturmak istiyor.
         *
         * map(Person::getSkills) kullanılsaydı Stream<List<String>> oluşurdu.
         * flatMap(), iç Stream'leri tek Stream<String> içinde birleştirir.
         */
        Set<String> skillInventory = people.stream()
                .flatMap(person -> person.getSkills().stream())
                .collect(Collectors.toSet());

        System.out.println("\n3. Skill inventory: " + skillInventory);
    }

    private static void sortEmployeesBySalary(List<Person> people) {
        /*
         * Business ihtiyacı:
         * Bütçe raporunda yüksek maaşlı çalışanlar önce gösterilecek.
         * Maaş aynıysa kararlı ve okunabilir sonuç için isim kullanılır.
         */
        Comparator<Person> salaryDescendingThenName =
                Comparator.comparingDouble(Person::getMonthlySalary)
                        .reversed()
                        .thenComparing(Person::getName);

        List<Person> payrollOrder = people.stream()
                .sorted(salaryDescendingThenName)
                .toList();

        System.out.println("\n4. Payroll order:");
        payrollOrder.forEach(person -> System.out.println(
                person.getName() + " - €" + person.getMonthlySalary()));
    }

    private static void createSecondDirectoryPage(List<Person> people) {
        /*
         * Business ihtiyacı:
         * Çalışan rehberinin sayfa boyutu 3'tür. İkinci sayfa için önce bütün
         * kişiler isme göre sıralanır, ilk sayfanın 3 kaydı atlanır ve sonraki
         * 3 kayıt alınır.
         *
         * sorted(), skip() ve limit() intermediate operation'lardır.
         */
        List<Person> secondPage = people.stream()
                .sorted(Comparator.comparing(Person::getName))
                .skip(3)
                .limit(3)
                .toList();

        System.out.println("\n5. Second directory page: " + secondPage);
    }

    private static void countActiveEmployees(List<Person> people) {
        /*
         * Business ihtiyacı:
         * Yönetim panelindeki KPI yalnızca aktif çalışanların sayısını gösterir.
         *
         * count() terminal operation'dır ve long döndürür.
         */
        long activeEmployeeCount = people.stream()
                .filter(Person::isActive)
                .count();

        System.out.println("\n6. Active employee count: " + activeEmployeeCount);
    }

    private static void calculateSalaryValues(List<Person> people) {
        /*
         * Business ihtiyacı:
         * Finans, aktif çalışanların aylık toplam ve ortalama maaşını ister.
         *
         * mapToDouble(), Person nesnelerini primitive double maaş akışına
         * dönüştürür. summaryStatistics() tek geçişte count, sum, min, max ve
         * average üretir.
         */
        DoubleSummaryStatistics salaryStatistics = people.stream()
                .filter(Person::isActive)
                .mapToDouble(Person::getMonthlySalary)
                .summaryStatistics();

        /*
         * Aynı toplamın reduce() ile karşılığı:
         * 0.0 toplamanın identity değeridir.
         */
        double salaryTotalWithReduce = people.stream()
                .filter(Person::isActive)
                .map(Person::getMonthlySalary)
                .reduce(0.0, Double::sum);

        System.out.println("\n7. Active salary total: "
                + salaryStatistics.getSum());
        System.out.println("Active salary average: "
                + salaryStatistics.getAverage());
        System.out.println("Salary total with reduce: "
                + salaryTotalWithReduce);
    }

    private static void collectPeopleIntoReports(List<Person> people) {
        /*
         * Business ihtiyacı 1:
         * Organizasyon ekranında çalışanları şehir başlıkları altında göster.
         *
         * groupingBy() sonucu:
         * Map<String, List<Person>>
         */
        Map<String, List<Person>> peopleByCity = people.stream()
                .collect(Collectors.groupingBy(Person::getCity));

        /*
         * Business ihtiyacı 2:
         * Yönetim panelinde şehir başına aktif çalışan adedini göster.
         *
         * groupingBy() ana grubu, filtering() grup içindeki koşulu,
         * counting() ise her grubun sonucunu belirler.
         */
        Map<String, Long> activeCountByCity = people.stream()
                .collect(Collectors.groupingBy(
                        Person::getCity,
                        Collectors.filtering(
                                Person::isActive,
                                Collectors.counting()
                        )
                ));

        /*
         * Business ihtiyacı 3:
         * Çalışanları 30 yaş ve üzeri / 30 yaş altı olarak iki rapora ayır.
         */
        Map<Boolean, List<Person>> ageGroups = people.stream()
                .collect(Collectors.partitioningBy(
                        person -> person.getAge() >= 30));

        System.out.println("\n8. People by city: " + peopleByCity);
        System.out.println("Active count by city: " + activeCountByCity);
        System.out.println("Age groups: " + ageGroups);
    }

    private static void createActiveEmployeeMessage(List<Person> people) {
        /*
         * Business ihtiyacı:
         * İK duyurusunda aktif çalışan adları tek bir rapor cümlesi olsun.
         *
         * joining() isimleri toplar.
         * collectingAndThen() toplama bittikten sonra metnin başına rapor
         * başlığını ekler.
         */
        String activeEmployeeMessage = people.stream()
                .filter(Person::isActive)
                .map(Person::getName)
                .collect(Collectors.collectingAndThen(
                        Collectors.joining(", "),
                        names -> "Active employees: " + names
                ));

        System.out.println("\n9. " + activeEmployeeMessage);
    }

    private static void findYoungestAndHighestPaidEmployee(List<Person> people) {
        Comparator<Person> byAge =
                Comparator.comparingInt(Person::getAge);
        Comparator<Person> bySalary =
                Comparator.comparingDouble(Person::getMonthlySalary);

        /*
         * Business ihtiyacı:
         * İK özeti en genç ve en yüksek maaşlı aktif çalışanı gösterecek.
         *
         * min() ve max() Stream'in terminal operation'larıdır.
         * Boş sonuç ihtimali nedeniyle Optional<Person> döndürürler.
         */
        Optional<Person> youngest = people.stream()
                .filter(Person::isActive)
                .min(byAge);

        Optional<Person> highestPaid = people.stream()
                .filter(Person::isActive)
                .max(bySalary);

        /*
         * Collector tabanlı maxBy() aynı seçimi collect() içinde yapar.
         * Tek maksimum için max() daha sade; groupingBy() gibi Collector
         * bileşimlerinde maxBy() daha kullanışlıdır.
         */
        Optional<Person> highestPaidWithMaxBy = people.stream()
                .filter(Person::isActive)
                .collect(Collectors.maxBy(bySalary));

        System.out.println("\n10. Youngest active employee: "
                + youngest.map(Person::getName).orElse("None"));
        System.out.println("Highest paid active employee: "
                + highestPaid.map(Person::getName).orElse("None"));
        System.out.println("Highest paid with maxBy: "
                + highestPaidWithMaxBy.map(Person::getName).orElse("None"));
    }
}
