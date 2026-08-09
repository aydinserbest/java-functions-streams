package org.practice.streamsandfunctionalinterfaces.streams.flatMap.personExample;

import java.util.List;

public class Person {
    String name;
    List<String> phoneNumbers;
    public Person(String name, List<String> phoneNumbers) {
        this.name = name;
        this.phoneNumbers = phoneNumbers;
    }

    public String getName() {
        return name;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }
}
