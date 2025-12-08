package org.eloyprado.prograavanzada.controller;

import org.eloyprado.prograavanzada.service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import usuario.Chat;
import usuario.Mensaje;

import java.security.Principal;
import java.util.List;

@Controller
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
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

    @org.springframework.web.bind.annotation.PostMapping("/chat/start")
    public String startChat(
            @org.springframework.web.bind.annotation.RequestParam("publisherUsername") String publisherUsername,
            Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String currentUser = principal.getName();
        if (!currentUser.equals(publisherUsername)) {
            chatService.iniciarChat(currentUser, publisherUsername);
        }
        return "redirect:/inbox";
    }

    @GetMapping("/chat/{id}")
    public String viewChat(@org.springframework.web.bind.annotation.PathVariable("id") String id, Model model,
            Principal principal) {
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
        return "chat";
    }

    @org.springframework.web.bind.annotation.PostMapping("/chat/{id}/send")
    public String sendMessage(@org.springframework.web.bind.annotation.PathVariable("id") String id,
            @org.springframework.web.bind.annotation.RequestParam("content") String content,
            Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        chatService.sendMessage(id, principal.getName(), content);
        return "redirect:/chat/" + id;
    }
}
