package org.practice.javacore;

import java.util.Arrays;
import java.util.List;

public class Cities {
    public static void main(String[] args) {
        List<String> cities = Arrays.asList("Albany", "Boulder", "Chicago", "Denver", "Eugene");
        findChicagoDeclarative(cities);
        findChicagoImperative(cities);
        /*
        System.out.println("Found chicago?:" + cities.contains("Chicago"));
        boolean chicago = cities.contains("Chicago");
        System.out.println("Found chicago?:" + chicago);
         */
    }

    private static void findChicagoImperative(List<String> cities) {
        System.out.println("Found chicago?:" + cities.contains("Chicago"));
    }

    private static void findChicagoDeclarative(List<String> cities) {
        boolean found = false;
        for (String city : cities) {
            if (city.equals("Chicago")) {
                found = true;
                break;
            }
        }
        System.out.println("Found chicago?:" + found);
    }
}
