package ru.skypro.homework.dto;

import javax.validation.constraints.*;

/**
 * @param author id автора
 * @param image  ссылка на картинку объявления
 * @param pk     ID объявления
 * @param price  Цена в объявлений
 * @param title  Заголовок объявления
 */
public record AdDto(
        Integer author,
        String image,
        Integer pk,
        @NotNull(message = "Цена в объявлений не может быть пустой")
        @Min(0)
        @Max(10000000)
        Integer price,
        @NotBlank(message = "Заголовок объявления не может быть пустым")
        @Size(min = 4, max = 32, message = "Заголовок объявления должен содержать не менее 4 и не более 32 символов")
        String title
) {
}
