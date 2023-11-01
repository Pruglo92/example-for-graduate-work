package ru.skypro.homework.dto;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @param count   Oбщее количество объявлений
 * @param results Список объявлений
 */
public record AdsDto(

        Integer count,
        @NotEmpty
        List<AdDto> results
) {
}
