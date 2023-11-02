package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.AdImage;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.entity.User;

import java.util.List;

@Mapper
public interface AdMapper {

    /**
     * Преобразует объект `Ad` в объект `ExtendedAdDto`, расширенную версию DTO для объявления.
     *
     * @param ad объект `Ad` для преобразования
     * @return объект `ExtendedAdDto`
     */
    @Mapping(target = "authorFirstName", source = "user.firstName")
    @Mapping(target = "authorLastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.login")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "image", source = "image.filePath")
    @Mapping(target = "pk", source = "id")
    ExtendedAdDto toDto(Ad ad);

    /**
     * Преобразует объект `Ad` в объект `AdDto`, простую версию DTO для объявления.
     *
     * @param ad объект `Ad` для преобразования
     * @return объект `AdDto`
     */
    @Mapping(target = "image", source = "image.filePath")
    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", source = "user.id")
    AdDto toAdDto(Ad ad);

    /**
     * Создает объект `Ad` на основе данных из `CreateOrUpdateAdDto`, `User` и `Image`.
     *
     * @param createOrUpdateAdDto данные для создания или обновления объявления
     * @param user                пользователь, создающий или обновляющий объявление
     * @param image               изображение, связанное с объявлением
     * @return объект `Ad`
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    Ad createAdDtoToAd(CreateOrUpdateAdDto createOrUpdateAdDto, User user, Image image);

    /**
     * Обновляет объект `Ad` на основе данных из `CreateOrUpdateAdDto` и `User`.
     *
     * @param id                  идентификатор объявления для обновления
     * @param createOrUpdateAdDto данные для обновления объявления
     * @param user                пользователь, обновляющий объявление
     * @return объект `Ad`
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "user", source = "user")
    Ad updateAdDtoToAd(Integer id, CreateOrUpdateAdDto createOrUpdateAdDto, User user);

    /**
     * Преобразует список объектов `Ad` в список объектов `AdDto`.
     *
     * @param list список объявлений
     * @return список `AdDto`
     */
    List<AdDto> toAdsDto(List<Ad> list);

    /**
     * Преобразует `MultipartFile` в объект `AdImage`.
     *
     * @param file файл изображения
     * @return объект `AdImage`
     */
    AdImage toAdImage(MultipartFile file);
}
