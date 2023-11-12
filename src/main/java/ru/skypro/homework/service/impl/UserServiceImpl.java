package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UpdateUserDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.entity.UserImage;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.utils.AuthUtils;

/**
 * Реализация сервиса пользователей.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder encoder;
    private final UserMapper userMapper;
    private final ImageService imageService;
    private final UserRepository userRepository;
    private final AuthUtils authUtils;

    /**
     * Устанавливает новый пароль для текущего пользователя.
     *
     * @param currentPassword текущий пароль
     * @param newPassword     новый пароль
     * @throws BadCredentialsException если текущий пароль неверен
     */
    @Override
    public void setPassword(final String currentPassword, final String newPassword) {
        log.info("Was invoked method : setPassword");

        User user = authUtils.getUserFromAuthentication(userRepository);
        if (encoder.matches(currentPassword, user.getPassword())) {
            user.setPassword(encoder.encode(newPassword));
            userRepository.save(user);
        }
    }

    /**
     * Возвращает данные авторизованного пользователя.
     *
     * @return данные авторизованного пользователя
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public UserDto getAuthorizedUser() {
        log.info("Was invoked method for : getAuthorizedUser");

        return userMapper.toDto(authUtils.getUserFromAuthentication(userRepository));
    }

    /**
     * Обновляет данные пользователя.
     *
     * @param updateUser данные для обновления пользователя
     * @return обновленные данные пользователя
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public UpdateUserDto updateUser(final UpdateUserDto updateUser) {
        log.info("Was invoked method for : updateUser");

        User user = authUtils.getUserFromAuthentication(userRepository);
        userMapper.updateUserFromDto(user, updateUser);
        var updatedUser = userRepository.save(user);
        return userMapper.updateUserDtoFromUser(updatedUser);
    }

    /**
     * Обновляет изображение пользователя.
     *
     * @param file файл изображения
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public void updateUserImage(final MultipartFile file) {
        log.info("Was invoked method for : UpdateUserImage");

        User user = authUtils.getUserFromAuthentication(userRepository);
        UserImage image = (UserImage) imageService.updateImage(file, new UserImage());
        user.setUserImage(image);
        userRepository.save(user);
    }
}