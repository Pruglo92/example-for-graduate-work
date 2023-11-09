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
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.service.AdService;

import javax.validation.Valid;

/**
 * Контроллер для работы с объявлениями.
 */
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@Tag(name = "Объявления")
@Validated
@RequiredArgsConstructor

public class AdsController {

    private final AdService adService;

    /**
     * Удаление объявления.
     *
     * @param id идентификатор объявления
     * @return {@link ResponseEntity} без тела (No Content)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление объявления",
            description = "Удаление объявления по идентификационному номеру авторизованным пользователем")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not Found")
    public ResponseEntity<Void> removeAd(@PathVariable("id") Integer id) {
        adService.removeAd(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получение всех объявлений.
     *
     * @return {@link ResponseEntity} с объектом {@link AdsDto}
     */
    @GetMapping
    @Operation(summary = "Получение всех объявлений",
            description = "Получение количества и списка всех объявлений")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<AdsDto> getAllAds() {
        AdsDto allAdsDtoList = adService.getAllAds();
        return ResponseEntity.ok(allAdsDtoList);
    }

    /**
     * Добавление объявления.
     *
     * @param createOrUpdateAdDto объект с данными для создания или обновления объявления
     * @param image               изображение объявления
     * @return {@link ResponseEntity} с объектом {@link AdDto}
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Добавление объявления",
            description = "Добавление изображения и всех полей объявления")
    @ApiResponse(responseCode = "201", description = "Created")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<AdDto> addAd(@RequestPart("properties") @Valid CreateOrUpdateAdDto createOrUpdateAdDto,
                                       @RequestPart MultipartFile image) {
        AdDto addedAdDto = adService.addAd(createOrUpdateAdDto, image);
        return ResponseEntity.ok(addedAdDto);
    }

    /**
     * Получение информации об объявлении по его id.
     *
     * @param id идентификатор объявления
     * @return {@link ResponseEntity} с объектом {@link ExtendedAdDto}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получение информации об объявлении",
            description = "Получение информации об объявлении по его id")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<ExtendedAdDto> getExtendedAdById(@PathVariable("id") Integer id) {
        ExtendedAdDto extendedAdDto = adService.getAdById(id);
        return ResponseEntity.ok(extendedAdDto);
    }

    /**
     * Обновление информации об объявлении по id объявления.
     *
     * @param id                  идентификатор объявления
     * @param createOrUpdateAdDto объект с данными для создания или обновления объявления
     * @return {@link ResponseEntity} с объектом {@link AdDto}
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Обновление информации об объявлении",
            description = "Обновление информации об объявлении по id объявления")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<AdDto> updateAd(@PathVariable("id") Integer id,
                                          @RequestBody @Valid CreateOrUpdateAdDto createOrUpdateAdDto) {
        return ResponseEntity.ok(adService.updateAd(id, createOrUpdateAdDto));
    }

    /**
     * Получение объявлений авторизованного пользователя.
     *
     * @return {@link ResponseEntity} с объектом {@link AdsDto}
     */
    @GetMapping("/me")
    @Operation(summary = "Получение объявлений авторизованного пользователя")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<AdsDto> getAuthorizedUserAds() {
        AdsDto userAdsDtoList = adService.getAuthorizedUserAds();
        return ResponseEntity.ok(userAdsDtoList);
    }

    /**
     * Обновление картинки объявления по id объявления.
     *
     * @param id    идентификатор объявления
     * @param image новое изображение объявления
     * @return {@link ResponseEntity} с сообщением об успешном обновлении
     */
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Обновление картинки объявления",
            description = "Обновление картинки объявления по id объявления")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<String> updateAdImage(@PathVariable("id") Integer id,
                                                @RequestParam MultipartFile image) {
        adService.updateAdImage(id, image);
        return ResponseEntity.ok("Image updated successfully");
    }

}