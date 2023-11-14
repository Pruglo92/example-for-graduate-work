package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.LoginDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.service.AuthService;

import javax.validation.Valid;

/**
 * Класс, отвечающий за управление авторизацией и регистрацией пользователей.
 */
@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    /**
     * Авторизует пользователя на основе предоставленных учетных данных.
     *
     * @param loginDto данные пользователя для авторизации
     * @return Ответ сервера со статусом 200 OK в случае успешной авторизации,
     * или ответ с кодом 401 Unauthorized в случае неудачной авторизации.
     */
    @Tag(name = "Авторизация")
    @Operation(summary = "Авторизация пользователя")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto) {
        if (authService.login(loginDto.username(), loginDto.password())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Регистрирует нового пользователя на основе предоставленных данных.
     *
     * @param registerDto данные нового пользователя для регистрации
     * @return Ответ сервера со статусом 201 Created в случае успешной регистрации,
     * или ответ с кодом 400 Bad Request в случае некорректных данных.
     */

    @Tag(name = "Регистрация")
    @Operation(summary = "Регистрация пользователя")
    @ApiResponse(responseCode = "201", description = "Created")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDto registerDto) {
        if (authService.register(registerDto)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}