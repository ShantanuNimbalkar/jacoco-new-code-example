package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {
    @Test
    void testReverse() {
        StringUtils utils = new StringUtils();
        assertEquals("olleh", utils.reverse("hello"));
    }
}
