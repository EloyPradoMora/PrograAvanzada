package org.eloyprado.prograavanzada;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class LoggingTest {

    private static final Logger logger = LoggerFactory.getLogger(LoggingTest.class);

    @Test
    void testLogGeneration() {
        // Log a unique message
        String uniqueMessage = "Test Log Message " + System.currentTimeMillis();
        logger.info(uniqueMessage);
        logger.error("Error Log Verification: " + uniqueMessage);

        // Verify file exists
        File logFile = new File("logs/application.log");
        assertTrue(logFile.exists(), "Log file should exist at logs/application.log");

        // Verify content contains message
        try {
            // Read all lines (might be large, but sufficient for test)
            List<String> lines = Files.readAllLines(logFile.toPath(), java.nio.charset.StandardCharsets.ISO_8859_1);
            boolean found = lines.stream().anyMatch(line -> line.contains(uniqueMessage));
            assertTrue(found, "Log file should contain the logged message: " + uniqueMessage);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read log file", e);
        }
    }
}
