package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.service.ImageService;

/**
 * Контроллер отвечающий за обработку HTTP-запросов, связанных с ним изображений.
 */
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    /**
     * Получает изображение с указанным именем.
     *
     * @param name Имя изображения.
     * @return Ответ со статусом HTTP 200 и содержимым изображения в теле ответа.
     */
    @GetMapping(value = "/{name}", produces = {
            MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_GIF_VALUE,
    })
    public ResponseEntity<Resource> getImage(@PathVariable String name) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imageService.getImageFromFile(name));
    }
}