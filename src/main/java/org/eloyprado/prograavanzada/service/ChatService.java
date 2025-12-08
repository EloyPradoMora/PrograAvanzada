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

    public void iniciarChat(String sender, String recipient, String productId, String productName,
            String productImage) {
        // Ver si el chat ya existe, solo se puede negociar el mismo producto uno a la vez o en un paquete grande a la vez  
        List<Chat> existingChats = chatRepository.findByParticipantsContaining(sender);
        boolean chatExists = existingChats.stream()
                .anyMatch(chat -> chat.getParticipants().contains(recipient)
                        && (productId == null ? chat.getProductId() == null : productId.equals(chat.getProductId())));

        if (!chatExists) {
            Chat newChat = new Chat(java.util.List.of(sender, recipient), productId, productName, productImage);
            chatRepository.save(newChat);
        }
    }

    public Chat getChatById(String chatId) {
        return chatRepository.findById(chatId).orElse(null);
    }

    public List<Mensaje> getMessages(String chatId) {
        return mensajeRepository.findByChatIdOrderByTimestampAsc(chatId);
    }

    public void sendMessage(String chatId, String sender, String content) {
        Mensaje mensaje = new Mensaje(chatId, sender, content);
        mensajeRepository.save(mensaje);

        Chat chat = chatRepository.findById(chatId).orElse(null);
        if (chat != null) {
            chat.setLastMessage(content);
            chat.setLastMessageDate(java.time.LocalDateTime.now());
            chatRepository.save(chat);
        }
    }
}
