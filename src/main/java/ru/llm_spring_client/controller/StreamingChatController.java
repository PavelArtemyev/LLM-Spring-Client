package ru.llm_spring_client.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import ru.llm_spring_client.service.ChatService;

@RestController
@RequiredArgsConstructor
public class StreamingChatController {

    private final ChatService chatService;

    @GetMapping(value = "/chat-stream/{chatId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sendMessage(@PathVariable Long chatId, @RequestParam String message) {
        return chatService.proceedInteractionWithStreaming(chatId, message);
    }
}
