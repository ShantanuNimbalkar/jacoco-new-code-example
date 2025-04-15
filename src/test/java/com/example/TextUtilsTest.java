package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TextUtilsTest {
    @Test
    void testReverse() {
        TextUtils utils = new TextUtils();
        assertEquals("olleh", utils.reverse("hello"));
    }
}
