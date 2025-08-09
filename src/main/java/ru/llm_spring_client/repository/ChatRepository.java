package ru.llm_spring_client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.llm_spring_client.repository.entity.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>/*, ChatMemoryRepository*/ {

//    @Override
//    default List<String> findConversationIds() {
//        return findAll().stream()
//                .map(chat -> String.valueOf(chat.getId()))
//                .toList();
//    }
//
//    @Override
//    default List<Message> findByConversationId(String conversationId) {
//        Chat chat = findById(Long.valueOf(conversationId)).orElseThrow();
//        return chat.getMessages().stream()
//                .map(ChatMessage::toMessage)
//                .toList();
//    }
//
//    @Override
//    default void saveAll(String conversationId, List<Message> messages) {
//        Chat chat = findById(Long.valueOf(conversationId)).orElseThrow();
//        messages.stream().map(ChatMessage::toChatMessage).forEach(chat::addChatMessage);
//        save(chat);
//    }
//
//    @Override
//    default void deleteByConversationId(String conversationId) {
//        // there is no implementation
//    }
}
