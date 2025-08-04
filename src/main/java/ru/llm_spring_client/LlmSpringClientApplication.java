package ru.llm_spring_client;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LlmSpringClientApplication {

    public static void main(String[] args) {
        ChatClient chatClient = SpringApplication.run(LlmSpringClientApplication.class, args).getBean(ChatClient.class);
        System.out.println(chatClient.prompt().user("Назови имена каждого пальца на руке").call().content());
    }

}
