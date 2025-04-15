package com.example;

import org.junit.jupiter.api.Test;

class LoggerTest {
    @Test
    void testLog() {
        Logger logger = new Logger();
        logger.log("Testing logger output");  // Just testing that logger outputs to the console
    }
}
