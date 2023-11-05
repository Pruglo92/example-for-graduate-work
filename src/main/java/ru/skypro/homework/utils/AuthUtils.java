package ru.skypro.homework.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.UserRepository;

public final class AuthUtils {

    private AuthUtils() {
    }

    public static User getUserFromAuthentication(final UserRepository userRepository) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByLogin(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }
}