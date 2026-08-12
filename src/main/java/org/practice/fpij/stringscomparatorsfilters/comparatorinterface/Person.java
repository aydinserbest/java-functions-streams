package org.practice.fpij.stringscomparatorsfilters.comparatorinterface;

public class Person {

    private final String name;
    private final int age;

    public Person(final String name, final int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public int ageDifference(final Person other) {
        return age - other.age;
    }
    /*
    Bu metot bilinçli olarak Comparator'ın compareTo sözleşmesine UYACAK
    şekilde yazıldı: negatif -> bu kişi daha genç, sıfır -> yaşıt, pozitif
    -> bu kişi daha yaşlı. Bu sayede ageDifference, doğrudan bir
    Comparator<Person> olarak (method reference ile) kullanılabilir hale
    geliyor -- bunu SortByAgeAscending'de göreceğiz.
     */

    @Override
    public String toString() {
        return String.format("%s - %d", name, age);
    }
}
