package org.practice.javacore.functional.predicate.exercises.userfilter;

import java.util.List;
import java.util.function.Predicate;

public class UserDemo {
    static void main() {
        List<User> users = List.of(
                new User("Alice", true),
                new User("Bob", false),
                new User("Charlie", false),
                new User("Dave", false),
                new User("Eve", false)
        );

        // Business kuralı: Yalnızca aktif durumdaki kullanıcıları kabul et.
        Predicate<User> isActiveUser = user -> user.isActive();

        List<User> activeUsers = getActiveUsers(users, isActiveUser);

        // Listenin tamamını tek satırda yazdırır.
        System.out.println(activeUsers);

        // Alternatif: Her kullanıcıyı ayrı satırda yazdırır.
        activeUsers.forEach(System.out::println);
    }

    static List<User> getActiveUsers(
            List<User> users,
            Predicate<User> predicate
    ) {
        // toList() filtreleme sonucunda yeni bir liste üretir; bu listeyi döndürmeliyiz.
        return users.stream()
                .filter(predicate)
                .toList();
    }
}
