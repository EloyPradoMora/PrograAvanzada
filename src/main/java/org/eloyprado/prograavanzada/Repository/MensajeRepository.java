package org.eloyprado.prograavanzada.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import usuario.Mensaje;
import java.util.List;

@Repository
public interface MensajeRepository extends MongoRepository<Mensaje, String> {
    List<Mensaje> findByChatIdOrderByTimestampAsc(String chatId);
}
