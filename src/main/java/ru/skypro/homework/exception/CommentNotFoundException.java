package ru.skypro.homework.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
@RequiredArgsConstructor
public class CommentNotFoundException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Комментарий не найден";

    }
}
