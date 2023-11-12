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

/**
 * Хэндлер для обработки исключений и возвращения соответствующих HTTP-ответов и сообщений.
 */

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerClass {

    /**
     * Обрабатывает MaxUploadSizeExceededException.
     *
     * @param e объект текущего исключения.
     * @return 417 код ответа и сообщение, соответствующее исключению.
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public String maxUploadSizeExceededExceptionHandler(MaxUploadSizeExceededException e) {
        return "Превышен максимальный размер файла!";
    }

    /**
     * Обрабатывает ImageNotFoundException.
     *
     * @param e объект текущего исключения.
     * @return 404 код ответа и сообщение, соответствующее исключению.
     */
    @ExceptionHandler(ImageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String imageNotFoundExceptionHandler(ImageNotFoundException e) {
        return "Не найдено изображение по данному пути!";
    }

    /**
     * Обрабатывает AdNotFoundException.
     *
     * @param e объект текущего исключения.
     * @return 404 код ответа и сообщение, соответствующее исключению.
     */
    @ExceptionHandler(AdNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String adNotFoundExceptionHandler(AdNotFoundException e) {
        return "Не найдено объявление!";
    }

    /**
     * Обрабатывает CommentNotFoundException.
     *
     * @param e объект текущего исключения.
     * @return 404 код ответа и сообщение, соответствующее исключению.
     */
    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String commentNotFoundExceptionHandler(CommentNotFoundException e) {
        return "Не найден комментарий!";
    }

    /**
     * Обрабатывает EntityNotFoundException.
     *
     * @param e объект текущего исключения.
     * @return 404 код ответа и сообщение, соответствующее исключению.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String entityNotFoundExceptionHandler(EntityNotFoundException e) {
        return e.getMessage().replace("ru.skypro.homework.entity.", "");
    }

    /**
     * Обрабатывает CommentInconsistencyToAdException.
     *
     * @param e объект текущего исключения.
     * @return 404 код ответа и сообщение, соответствующее исключению.
     */
    @ExceptionHandler(CommentInconsistencyToAdException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String commentInconsistencyToAdExceptionHandler(CommentInconsistencyToAdException e) {
        return "Не найдено соответствия между id комментария и объявления!";
    }

    /**
     * Обрабатывает AccessDeniedException.
     *
     * @param e объект текущего исключения.
     * @return 403 код ответа и сообщение, соответствующее исключению.
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String accessDeniedExceptionHandler(AccessDeniedException e) {
        return e.getMessage();
    }

    /**
     * Обрабатывает исключения, наследуемые от Exception, если они не были обработаны в предыдущих методах.
     *
     * @param e объект текущего исключения.
     * @return 400 код ответа и сообщение, соответствующее исключению.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleException(Exception e) {
        return e.getMessage();
    }

}