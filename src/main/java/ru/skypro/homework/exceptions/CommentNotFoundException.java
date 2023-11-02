package ru.skypro.homework.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Исключение, которое выбрасывается в случае, если комментарий с указанным идентификатором не найден.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
@RequiredArgsConstructor
public class CommentNotFoundException extends RuntimeException {
    private final Integer commentId;
    /**
     * Возвращает сообщение об ошибке для данного исключения.
     *
     * @return сообщение об ошибке
     */
    @Override
    public String getMessage() {
        return "Комментарий с id = " + commentId + " не найден!";
    }
}
