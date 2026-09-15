package com.fazil.learn_spring.learnspring_jpa.test;

public class Calculator {

    public int add(int x, int y) {
        return x + y;
    }

    public int divide(int a, int b) {

        if (b == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }

        return a / b;
    }
}
