package org.eloyprado.prograavanzada.config;

import org.eloyprado.prograavanzada.Repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import usuario.Producto;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductoRepository repository;

    public DataInitializer(ProductoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() == 0) {
            repository.saveAll(List.of(
                    new Producto("1", "Acer Nitro 5", 600000),
                    new Producto("2", "Polera niño", 1000),
                    new Producto("3", "Desodorante", 4000)));
            System.out.println("Se inicializaron 3 productos en MongoDB.");
        }
    }
}