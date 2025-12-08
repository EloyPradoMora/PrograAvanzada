package org.eloyprado.prograavanzada;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class PrograAvanzadaApplication {

    private static final Logger logger = LoggerFactory.getLogger(PrograAvanzadaApplication.class);

    public static void main(String[] args) {
        logger.info("Application Starting...");
        SpringApplication.run(PrograAvanzadaApplication.class, args);
        logger.info("Application Started");
    }
}