package ru.skypro.homework.dto;

import ru.skypro.homework.enums.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @param id        id пользователя
 * @param email     Логин в виде электронной почты
 * @param firstName Имя автора
 * @param lastName  Фамилия автора
 * @param phone     Номер контактного телефона
 * @param role      Роль пользователя
 * @param image     Ссылка на аватар пользователя
 */
public record UserDto(
        Integer id,
        @NotBlank(message = "Поле логин не может быть пустым")
        @Size(min = 4, max = 32, message = "Логин должен содержать от 4 до 32 символов")
        @Email
        String email,
        @NotBlank(message = "Имя автора не может быть пустым")
        @Size(min = 3, max = 10, message = "Имя автора должно быть от 2 до 10 символов")
        String firstName,
        @NotBlank(message = "Фамилия автора не может быть пустым")
        @Size(min = 3, max = 10, message = "Фамилия автора должно быть от 2 до 10 символов")
        String lastName,
        @NotBlank(message = "Введите номер Вашего контактного телефона")
        @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}",
                message = "Введите номер в формате +7 (000) 000-00-00")
        String phone,
        Role role,
        String image
) {
}
