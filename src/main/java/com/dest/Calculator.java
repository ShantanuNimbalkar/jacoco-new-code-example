package com.dest;

public class Calculator {
    public int multiply(int a, int b) {
        return a * b;
    }
    public int add(int a, int b) {
        return a + b;
    }

    public int divide(int a, int b) {
        if (b == 0) throw new IllegalArgumentException("Division by zero");
        return a / b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    public int squarePublic(int a) {
        return square(a);
    }

    private int square(int a) {
        int c = a*a;
        return a * a;
    }


    public int subtractwith(int a, int b) {
        // System.out.println("Hello");
        return a - b;
    }
}

