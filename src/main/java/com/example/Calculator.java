package com.example;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Calculator {
    public int multiply(int a, int b) {
		System.out.println("hello");
        return a * b;
    }
    public int add(int a, int b) {
        return a + b;
    }

    public void process1() {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("out.txt", true)));
			out.println("the text");
			out.close(); // close() is in try clause
		} catch (IOException e) {
			logger.error("resource is closed in try block", e);
		}
	}

    public void process2() {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("out.txt", true)));
			out.println("the text");
		} catch (IOException e) {
			logger.error("Resource is not closed anywhere.", e);
		}
	}

    public int divide(int a, int b) {
        if (b == 0) throw new IllegalArgumentException("Division by zero");
        return a / b;
    }

    public int subtract(int a, int b) {
        System.out.println("Subtract");
        return a - b;
    }

    public int squarePublic(int a) {
        return square(a);
    }

    private int square(int a) {
        return a * a;
    }

	 public int subtract23(int a, int b) {
        System.out.println("Subtract");
        return a - b;
    }

	 public int subtract34(int a, int b) {
        System.out.println("Subtract");
        return a - b;
    }

	 public int subtract56(int a, int b) {
        System.out.println("Subtract");
        return a - b;
    }
}

