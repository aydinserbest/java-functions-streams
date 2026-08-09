package org.practice.streamsandfunctions.streams.comprehensive.person;

import java.util.List;

/*
 * Person POJO'su çalışan rehberi ve insan kaynakları raporlarını temsil eder.
 *
 * skills alanının List<String> olması flatMap() ile iç listeleri tek yetenek
 * akışında birleştirmemize imkân verir.
 */
public class Person {

    private final long id;
    private final String name;
    private final int age;
    private final String city;
    private final boolean active;
    private final double monthlySalary;
    private final List<String> skills;

    public Person(
            long id,
            String name,
            int age,
            String city,
            boolean active,
            double monthlySalary,
            List<String> skills
    ) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.city = city;
        this.active = active;
        this.monthlySalary = monthlySalary;
        this.skills = List.copyOf(skills);
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }

    public boolean isActive() {
        return active;
    }

    public double getMonthlySalary() {
        return monthlySalary;
    }

    public List<String> getSkills() {
        return skills;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", city='" + city + '\'' +
                ", active=" + active +
                ", monthlySalary=" + monthlySalary +
                ", skills=" + skills +
                '}';
    }
}
