package org.eloyprado.prograavanzada;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class PrograAvanzadaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrograAvanzadaApplication.class, args);
    }

    @RestController
    static class HolaMundoController {
        @GetMapping("/")
        public String holaMundo() {
            return "Hola Mundo";
        }
    }
}
