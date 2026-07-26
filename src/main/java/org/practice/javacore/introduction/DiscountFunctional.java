package org.practice.javacore;

import java.util.List;

public class DiscountFunctional {
    public static void main(String[] args) {
        List<Integer> prices = List.of(10, 30, 17, 20, 12, 45, 18);
        double totalOfDiscountedPrices = prices.stream()
                .filter(price -> price > 20)
                .mapToDouble(price -> price * 0.9)
                .sum();
        System.out.println("Total of discounted prices: " + totalOfDiscountedPrices);
    }
}
