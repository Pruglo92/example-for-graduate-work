package ru.skypro.homework.controller;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.skypro.homework.TestContainerInitializer;
import ru.skypro.homework.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AuthControllerTest extends TestContainerInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    String username1 = "test@test.ru";
    String password = "password";
    String firstName1 = "FirstName1";
    String lastName1 = "LastName1";
    String phone = "+7 (000) 000-00-00";
    String user = "USER";
    Boolean expectedTrueValue = true;

    @Test
    void test() {
        assertTrue(userRepository.existsByLogin("user1@gmail.com"));
    }

    @Test
    void test1() {
        assertTrue(userRepository.existsByLogin("user2@gmail.com"));
    }

    @Test
    void registerTest() throws Exception {
        JSONObject userJsonObject = new JSONObject();
        userJsonObject.put("username", username1);
        userJsonObject.put("password", password);
        userJsonObject.put("firstName", firstName1);
        userJsonObject.put("lastName", lastName1);
        userJsonObject.put("phone", phone);
        userJsonObject.put("role", user);

        // Регистрация нового пользователя
        MockHttpServletResponse responsePost = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJsonObject.toString())
                )
                .andReturn()
                .getResponse();

        // Проверка, что отдан успешный код ответа
        assertThat(responsePost.getStatus()).isEqualTo(201);

        // Проверка, что пользователь добавился в базу
        assertThat(userRepository.findByLogin(username1))
                .isPresent()
                .get()
                .satisfies(newUser -> {
                    assertThat(newUser.getId()).isEqualTo(4L);
                    Boolean actualValue = encoder.matches(password, newUser.getPassword());
                    // assertThat(actualValue.isEqualTo(expectedTrueValue));
                    assertThat(newUser.getFirstName()).isEqualTo(firstName1);
                    assertThat(newUser.getLastName()).isEqualTo(lastName1);
                    assertThat(newUser.getPhone()).isEqualTo(phone);
                    // assertThat(newUser.getRole()).isEqualTo(user);
                });
    }
}
