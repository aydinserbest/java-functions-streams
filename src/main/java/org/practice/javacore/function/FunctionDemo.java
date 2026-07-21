package org.practice.javacore.function;

import java.util.function.Function;

public class FunctionDemo {
    public static void main(String[] args) {
        Function<String, String> convertStr =  String::toUpperCase;
        Function<String, String> convertStr2 = str -> str.toUpperCase();
        System.out.println(convertStr.apply("John"));
        System.out.println(convertStr2.apply("Madame"));
        String helloWorld = convertStr.apply("hello world");
        System.out.println(convertStr.apply("admin")); //ADMIN

        Function<String, Integer> convertStr3 = String::length;
        Integer apply = convertStr3.apply(helloWorld);
        System.out.println(apply);

        Function<Integer, Integer> square = x -> x * x;
        System.out.println(square.apply(5));
        Function<Integer, Integer> doubleValue = x -> x * 2;
        System.out.println(doubleValue.apply(5));
        Function<Integer, Integer> addValue = x -> x + x;
        System.out.println(addValue.apply(5));

        Function<Integer, Integer> integerIntegerFunction = doubleValue.andThen(addValue);
        System.out.println(integerIntegerFunction.apply(5));

        Function<Integer, Integer> composedFunction = doubleValue.compose(addValue);
        System.out.println(composedFunction.apply(5));
    }
}
