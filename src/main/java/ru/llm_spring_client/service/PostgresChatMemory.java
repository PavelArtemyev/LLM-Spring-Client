package ru.llm_spring_client.service;

import lombok.Builder;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import ru.llm_spring_client.repository.ChatRepository;
import ru.llm_spring_client.repository.entity.Chat;
import ru.llm_spring_client.repository.entity.ChatMessage;

import java.util.Comparator;
import java.util.List;

@Builder
public class PostgresChatMemory implements ChatMemory {

    private final ChatRepository chatMemoryRepository;

    private int maxMessages;

    @Override
    public void add(String conversationId, List<Message> messages) {
        for (Message message : messages) {
            Chat chat = chatMemoryRepository.findById(Long.valueOf(conversationId)).orElseThrow();
            chat.addChatMessage(ChatMessage.toChatMessage(message));
            chatMemoryRepository.save(chat);
        }
    }

    @Override
    public List<Message> get(String conversationId) {
        return chatMemoryRepository.findById(Long.valueOf(conversationId)).orElseThrow()
                .getMessages().stream()
                .sorted(Comparator.comparing(ChatMessage::getCreatedAt).reversed())
                .map(ChatMessage::toMessage)
                .limit(maxMessages)
                .toList();
    }

    @Override
    public void clear(String conversationId) {
        // there is no implementation
    }
}
