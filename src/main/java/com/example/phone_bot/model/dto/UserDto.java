package com.example.phone_bot.model.dto;

import com.example.phone_bot.model.Role;
import com.example.phone_bot.model.entity.UserEntity;

public record UserDto(
        Long id,
        Long telegramId,
        String name,
        Role role
) {
    public static UserDto toDto(UserEntity userEntity) {
        return new UserDto(userEntity.getId(),
                userEntity.getTelegramId(),
                userEntity.getName(),
                userEntity.getRole());
    }
}
