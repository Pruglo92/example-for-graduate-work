package ru.skypro.homework.exceptions.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import ru.skypro.homework.exceptions.AdNotFoundException;
import ru.skypro.homework.exceptions.CommentInconsistencyToAdException;
import ru.skypro.homework.exceptions.CommentNotFoundException;
import ru.skypro.homework.exceptions.ImageNotFoundException;

import javax.persistence.EntityNotFoundException;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerClass {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public String maxUploadSizeExceededExceptionHandler(MaxUploadSizeExceededException e) {
    return "Превышен максимальный размер файла";
    }

    @ExceptionHandler(ImageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String imageNotFoundExceptionHandler(ImageNotFoundException e) {
        return "Не найден Image по данному пути";
    }

    @ExceptionHandler(AdNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String adNotFoundExceptionHandler(AdNotFoundException e) {
        return "Не найдено объявление";
    }

    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String commentNotFoundExceptionHandler(CommentNotFoundException e) {
        return "Не найдено объявление";
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String entityNotFoundExceptionHandler(EntityNotFoundException e) {
        return e.getMessage().replace("ru.skypro.homework.entity.", "");
    }

    @ExceptionHandler(CommentInconsistencyToAdException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String commentInconsistencyToAdExceptionHandler(CommentInconsistencyToAdException e) {
        return "Не найдено соответствия между id комментария и объявления!";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String accessDeniedExceptionHandler(AccessDeniedException e) {
        return e.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleException(Exception e) {
        return e.getMessage();
    }



    /**
     * Обрабатывает исключения и возвращает соответствующий HTTP-ответ с кодом состояния и сообщением.
     *
     * @param e исключение, которое нужно обработать
     * @return HTTP-ответ с кодом состояния и сообщением об ошибке
     */
    /*
    @ExceptionHandler
    public ResponseEntity<String> handleException(Throwable e) {




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
            message = "Не найдено объявление";
        } else if (e instanceof CommentNotFoundException) {
            status = HttpStatus.NOT_FOUND;
            message = "Не найден комментарий";
        } else if (e instanceof EntityNotFoundException) {
            status = HttpStatus.NOT_FOUND;
            message = e.getMessage().replace("ru.skypro.homework.entity.", "");
        } else if (e instanceof CommentInconsistencyToAdException) {
            status = HttpStatus.NOT_FOUND;
            message = "Не найдено соответствия между id комментария и объявления!";
        } else if (e instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN;
            message = e.getMessage();
        }

        log.error(message);
        return ResponseEntity.status(status).body(message);


    }

     */
}