package ru.skypro.homework.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @param title       Заголовок объявления
 * @param price       Цена в объявлений
 * @param description Описание объявления
 */
public record CreateOrUpdateAdDto(
        @NotBlank(message = "Заголовок объявления не может быть пустым")
        @Size(min = 4, max = 32, message = "Заголовок объявления должен содержать не менее 4 и не более 32 символов")
        String title,
        @NotNull(message = "Цена в объявлений не может быть пустой")
        @Min(0)
        @Max(10000000)
        Integer price,
        @NotBlank(message = "Описание объявления не может быть пустым")
        @Size(min = 8, max = 64, message = "Описание объявления должен содержать не менее 8 и не более 64 символов")
        String description
) {
}