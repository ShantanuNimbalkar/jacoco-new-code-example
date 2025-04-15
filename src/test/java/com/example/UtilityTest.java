package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UtilityTest {
    @Test
    void testIsEven() {
        Utility util = new Utility();
        assertTrue(util.isEven(6));  // Even number
        assertFalse(util.isEven(7)); // Odd number
    }
}
