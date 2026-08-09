package org.practice.streamsandfunctionalinterfaces.functional.lambda.custominterface;

public class LambdaExpressionDemo {
    public static void main(String[] args) {
        // build a Lambda expression using the Hello interface

        //1th approach:
        /*
        Behind the scenes
based upon this lambda expression, my compiler is going to have all kinds of information like what
is the method name,
what is the return type of it,
 what is the input parameter types

 So whenever you want to write a lambda expression,
 you need to have a supporting Functional interface
which has a single abstract method.
So based upon that single abstract method, the compiler is not going to expect you
to provide the method name.
What are the return type,
what are the input parameter data type
         */
        Hello hello = () -> System.out.println("Hello, World!");
        //we're sending our business logic as an input parameter
        //with lambda expression we can pass the behaviour as an input
        process(hello);
        Hello hello2 = () -> System.out.println("Hello, Moon!");
        process(hello2);

        //2nd approach:

        process(() -> System.out.println("Hello, Amsterdam!"));

        //with other interface

        ArithmeticOperation addition     = (a, b) -> a + b;
        System.out.println(addition.operation(1, 2));

        ArithmeticOperation subtraction = (a, b) -> a - b;
        System.out.println(subtraction.operation(5, 3));

        ArithmeticOperation multiplication = (a, b) -> a * b;
        System.out.println(multiplication.operation(2, 4));
    }
    public static void process(Hello hello) {
        hello.sayHello();
    }
}
