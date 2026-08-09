package org.practice.streamsandfunctionalinterfaces.functional.lambda.custominterface;
//this annotation prevents others to add or remove abstract methods
//so it can have only 1 abstract methods
@FunctionalInterface
public interface Hello {
    //it should have only one abstract method
    //all abstract methods are public by default, we do not need mention as public
    void sayHello();
}
