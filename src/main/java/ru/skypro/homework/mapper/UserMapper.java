package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;

@Mapper
public abstract class UserMapper {

    @Autowired
    private PasswordEncoder encoder;

    /**
     * Преобразует объект `RegisterDto` в объект `User`.
     *
     * @param registerDto данные для регистрации
     * @return объект `User`
     */
    @Mapping(target = "login", source = "username")
    @Mapping(target = "password", expression = "java(encryptPassword(registerDto))")
    public abstract User toEntity(RegisterDto registerDto);

    /**
     * Преобразует объект `User` в объект `UserDto`.
     *
     * @param user объект `User` для преобразования
     * @return объект `UserDto`
     */
    @Mapping(target = "email", source = "login")
    @Mapping(target = "image", source = "userImage.filePath")
    public abstract UserDto toDto(User user);

    /**
     * Преобразует объект `UpdateUserDto` в существующий объект `User`.
     *
     * @param updateUser данные для обновления профиля
     * @param user       существующий объект `User`
     */
    public abstract void updateUserFromDto(@MappingTarget User user, UpdateUserDto updateUser);

    /**
     * Преобразует объект `User` в объект `UpdateUserDto`.
     *
     * @param user объект `User` для преобразования
     * @return объект `UpdateUserDto`
     */
    public abstract UpdateUserDto updateUserDtoFromUser(User user);

    /**
     * Шифрует пароль, используя `PasswordEncoder`.
     *
     * @param registerDto данные для регистрации
     * @return зашифрованный пароль
     */
    protected String encryptPassword(RegisterDto registerDto) {
        return encoder.encode(registerDto.password());
    }
}