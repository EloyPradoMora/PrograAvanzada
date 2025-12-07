package org.eloyprado.prograavanzada.controller;

import org.eloyprado.prograavanzada.service.ChatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import usuario.Chat;

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
}
