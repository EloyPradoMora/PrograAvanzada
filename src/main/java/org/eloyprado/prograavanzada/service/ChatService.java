package org.eloyprado.prograavanzada.service;

import org.eloyprado.prograavanzada.Repository.ChatRepository;
import org.eloyprado.prograavanzada.Repository.MensajeRepository;
import org.springframework.stereotype.Service;
import usuario.Chat;
import usuario.Mensaje;

import java.util.List;

@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final MensajeRepository mensajeRepository;

    public ChatService(ChatRepository chatRepository, MensajeRepository mensajeRepository) {
        this.chatRepository = chatRepository;
        this.mensajeRepository = mensajeRepository;
    }

    public List<Chat> getChatsForUser(String username) {
        return chatRepository.findByParticipantsContaining(username);
    }
}
