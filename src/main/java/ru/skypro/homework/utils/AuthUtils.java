package ru.skypro.homework.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.repository.UserRepository;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class AuthUtils {

    private final UserRepository userRepository;

    public User getUserFromAuthentication(final UserRepository userRepository) {
        log.info("Was invoked method for : getUserFromAuthentication");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByLogin(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }
}