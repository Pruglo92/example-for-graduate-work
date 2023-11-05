package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.service.CommentService;

import javax.validation.Valid;

/**
 * Контроллер для работы с комментариями.
 */
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@Tag(name = "Комментарии")
@RequiredArgsConstructor
@Validated
public class CommentsController {

    private final CommentService commentService;

    /**
     * Удаляет комментарий в объявлении.
     *
     * @param adId      Идентификатор объявления.
     * @param commentId Идентификатор комментария.
     * @return Ответ со статусом HTTP 200 в случае успешного удаления комментария.
     */
    @DeleteMapping("/{adId}/comments/{commentId}")
    @Operation(summary = "Удаление комментария в объявлении",
            description = "Удаление комментария по id объявления и id комментария авторизованным пользователем")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not Found")
    public ResponseEntity<Void> removeComment(@PathVariable("adId") Integer adId,
                                              @PathVariable("commentId") Integer commentId) {
        commentService.removeComment(adId, commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Получает все комментарии объявления.
     *
     * @param adId Идентификатор объявления.
     * @return Ответ со статусом HTTP 200 и списком комментариев в теле ответа.
     */
    @GetMapping("/{id}/comments")
    @Operation(summary = "Получение комментариев объявления",
            description = "Получение всех комментариев объявления по id объявления")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<CommentsDto> getCommentsByAd(@PathVariable("id") Integer adId) {
        return ResponseEntity.ok(commentService.getCommentsByAd(adId));
    }

    /**
     * Добавляет комментарий к объявлению.
     *
     * @param adId                     Идентификатор объявления.
     * @param createOrUpdateCommentDto Данные для создания или обновления комментария.
     * @return Ответ со статусом HTTP 200 и созданным комментарием в теле ответа.
     */
    @PostMapping("/{id}/comments")
    @Operation(summary = "Добавление комментария к объявлению",
            description = "Добавление комментария к объявлению по его id")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<CommentDto> addCommentToAd(@PathVariable("id") Integer adId,
                                                     @RequestBody @Valid CreateOrUpdateCommentDto createOrUpdateCommentDto) {
        return ResponseEntity.ok(commentService.addCommentToAd(adId, createOrUpdateCommentDto));
    }

    /**
     * Обновляет комментарий в объявлении.
     *
     * @param adId                     Идентификатор объявления.
     * @param commentId                Идентификатор комментария.
     * @param createOrUpdateCommentDto Данные для создания или обновления комментария.
     * @return Ответ со статусом HTTP 200 и обновленным комментарием в теле ответа.
     */
    @PatchMapping("/{adId}/comments/{commentId}")
    @Operation(summary = "Обновление комментария",
            description = "Обновление комментария к объявлению по id объявления и id комментария")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<CommentDto> updateCommentToAd(@PathVariable("adId") Integer adId,
                                                        @PathVariable("commentId") Integer commentId,
                                                        @RequestBody @Valid CreateOrUpdateCommentDto createOrUpdateCommentDto) {
        return ResponseEntity.ok(commentService.updateCommentToAd(adId, commentId, createOrUpdateCommentDto));
    }
}