package ru.skypro.homework.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, которое выбрасывается в случае отсутствия соответствия между идентификатором комментария и объявления.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentInconsistencyToAdException extends RuntimeException {
    /**
     * Возвращает сообщение об ошибке для данного исключения.
     *
     * @return сообщение об ошибке
     */
    @Override
    public String getMessage() {
        return "Не найдено соответствия между айди комментария и объявления!";
    }
}
