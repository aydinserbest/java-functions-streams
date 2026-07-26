package org.practice.javacore;

import java.util.Arrays;
import java.util.List;

public class DiscountImperative {
    public static void main(String[] args) {
        List<Integer> prices = Arrays.asList(10, 30, 17, 20, 12, 45, 18);

        double totalOfDiscountedPrices = 0.0;
        for (int price : prices) {
            if (price > 20) {
                totalOfDiscountedPrices += price * 0.9;
            }
        }
        System.out.println("Total of discounted prices: " + totalOfDiscountedPrices);
    }
}
