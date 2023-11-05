package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper
public interface CommentMapper {
     /**
     * Преобразует объект `Comment` в объект `CommentDto`.
     *
     * @param comment объект `Comment` для преобразования
     * @return объект `CommentDto`
     */
    @Mapping(target = "author", source = "user.id")
    @Mapping(target = "authorImage", source = "user.userImage.filePath")
    @Mapping(target = "authorFirstName", source = "user.firstName")
    @Mapping(target = "createdAt", expression = "java(getLongFromLocalDateTime(comment.getCreatedAt()))")
    @Mapping(target = "pk", source = "id")
    CommentDto entityToCommentDto(Comment comment);

    /**
     * Создает объект `Comment` на основе данных из `Ad`, `CreateOrUpdateCommentDto` и `User`.
     *
     * @param ad                       объявление, к которому относится комментарий
     * @param createOrUpdateCommentDto данные для создания или обновления комментария
     * @param user                     пользователь, создающий или обновляющий комментарий
     * @return объект `Comment`
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "ad", source = "ad")
    Comment createCommentDtoToEntity(Ad ad, CreateOrUpdateCommentDto createOrUpdateCommentDto, User user);

    /**
     * Обновляет объект `Comment` на основе данных из `Ad`, `CreateOrUpdateCommentDto`, `LocalDateTime` и `User`.
     *
     * @param ad                       объявление, к которому относится комментарий
     * @param commentId                идентификатор комментария для обновления
     * @param createOrUpdateCommentDto данные для обновления комментария
     * @param createdAt                дата и время создания комментария
     * @param user                     пользователь, обновляющий комментарий
     * @return объект `Comment`
     */
    @Mapping(target = "id", source = "commentId")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "ad", source = "ad")
    Comment updateCommentDtoToEntity(Ad ad, Integer commentId,
                                                     CreateOrUpdateCommentDto createOrUpdateCommentDto,
                                                     LocalDateTime createdAt, User user);

    /**
     * Преобразует список объектов `CommentDto` в объект `CommentsDto`.
     *
     * @param commentDtoList список комментариев
     * @return объект `CommentsDto`
     */
    default CommentsDto commentDtoListToCommentsDto(List<CommentDto> commentDtoList) {
        return new CommentsDto(commentDtoList.size(), commentDtoList);
    }
    /**
     * Преобразует объект `LocalDateTime` в тип `Long`, представляющий количество миллисекунд с 1970-01-01T00:00:00.
     *
     * @param localDateTime объект `LocalDateTime` для преобразования
     * @return количество миллисекунд с 1970-01-01T00:00:00Z
     */
    default Long getLongFromLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
