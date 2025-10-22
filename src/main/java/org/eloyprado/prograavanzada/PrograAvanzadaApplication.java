package org.eloyprado.prograavanzada;

import java.util.List;

import org.eloyprado.prograavanzada.Repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import usuario.Producto;

@SpringBootApplication
public class PrograAvanzadaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrograAvanzadaApplication.class, args);
    }
@Bean
    public CommandLineRunner initialData(ProductoRepository repository) {
        return (args) -> {
            // Solo inserta si no hay productos
            if (repository.count() == 0) {
                repository.saveAll(List.of(
                        new Producto("1", "Acer Nitro 5", 600000),
                        new Producto("2", "Polera niño", 1000),
                        new Producto("3", "Desodorante", 4000)
                ));
                System.out.println("Se inicializaron 3 productos en MongoDB.");
            }
        };
    }
    @RestController
    static class HolaMundoController {
        @GetMapping("/holaMundo")
        public String holaMundo() {
            return "Hola Mundo";
        }
    }
}