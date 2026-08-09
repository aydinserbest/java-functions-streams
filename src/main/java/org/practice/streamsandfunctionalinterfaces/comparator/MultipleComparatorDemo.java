package org.practice.streamsandfunctionalinterfaces.comparator;

import java.util.Comparator;
import java.util.List;

public class MultipleComparatorDemo {

    public static void main(String[] args) {
        List<Employee> employees = List.of(
                new Employee("Alice", "Engineering", 5000),
                new Employee("John", "Sales", 4200),
                new Employee("Mehmet", "Engineering", 5500),
                new Employee("Bob", "Sales", 4200)
        );

        /*
         * Aynı Employee sınıfı için ihtiyacımız kadar Comparator yazabiliriz.
         * Comparator kuralları Employee sınıfının dışında tutulduğu için her
         * rapor kendi business sırasını seçebilir.
         */
        Comparator<Employee> byName =
                Comparator.comparing(Employee::name);

        Comparator<Employee> bySalaryDescending =
                Comparator.comparingInt(Employee::salary).reversed();

        Comparator<Employee> byDepartmentThenName =
                Comparator.comparing(Employee::department)
                        .thenComparing(Employee::name);

        /*
         * Business senaryosu 1:
         * Şirket rehberinde çalışanları alfabetik göster.
         */
        System.out.println("Directory by name:");
        employees.stream()
                .sorted(byName)
                .forEach(System.out::println);

        /*
         * Business senaryosu 2:
         * Bütçe raporunda en yüksek maaşı önce göster.
         */
        System.out.println("Payroll by salary descending:");
        employees.stream()
                .sorted(bySalaryDescending)
                .forEach(System.out::println);

        /*
         * Business senaryosu 3:
         * Organizasyon ekranında önce departmana göre sırala.
         * İki çalışanın departmanı aynıysa ikinci kural olan isme göre sırala.
         *
         * thenComparing(), ilk Comparator eşitlik (0) ürettiğinde devreye girer.
         */
        System.out.println("Organization by department, then name:");
        employees.stream()
                .sorted(byDepartmentThenName)
                .forEach(System.out::println);
    }

    record Employee(String name, String department, int salary) {
    }
}
