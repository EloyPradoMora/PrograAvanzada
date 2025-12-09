package org.eloyprado.prograavanzada.controller;

import org.eloyprado.prograavanzada.service.ChatService;
import org.eloyprado.prograavanzada.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import usuario.Chat;
import usuario.Mensaje;
import usuario.Usuario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;
import java.util.List;

@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final ChatService chatService;
    private final UsuarioService usuarioService;

    public ChatController(ChatService chatService, UsuarioService usuarioService) {
        this.chatService = chatService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/inbox")
    public String inbox(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        List<Chat> chats = chatService.getChatsForUser(username);
        model.addAttribute("chats", chats);
        model.addAttribute("currentUser", username);
        return "inbox";
    }

    @PostMapping("/chat/start")
    public String startChat(
            @RequestParam("publisherUsername") String publisherUsername,
            @RequestParam("productId") String productId,
            @RequestParam("productName") String productName,
            @RequestParam(value = "productImage", required = false) String productImage,
            Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String currentUser = principal.getName();
        if (!currentUser.equals(publisherUsername)) {
            chatService.iniciarChat(currentUser, publisherUsername, productId, productName, productImage);
        }
        return "redirect:/inbox";
    }

    @GetMapping("/chat/{id}")
    public String viewChat(@PathVariable("id") String id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        Chat chat = chatService.getChatById(id);
        if (chat == null || !chat.getParticipants().contains(principal.getName())) {
            return "redirect:/inbox";
        }

        List<Mensaje> mensajes = chatService.getMessages(id);
        model.addAttribute("chat", chat);
        model.addAttribute("mensajes", mensajes);
        model.addAttribute("currentUser", principal.getName());

        // Find other user and get prestige
        String otherUserName = chat.getParticipants().stream()
                .filter(p -> !p.equals(principal.getName()))
                .findFirst().orElse(null);
        if (otherUserName != null) {
            Usuario otherUser = usuarioService.obtenerUsuarioPorNombre(otherUserName);
            if (otherUser != null) {
                model.addAttribute("otherUser", otherUser);
                model.addAttribute("otherPrestige", otherUser.getPrestigio());
            }
        }
        return "chat";
    }

    @PostMapping("/chat/{id}/send")
    public String sendMessage(@PathVariable("id") String id,
            @RequestParam("content") String content,
            Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        Chat chat = chatService.getChatById(id);
        if (chat != null && (chat.getStatus() == Chat.Status.SEALED || chat.getStatus() == Chat.Status.ARCHIVED)) {
            return "redirect:/chat/" + id;
        }

        chatService.sendMessage(id, principal.getName(), content);
        logger.info("Usuario '{}' mandó mensaje a chat '{}'", principal.getName(), id);
        return "redirect:/chat/" + id;
    }

    @PostMapping("/chat/{id}/cancel")
    public String cancelChat(@PathVariable("id") String id, Principal principal) {
        chatService.deleteChat(id);
        return "redirect:/inbox";
    }

    @PostMapping("/chat/{id}/seal/initiate")
    public String initiateSeal(@PathVariable("id") String id, Principal principal) {
        chatService.initiateSeal(id, principal.getName());
        return "redirect:/chat/" + id;
    }

    @PostMapping("/chat/{id}/seal/confirm")
    public String confirmSeal(@PathVariable("id") String id) {
        chatService.confirmSeal(id);
        return "redirect:/chat/" + id;
    }

    @PostMapping("/chat/{id}/seal/deny")
    public String denySeal(@PathVariable("id") String id) {
        chatService.denySeal(id);
        return "redirect:/chat/" + id;
    }

    @PostMapping("/chat/{id}/rate")
    public String rateUser(@PathVariable("id") String id,
            @RequestParam("rating") String rating,
            Principal principal) {
        chatService.rateUser(id, principal.getName(), rating);
        return "redirect:/chat/" + id;
    }
}
