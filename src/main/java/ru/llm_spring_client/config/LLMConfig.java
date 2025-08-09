package ru.llm_spring_client.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.llm_spring_client.repository.ChatRepository;
import ru.llm_spring_client.service.PostgresChatMemory;

@Component
@RequiredArgsConstructor
public class LLMConfig {

    private final ChatRepository chatRepository;

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultAdvisors(getAdvisor()).build();
    }

    private Advisor getAdvisor() {
        return MessageChatMemoryAdvisor.builder(getChatMemory()).build();
    }

    private ChatMemory getChatMemory() {
        return PostgresChatMemory.builder()
                .maxMessages(3)
                .chatMemoryRepository(chatRepository)
                .build();
    }
}
