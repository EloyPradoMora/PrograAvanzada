package org.eloyprado.prograavanzada.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import usuario.Producto;

/**
 * Repositorio para la entidad Producto.
 * Hereda todos los métodos CRUD básicos de MongoRepository.
 */
@Repository
public interface ProductoRepository extends MongoRepository<Producto, String> {
    // Puedes definir métodos de consulta personalizados aquí si son necesarios
}