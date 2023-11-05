package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
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

import static ru.skypro.homework.utils.AuthUtils.getUserFromAuthentication;

/**
 * Реализация сервиса пользователей.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder encoder;
    private final UserMapper userMapper;
    private final ImageService imageService;
    private final UserRepository userRepository;

    /**
     * Устанавливает новый пароль для текущего пользователя.
     *
     * @param currentPassword текущий пароль
     * @param newPassword     новый пароль
     * @throws BadCredentialsException если текущий пароль неверен
     */
    @Override
    public void setPassword(final String currentPassword, final String newPassword) {
        User user = getUserFromAuthentication(userRepository);
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
        return userMapper.toDto(getUserFromAuthentication(userRepository));
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
        User user = getUserFromAuthentication(userRepository);
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
    public void UpdateUserImage(final MultipartFile file) {
        User user = getUserFromAuthentication(userRepository);
        UserImage image = (UserImage) imageService.updateImage(file, new UserImage());
        user.setUserImage(image);
        userRepository.save(user);
    }
}