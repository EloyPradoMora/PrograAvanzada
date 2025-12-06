package org.eloyprado.prograavanzada.service;

import org.eloyprado.prograavanzada.Repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import usuario.Usuario;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registrarUsuario(String username, String password, String passwordAgain) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío.");
        }
        if (!password.equals(passwordAgain)) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }
        if (usuarioRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }

        Usuario newUser = new Usuario();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        usuarioRepository.save(newUser);
    }

    public Usuario obtenerUsuarioPorNombre(String username) {
        return usuarioRepository.findByUsername(username).orElse(null);
    }
}