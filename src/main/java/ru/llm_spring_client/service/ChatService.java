package ru.llm_spring_client.service;

import lombok.SneakyThrows;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import ru.llm_spring_client.repository.ChatRepository;
import ru.llm_spring_client.repository.entity.Chat;
import ru.llm_spring_client.repository.entity.Message;
import ru.llm_spring_client.repository.entity.Role;

import java.util.List;

import static ru.llm_spring_client.repository.entity.Role.ASSISTANT;
import static ru.llm_spring_client.repository.entity.Role.USER;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatService chatServiceProxy;

    @Autowired
    private ChatClient chatClient;

    public List<Chat> getAllChats() {
        return chatRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public Chat getChatById(Long chatId) {
        return chatRepository.findById(chatId).orElseThrow();
    }

    public Chat createNewChat(String title) {
        Chat chat = Chat.builder().title(title).build();
        return chatRepository.save(chat);
    }

    public void deleteChatById(Long id) {
        chatRepository.deleteById(id);
    }

    public void proceedInteraction(Long chatId, String message) {
        chatServiceProxy.addChatMessage(chatId, message, USER);
        String answerMessage = chatClient.prompt().user(message).call().content();
        chatServiceProxy.addChatMessage(chatId, answerMessage, ASSISTANT);
    }

    @Transactional
    protected void addChatMessage(Long chatId, String message, Role role) {
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        chat.addMessage(Message.builder().content(message).role(role).build());
    }

    public SseEmitter proceedInteractionWithStreaming(Long chatId, String message) {
        chatServiceProxy.addChatMessage(chatId, message, USER);
        SseEmitter sseEmitter = new SseEmitter(0L);
        StringBuffer answerMessage = new StringBuffer();
        chatClient.prompt().user(message).stream()
                .chatResponse()
                .subscribe(response -> processToken(response, sseEmitter, answerMessage),
                        sseEmitter::completeWithError,
                        () -> chatServiceProxy.addChatMessage(chatId, answerMessage.toString(), ASSISTANT));

        return sseEmitter;
    }

    @SneakyThrows
    private void processToken(ChatResponse response, SseEmitter sseEmitter, StringBuffer stringBuffer) {
        AssistantMessage token = response.getResult().getOutput();
        sseEmitter.send(token);
        stringBuffer.append(token.getText());
    }
}
