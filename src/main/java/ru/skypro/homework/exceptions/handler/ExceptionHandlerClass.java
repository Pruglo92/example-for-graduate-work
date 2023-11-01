package ru.skypro.homework.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import ru.skypro.homework.exceptions.AdNotFoundException;
import ru.skypro.homework.exceptions.ImageNotFoundException;

/**
 * Класс, отвечающий за обработку исключений при выполнении запросов в контроллерах.
 */
@ControllerAdvice
public class ExceptionHandlerClass {
    /**
     * Обрабатывает исключения и возвращает соответствующий HTTP-ответ с кодом состояния и сообщением.
     *
     * @param e   исключение, которое нужно обработать
     * @param <T> тип исключения
     * @return HTTP-ответ с кодом состояния и сообщением об ошибке
     */
    @ExceptionHandler
    public <T extends Throwable> ResponseEntity<String> handleException(T e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = e.getMessage();

        if (e instanceof MaxUploadSizeExceededException) {
            status = HttpStatus.EXPECTATION_FAILED;
            message = "Превышен максимальный размер файла ";
        } else if (e instanceof ImageNotFoundException) {
            status = HttpStatus.NOT_FOUND;
            message = "Не найден Image по данному пути";
        } else if (e instanceof AdNotFoundException) {
            status = HttpStatus.NOT_FOUND;
            message = "Объявление не найдено";
        }

        e.printStackTrace(System.err);
        return ResponseEntity.status(status).body(message);
    }


}