package ru.skypro.homework.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.skypro.homework.TestContainerInitializer;
import ru.skypro.homework.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

public class UserControllerTest extends TestContainerInitializer {

    @Autowired
    private UserRepository userRepository;

    @Test
    void getUserTest() {
        assertThat(userRepository.findByLogin("admin@gmail.com"))
                .isPresent()
                .get()
                .satisfies(newUser -> assertThat(newUser.getId()).isEqualTo(3L));

    }
}
