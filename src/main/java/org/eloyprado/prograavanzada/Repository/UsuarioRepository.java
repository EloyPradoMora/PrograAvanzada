package org.eloyprado.prograavanzada.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import usuario.Usuario;

import java.util.Optional;

/**
 * Repositorio para la entidad Usuario.
 */
@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    /**
     * Método para buscar un usuario por su nombre de usuario.
     * (Implementa la funcionalidad de lectura de usuarios)
     */
    Optional<Usuario> findByUsername(String username);
}