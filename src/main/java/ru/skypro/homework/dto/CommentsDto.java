package ru.skypro.homework.dto;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @param count   Общее количество комментариев
 * @param results Список комментариев
 */
public record CommentsDto(
        Integer count,
        @NotEmpty
        List<CommentDto> results
) {
}
