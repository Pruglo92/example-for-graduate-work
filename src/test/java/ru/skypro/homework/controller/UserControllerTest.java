package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.skypro.homework.TestContainerInitializer;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.repository.UserRepository;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class UserControllerTest extends TestContainerInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String email = "test@test.ru";
    private static final String currentEmail = "user1@gmail.com";
    private static final String password = "12345678";

    private static final String currentPassword = "password";
    private static final String firstName1 = "FirstNameTest1";
    private static final String lastName1 = "LastNameTest1";
    private static final String phone = "+7 (999) 999-99-99";

    @Test
    @DisplayName("Проверка обновления пароля, когда пользователь существует и пароль верный")
    void givenNewPasswordDtoAndAuthorizedUser_whenChangePassword_thenReturnIsOk() throws Exception {

        NewPasswordDto newPasswordDto = new NewPasswordDto(currentPassword, password);
        String newPasswordDtoString = mapper.writeValueAsString(newPasswordDto);

        // Проверка, что соответствующий пользователь есть в базе и у него исходный пароль
        assertThat(userRepository.findByLogin(currentEmail))
                .isPresent()
                .get()
                .satisfies(currentUser -> {
                    assertTrue(encoder.matches(currentPassword, currentUser.getPassword()));
                });

        MockHttpServletResponse responsePost = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/users/set_password")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + HttpHeaders.encodeBasicAuth(
                                        currentEmail, currentPassword, StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPasswordDtoString))
                .andReturn().getResponse();

        // Проверка, что отдан успешный код ответа
        assertThat(responsePost.getStatus()).isEqualTo(204);

        // Проверка, что у пользователя в базе обновился пароль
        assertThat(userRepository.findByLogin(currentEmail))
                .isPresent()
                .get()
                .satisfies(currentUser -> {
                    assertTrue(encoder.matches(password, currentUser.getPassword()));
                });
    }

    @Test
    @DisplayName("Проверка обновления пароля, когда пользователь существует но пароль неверный")
    void givenNewPasswordDtoAndAuthorizedUserWithWrongPassword_whenChangePassword_thenReturnUnauthorized() throws Exception {

        NewPasswordDto newPasswordDto = new NewPasswordDto(password, password);
        String newPasswordDtoString = mapper.writeValueAsString(newPasswordDto);

        // Проверка, что соответствующий пользователь есть в базе и у него исходный пароль
        assertThat(userRepository.findByLogin(currentEmail))
                .isPresent()
                .get()
                .satisfies(currentUser -> {
                    assertTrue(encoder.matches(currentPassword, currentUser.getPassword()));
                });

        MockHttpServletResponse responsePost = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/users/set_password")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + HttpHeaders.encodeBasicAuth(
                                        currentEmail, currentPassword, StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPasswordDtoString))
                .andReturn().getResponse();

        // Проверка, что отдан успешный код ответа
        assertThat(responsePost.getStatus()).isEqualTo(204);

        // Проверка, что у пользователя в базе обновился пароль
        assertThat(userRepository.findByLogin(currentEmail))
                .isPresent()
                .get()
                .satisfies(currentUser -> {
                    assertTrue(encoder.matches(password, currentUser.getPassword()));
                });
    }


}
