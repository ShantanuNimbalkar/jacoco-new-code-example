package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculatorTest {

    @Test
    void testAdd() {
        Calculator calc = new Calculator();
        assertEquals(5, calc.add(2, 3));
    }

    @Test
    void testSubtract() {
        Calculator calc = new Calculator();
        assertEquals(1, calc.subtract(3, 2));
    }

    @Test
    void testDivide() {
        Calculator calc = new Calculator();
        assertEquals(3, calc.divide(9, 3));  // Divide two numbers
        assertThrows(IllegalArgumentException.class, () -> calc.divide(9, 0));  // Test divide by zero
    }

    @Test
void testSquarePublic() {
    Calculator calc = new Calculator();
    assertEquals(16, calc.squarePublic(4));  // Square method tested via squarePublic
}
}

