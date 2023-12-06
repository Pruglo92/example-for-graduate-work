package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.skypro.homework.TestContainerInitializer;
import ru.skypro.homework.dto.NewPasswordDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends TestContainerInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserMapper userMapper;

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
                .satisfies(currentUser ->
                    assertTrue(encoder.matches(currentPassword, currentUser.getPassword()))
                );

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
                .satisfies(currentUser ->
                    assertTrue(encoder.matches(password, currentUser.getPassword()))
                );
    }

    @Test
    @DisplayName("Проверка отклонения обновления пароля, когда логин верный, а пароль - нет")
    void givenNewPasswordDtoAndAuthorizedUserWithWrongPassword_whenChangePassword_thenReturnUnauthorized() throws Exception {

        NewPasswordDto newPasswordDto = new NewPasswordDto(password, password);
        String newPasswordDtoString = mapper.writeValueAsString(newPasswordDto);

        // Проверка, что login пользователя, который пытается авторизоваться, есть в базе
        assertTrue(userRepository
                .findByLogin(currentEmail)
                .isPresent()
        );

        MockHttpServletResponse responsePost = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/users/set_password")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + HttpHeaders.encodeBasicAuth(
                                        currentEmail, password, StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPasswordDtoString))
                .andReturn().getResponse();

        // Проверка, что отдан 401 код ответа
        assertThat(responsePost.getStatus()).isEqualTo(401);

        // Проверка, что у пользователя в базе не обновился пароль
        assertThat(userRepository.findByLogin(currentEmail))
                .isPresent()
                .get()
                .satisfies(currentUser ->
                    assertFalse(encoder.matches(password, currentUser.getPassword()))
                );
    }

    @Test
    @DisplayName("Проверка отклонения обновления пароля, когда пользователя не существует")
    void givenNewPasswordDtoAndAuthorizedUserWhichIsNotExist_whenChangePassword_thenReturnUnauthorized() throws Exception {

        NewPasswordDto newPasswordDto = new NewPasswordDto(currentPassword, password);
        String newPasswordDtoString = mapper.writeValueAsString(newPasswordDto);

        // Проверка, что пользователя, который пытается авторизоваться, нет в базе
        assertFalse(userRepository
                .findByLogin(email)
                .isPresent()
        );

        MockHttpServletResponse responsePost = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/users/set_password")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + HttpHeaders.encodeBasicAuth(
                                        email, currentPassword, StandardCharsets.UTF_8))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPasswordDtoString))
                .andReturn().getResponse();

        // Проверка, что отдан 401 ответа
        assertThat(responsePost.getStatus()).isEqualTo(401);
    }

    @Test
    @DisplayName("Проверка получения информации об авторизованном пользователе")
    void givenAuthorizedUser_whenGetAuthorizedUserDto_thenReturnOk() throws Exception {

        //Получаем UserDto существующего пользователя, напрямую из базы
        UserDto expectedUserDto = userMapper.toDto(
                userRepository
                        .findByLogin(currentEmail)
                        .orElseThrow());

        mockMvc.perform(
                        get("/users/me")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                currentEmail, currentPassword, StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON))
                //Проверяем, что отдан успешный код ответа и получены ожидаемые поля по авторизованному пользователю
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedUserDto.id()))
                .andExpect(jsonPath("$.email").value(expectedUserDto.email()))
                .andExpect(jsonPath("$.firstName").value(expectedUserDto.firstName()))
                .andExpect(jsonPath("$.lastName").value(expectedUserDto.lastName()))
                .andExpect(jsonPath("$.phone").value(expectedUserDto.phone()))
                .andExpect(jsonPath("$.role").value(expectedUserDto.role().toString()))
                .andExpect(jsonPath("$.image").value(expectedUserDto.image()));
    }

}
