package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentDtoList;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/ads")
@Tag(name = "Комментарии")
public class CommentsController {

    @Operation(summary = "Получение комментариев объявления")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("/{id}/comments")
    public ResponseEntity<CommentDtoList> getCommentsByAd(@PathVariable("id") Integer id) {
        List<CommentDto> list = new ArrayList<>();
        return ResponseEntity.ok(new CommentDtoList(list));
    }

    @Operation(summary = "Добавление комментария к объявлению")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDto> addCommentToAd(@PathVariable("id") Integer id, @RequestBody CommentDto newCommentDto) {
        return ResponseEntity.ok(newCommentDto);
    }

    @Operation(summary = "Обновление комментария")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    @PostMapping("/{id}/comments/{comment_id}")
    public ResponseEntity<CommentDto> updateCommentToAd(@PathVariable("id") Integer id,
                                                        @PathVariable("comment_id") Integer commentId,
                                                        @RequestBody CommentDto updatedCommentDto) {
        return ResponseEntity.ok(updatedCommentDto);
    }

}
