package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.skypro.homework.TestContainerInitializer;
import ru.skypro.homework.dto.RegisterDto;
import ru.skypro.homework.enums.Role;
import ru.skypro.homework.repository.UserRepository;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.skypro.homework.enums.Role.USER;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerTest extends TestContainerInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String email = "test@test.ru";
    private static final String password = "12345678";
    private static final String firstName1 = "FirstNameTest1";
    private static final String lastName1 = "LastNameTest1";
    private static final String phone = "+7 (999) 999-99-99";
    private static final Role user = USER;
    private final Boolean expectedTrueValue = true;

    private static Stream<Arguments> registerTestNotValidParams() throws Exception {
        String regJsonStr1 = mapper.writeValueAsString(new RegisterDto("hi@", password, firstName1, lastName1, phone, user));
        String regJsonStr2 = mapper.writeValueAsString(new RegisterDto(email, "1234567", firstName1, lastName1, phone, user));
        String regJsonStr3 = mapper.writeValueAsString(new RegisterDto(email, password, "K", lastName1, phone, user));
        String regJsonStr4 = mapper.writeValueAsString(new RegisterDto(email, password, firstName1, "ф", phone, user));
        String regJsonStr5 = mapper.writeValueAsString(new RegisterDto(email, password, firstName1, lastName1, "8909500", user));

        return Stream.of(
                Arguments.of(regJsonStr1, 400),
                Arguments.of(regJsonStr2, 400),
                Arguments.of(regJsonStr3, 400),
                Arguments.of(regJsonStr4, 400),
                Arguments.of(regJsonStr5, 400)
        );
    }

    @ParameterizedTest
    @MethodSource("registerTestNotValidParams")
    @DisplayName("Проверка регистрации, когда одно из полей не валидно")
    public void registerWhenNotValidParamsTest(String registerJsonString, int expectedResponse) throws Exception {
        Long userNumberBefore = userRepository.count();
        // Попытка регистрации нового пользователя
        MockHttpServletResponse responsePost = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJsonString)
                )
                .andReturn()
                .getResponse();

        // Проверка, что отдан ожидаемый код ответа, и количество юзеров в базе не изменилось
        assertThat(responsePost.getStatus()).isEqualTo(expectedResponse);
        assertThat(userRepository.count()).isEqualTo(userNumberBefore);
    }

    @Test
    @DisplayName("Проверка регистрации, когда все поля валидны")
        //    @Order(3)
    void registerTest() throws Exception {
        RegisterDto newRegisterDto = new RegisterDto(email, password, firstName1, lastName1, phone, user);
        String registerJsonString = mapper.writeValueAsString(newRegisterDto);

        // Регистрация нового пользователя
        MockHttpServletResponse responsePost = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJsonString)
                )
                .andReturn()
                .getResponse();

        // Проверка, что отдан успешный код ответа
        assertThat(responsePost.getStatus()).isEqualTo(201);

        // Проверка, что пользователь добавился в базу
        assertThat(userRepository.findByLogin(email))
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

    @Test
    @DisplayName("Проверка регистрации, когда пользователь с идентичным логином уже зарегистрирован")
        //   @Order(3)
    void registerWhenTheSameLoginAlreadyExistsTest() throws Exception {
        RegisterDto newRegisterDto = new RegisterDto("user1@gmail.com", password, firstName1, lastName1, phone, user);
        String registerJsonString = mapper.writeValueAsString(newRegisterDto);

        // Регистрация нового пользователя
        MockHttpServletResponse responsePost = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerJsonString)
                )
                .andReturn()
                .getResponse();

        // Проверка, что отдан код ответа BAD_REQUEST
        assertThat(responsePost.getStatus()).isEqualTo(400);

        // Проверка, что в БД по-прежнему данные исходного юзера
        assertThat(userRepository.findByLogin("user1@gmail.com"))
                .isPresent()
                .get()
                .satisfies(newUser -> {
                    Boolean actualValue = encoder.matches(password, newUser.getPassword());
                    // assertThat(actualValue.isEqualTo(expectedTrueValue));
                    assertThat(newUser.getFirstName()).isNotEqualTo(firstName1);
                    assertThat(newUser.getLastName()).isNotEqualTo(lastName1);
                    assertThat(newUser.getPhone()).isNotEqualTo(phone);
                });
    }


}
