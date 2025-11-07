package org.eloyprado.prograavanzada.service;

import org.eloyprado.prograavanzada.Repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // Se marca como un @Service para que Spring lo detecte automáticamente
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository repository;

    // Se inyecta el repositorio en el servicio, no en la configuración.
    public CustomUserDetailsService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // La lógica de negocio para cargar un usuario ahora vive aquí.
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }
}