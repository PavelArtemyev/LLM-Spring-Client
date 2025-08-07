package ru.llm_spring_client.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.llm_spring_client.repository.entity.Chat;
import ru.llm_spring_client.service.ChatService;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/")
    public String getMainPage(ModelMap modelMap) {
        modelMap.addAttribute("chats", chatService.getAllChats());
        return "chat";
    }

    @GetMapping("/chat/{chatId}")
    public String showChat(@PathVariable Long chatId, ModelMap modelMap) {
        modelMap.addAttribute("chats", chatService.getAllChats());
        modelMap.addAttribute("chat", chatService.getChatById(chatId));
        return "chat";
    }

    @PostMapping("/chat/new")
    public String newChat(@RequestParam String title) {
        Chat chat = chatService.createNewChat(title);
        return "redirect:/chat/" + chat.getId();
    }

    @PostMapping("/chat/{id}/delete")
    public String deleteChat(@PathVariable Long id) {
        chatService.deleteChatById(id);
        return "redirect:/";
    }

    @PostMapping("/chat/{chatId}/entry")
    public String sendMessage(@PathVariable Long chatId, @RequestParam String message) {
        chatService.proceedInteraction(chatId, message);
        return "redirect:/chat/" + chatId;
    }
}
