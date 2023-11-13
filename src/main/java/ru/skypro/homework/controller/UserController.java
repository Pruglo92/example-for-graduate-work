package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.service.UserService;

import javax.validation.Valid;

/**
 * Контроллер отвечающий за обработку HTTP-запросов, связанных с пользователями.
 */
@RestController
@RequestMapping("/users")
@CrossOrigin(value = "http://localhost:3000")
@Tag(name = "Пользователи")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    /**
     * Обновляет пароль авторизованного пользователя.
     *
     * @param newPasswordDto DTO-объект с текущим и новым паролем.
     * @return Ответ со статусом HTTP 204 (No Content).
     */
    @Operation(summary = "Обновление пароля")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @PostMapping("/set_password")
    public ResponseEntity<Void> setPassword(@RequestBody @Valid NewPasswordDto newPasswordDto) {
        userService.setPassword(newPasswordDto.currentPassword(), newPasswordDto.newPassword());
        return ResponseEntity.noContent().build();
    }

    /**
     * Получает информацию об авторизованном пользователе.
     *
     * @return Ответ со статусом HTTP 200 и DTO-объектом пользователя в теле ответа.
     */
    @Operation(summary = "Получение информации об авторизованном пользователе")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @GetMapping("/me")
    public ResponseEntity<UserDto> getUser() {
        return ResponseEntity.ok(userService.getAuthorizedUser());
    }

    /**
     * Обновляет информацию об авторизованном пользователе.
     *
     * @param updateUser DTO с обновленными данными пользователя.
     * @return Ответ со статусом HTTP 200 OK и обновленной информацией об авторизованном пользователе в теле ответа.
     */
    @PatchMapping("/me")
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<UpdateUserDto> updateUser(@RequestBody @Valid UpdateUserDto updateUser) {
        return ResponseEntity.ok(userService.updateUser(updateUser));
    }

    /**
     * Обновляет аватар авторизованного пользователя.
     *
     * @param image MultipartFile с изображением аватара пользователя.
     * @return Ответ со статусом HTTP 204 No Content в случае успешного обновления аватара.
     */
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Обновление аватара авторизованного пользователя")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<Void> updateUserImage(@RequestParam("image") MultipartFile image) {
        userService.updateUserImage(image);
        return ResponseEntity.noContent().build();
    }
}