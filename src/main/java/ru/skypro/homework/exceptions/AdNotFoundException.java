package ru.skypro.homework.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, которое выбрасывается в случае не нахождения объявления по указанному идентификатору.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
@RequiredArgsConstructor
public class AdNotFoundException extends RuntimeException {

    private final Integer adId;

    /**
     * Возвращает сообщение об ошибке для данного исключения.
     *
     * @return сообщение об ошибке
     */
    @Override
    public String getMessage() {
        return "Объявление с id = " + adId + " не найдено!";
    }
}