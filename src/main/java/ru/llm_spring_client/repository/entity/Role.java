package ru.llm_spring_client.repository.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("user"), ASSISTANT("assistant"), SYSTEM("system");

    private final String roleName;
}
