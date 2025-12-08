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
    private final UsuarioService usuarioService;

    public ChatService(ChatRepository chatRepository, MensajeRepository mensajeRepository,
            UsuarioService usuarioService) {
        this.chatRepository = chatRepository;
        this.mensajeRepository = mensajeRepository;
        this.usuarioService = usuarioService;
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

    public void deleteChat(String chatId) {
        chatRepository.deleteById(chatId);
    }

    public void initiateSeal(String chatId, String username) {
        Chat chat = getChatById(chatId);
        if (chat != null) {
            chat.setStatus(Chat.Status.WAITING_SEAL_CONFIRMATION);
            chat.setDealInitiator(username);
            chatRepository.save(chat);
        }
    }

    public void confirmSeal(String chatId) {
        Chat chat = getChatById(chatId);
        if (chat != null) {
            chat.setStatus(Chat.Status.SEALED);
            chatRepository.save(chat);
        }
    }

    public void denySeal(String chatId) {
        Chat chat = getChatById(chatId);
        if (chat != null) {
            chat.setStatus(Chat.Status.ACTIVE);
            chat.setDealInitiator(null);
            chatRepository.save(chat);
        }
    }

    public void rateUser(String chatId, String raterUsername, String rating) {
        Chat chat = getChatById(chatId);
        if (chat != null && chat.getStatus() == Chat.Status.SEALED) {
            // Indentify 'other' user
            String otherUser = chat.getParticipants().stream()
                    .filter(p -> !p.equals(raterUsername))
                    .findFirst().orElse(null);

            if (otherUser != null) {
                int prestigeChange = 0;
                switch (rating) {
                    case "Bueno":
                        prestigeChange = 20;
                        break;
                    case "Satisfecho":
                        prestigeChange = 10;
                        break;
                    case "Malo":
                        prestigeChange = -200;
                        break;
                }
                usuarioService.updatePrestige(otherUser, prestigeChange);
            }

            if (chat.getParticipants().indexOf(raterUsername) == 0) {
                chat.setBuyerRated(true);
            } else {
                chat.setSellerRated(true);
            }

            if (chat.isBuyerRated() && chat.isSellerRated()) {
                chat.setStatus(Chat.Status.ARCHIVED);
                chat.setProductId(null); // Desvincula producto
            }
            chatRepository.save(chat);
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
