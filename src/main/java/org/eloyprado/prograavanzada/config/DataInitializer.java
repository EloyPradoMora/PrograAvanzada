package org.eloyprado.prograavanzada.config;

import org.eloyprado.prograavanzada.Repository.ProductoRepository;
import org.eloyprado.prograavanzada.Repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import usuario.Producto;
import usuario.Usuario;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(ProductoRepository repository, UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
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

        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole("ROLE_ADMIN");
            usuarioRepository.save(admin);
            System.out.println("Usuario admin creado por defecto (admin/admin)");
        }
    }
}