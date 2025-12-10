package org.eloyprado.prograavanzada.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eloyprado.prograavanzada.Repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import usuario.Usuario;

import java.util.List;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registrarUsuario(String username, String password, String passwordAgain) throws Exception {
        logger.info("Attempting to register user: {}", username);
        if (username == null || username.trim().isEmpty()) {
            logger.error("Registration failed: Username is empty");
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío.");
        }
        if (!password.equals(passwordAgain)) {
            logger.error("Registration failed for user {}: Passwords do not match", username);
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }
        if (usuarioRepository.findByUsername(username).isPresent()) {
            logger.warn("Registration failed: Username {} already exists", username);
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }

        Usuario newUser = new Usuario();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        usuarioRepository.save(newUser);
        logger.info("User registered successfully: {}", username);
    }

    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario obtenerUsuarioPorNombre(String username) {
        return usuarioRepository.findByUsername(username).orElse(null);
    }

    public void updatePrestige(String username, int amount) {
        Usuario usuario = usuarioRepository.findByUsername(username).orElse(null);
        if (usuario != null) {
            usuario.setPrestigio(usuario.getPrestigio() + amount);
            usuarioRepository.save(usuario);
        }
    }

    public void registrarAdmin(String username, String password) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío.");
        }
        if (usuarioRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }

        Usuario newAdmin = new Usuario();
        newAdmin.setUsername(username);
        newAdmin.setPassword(passwordEncoder.encode(password));
        newAdmin.setRole("ROLE_ADMIN");
        usuarioRepository.save(newAdmin);
    }

    public void eliminarUsuario(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username).orElse(null);
        if (usuario != null) {
            usuarioRepository.delete(usuario);
        }
    }

    public void cambiarRol(String username, String newRole) {
        Usuario usuario = usuarioRepository.findByUsername(username).orElse(null);
        if (usuario != null) {
            usuario.setRole(newRole);
            usuarioRepository.save(usuario);
        } else {
            throw new IllegalArgumentException("Usuario no encontrado.");
        }
    }
}