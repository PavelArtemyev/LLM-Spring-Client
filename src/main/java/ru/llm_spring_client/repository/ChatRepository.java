package ru.llm_spring_client.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.llm_spring_client.repository.entity.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
}
