package ru.skypro.homework.controller;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.skypro.homework.TestContainerInitializer;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdsControllerTest extends TestContainerInitializer {

    @Autowired
    private AdRepository adRepository;
    @Autowired
    private AdService adService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdMapper adMapper;

    String description = "Ad1Description";
    Integer price = 1002;
    String title = "Ad1Title";

    @Test
    void getAllAdsTestX() throws Exception {

        MockHttpServletResponse responsePost = mockMvc
                .perform(get("/ads")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(200);
    }

    @Test
    @WithMockUser(authorities = "USER")
    void getAdByIdByUserTest2() throws Exception {

        MockHttpServletResponse responsePost = mockMvc
                .perform(get("/ads/1")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(200);
    }

    @Test
    @WithMockUser(authorities = "USER")
    void getAdByIdByUserTest3() throws Exception {
        MockHttpServletResponse responsePost2 = mockMvc
                .perform(get("/ads/1100")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost2.getStatus()).isEqualTo(404);
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getAdByIdByAdminTest() throws Exception {

        MockHttpServletResponse responsePost = mockMvc
                .perform(get("/ads/1")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(200);

    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getAdByIdByAdminTest2() throws Exception {

        MockHttpServletResponse responsePost2 = mockMvc
                .perform(get("/ads/11")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost2.getStatus()).isEqualTo(404);
    }

    @Test
    void getAd_ById_ByUnauthorizedUser_Test() throws Exception {

        MockHttpServletResponse responsePost = mockMvc
                .perform(get("/ads/1")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(401);
    }

    @Test
    @WithMockUser(value = "user2@gmail.com")
    void updateAdCorrectUserTest() throws Exception {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("description", description);
        jsonObject.put("price", price);
        jsonObject.put("title", title);

        MockHttpServletResponse responsePost = mockMvc
                .perform(patch("/ads/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                )
                .andDo(print())
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(200);

        assertThat(adService.getAdById(2).description()).isEqualTo(description);
        assertThat(adService.getAdById(2).price()).isEqualTo(price);
        assertThat(adService.getAdById(2).title()).isEqualTo(title);
    }

    @Test
    @WithMockUser(value = "user2@gmail.com")
    void updateAdWrongUserTest() throws Exception {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("description", description);
        jsonObject.put("price", price);
        jsonObject.put("title", title);

        MockHttpServletResponse responsePost = mockMvc
                .perform(patch("/ads/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(403);
    }

    @Test
    @WithMockUser(value = "user2@gmail.com")
    void updateAdNotFoundTest() throws Exception {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("description", description);
        jsonObject.put("price", price);
        jsonObject.put("title", title);

        MockHttpServletResponse responsePost = mockMvc
                .perform(patch("/ads/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(404);
    }

    @Test
    void updateAdUnauthorizedUserTest() throws Exception {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("description", description);
        jsonObject.put("price", price);
        jsonObject.put("title", title);

        MockHttpServletResponse responsePost = mockMvc
                .perform(patch("/ads/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(401);
    }

    @Test
    @WithMockUser(value = "user2@gmail.com")
    void getAuthorizedUserAdsTest() throws Exception {

        MockHttpServletResponse responsePost = mockMvc
                .perform(get("/ads/me")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(200);
    }

    @Test
    void getUnauthorizedUserAdsTest() throws Exception {

        MockHttpServletResponse responsePost = mockMvc
                .perform(get("/ads/me")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(401);
    }

    @WithMockUser(value = "user2@gmail.com")
    @Test
    void removeAdByWrongUserTest() throws Exception {
        assertThat(adService.getAllAds().count()).isEqualTo(2);

        MockHttpServletResponse responsePost = mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/ads/1")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(403);

        assertThat(adService.getAllAds().count()).isEqualTo(2);
    }


    @WithMockUser(authorities = "ADMIN")
    @Test
    void removeAdByAdminTest() throws Exception {
        assertThat(adService.getAllAds().count()).isEqualTo(2);

        MockHttpServletResponse responsePost = mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/ads/2")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(204);

        assertThat(adService.getAllAds().count()).isEqualTo(1);
    }

    @WithMockUser(value = "user1@gmail.com")
    @Test
    void removeAdByCorrectUserTest() throws Exception {
        assertThat(adService.getAllAds().count()).isEqualTo(2);

        MockHttpServletResponse responsePost = mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/ads/1")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(204);

        assertThat(adService.getAllAds().count()).isEqualTo(1);
    }

    //================================ТЕСТЫ БЕЗ ИСПОЛЬЗОВАНИЯ @WithMockUser======================================

    @Test
    @DisplayName("Экспериментальный тест")
    void experimentalTest() throws Exception {
        UserDto userDto = userMapper.toDto(userRepository.findByLogin("user1@gmail.com").orElseThrow());

        mockMvc.perform(
                        get("/users/me")
                                .header(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("user1@gmail.com", "password", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.email").value(userDto.email()))
                .andExpect(jsonPath("$.firstName").value(userDto.firstName()))
                .andExpect(jsonPath("$.lastName").value(userDto.lastName()))
                .andExpect(jsonPath("$.phone").value(userDto.phone()))
                .andExpect(jsonPath("$.role").value(userDto.role().toString()))
                .andExpect(jsonPath("$.image").value(userDto.image()));
    }

    @Test
    @DisplayName("Получение списка всех объявлений")
    void getAllAdsTest() throws Exception {
        AdsDto allAdsDtoList = adService.getAllAds();

        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(allAdsDtoList.count()))
                .andExpect(jsonPath("$.results[0].author").value(allAdsDtoList.results().get(0).author()))
                .andExpect(jsonPath("$.results[0].image").value(allAdsDtoList.results().get(0).image()))
                .andExpect(jsonPath("$.results[0].pk").value(allAdsDtoList.results().get(0).pk()))
                .andExpect(jsonPath("$.results[0].price").value(allAdsDtoList.results().get(0).price()))
                .andExpect(jsonPath("$.results[0].title").value(allAdsDtoList.results().get(0).title()))
                .andExpect(jsonPath("$.results[1].author").value(allAdsDtoList.results().get(1).author()))
                .andExpect(jsonPath("$.results[1].image").value(allAdsDtoList.results().get(1).image()))
                .andExpect(jsonPath("$.results[1].pk").value(allAdsDtoList.results().get(1).pk()))
                .andExpect(jsonPath("$.results[1].price").value(allAdsDtoList.results().get(1).price()))
                .andExpect(jsonPath("$.results[1].title").value(allAdsDtoList.results().get(1).title()));
    }

    @Test
    @DisplayName("Получение информации об объявлении по идентификатору, авторизованным юзером. Код ответа 200")
    void getAdById_ForUserTest() throws Exception {
        ExtendedAdDto extendedAdDto = adMapper.toDto(adRepository.getAdById(1).orElseThrow());

        mockMvc.perform(
                        get("/ads/1")
                                .header(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("user1@gmail.com", "password", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorFirstName").value(extendedAdDto.authorFirstName()))
                .andExpect(jsonPath("$.authorLastName").value(extendedAdDto.authorLastName()))
                .andExpect(jsonPath("$.description").value(extendedAdDto.description()))
                .andExpect(jsonPath("$.email").value(extendedAdDto.email()))
                .andExpect(jsonPath("$.image").value(extendedAdDto.image()))
                .andExpect(jsonPath("$.phone").value(extendedAdDto.phone()))
                .andExpect(jsonPath("$.price").value(extendedAdDto.price()))
                .andExpect(jsonPath("$.title").value(extendedAdDto.title()));
    }

    @Test
    @DisplayName("Запрос на получение информации об объявлении по идентификатору, неавторизованным юзером. Код ответа 401")
    void getAdById_ForUnauthorizedUserTest() throws Exception {
        mockMvc.perform(
                        get("/ads/1")
                                .header(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("Unauthorized", "Unauthorized", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Запрос на получение информации об объявлении которого не существует, авторизованным юзером. Код ответа 404")
    void getNotFoundAdById_ForUnauthorizedUserTest() throws Exception {
        mockMvc.perform(
                        get("/ads/9999")
                                .header(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("user1@gmail.com", "password", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Изменение объявления создателем объявления. Код ответа 200")
    void updateAd_CorrectUserTest() throws Exception {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("description", description);
        jsonObject.put("price", price);
        jsonObject.put("title", title);

        mockMvc.perform(
                        patch("/ads/1")
                                .header(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("user1@gmail.com", "password", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(jsonObject)))
                .andExpect(status().isOk());

        assertThat(adRepository.getAdById(1).orElseThrow().getDescription()).isEqualTo(description);
        assertThat(adRepository.getAdById(1).orElseThrow().getPrice()).isEqualTo(price);
        assertThat(adRepository.getAdById(1).orElseThrow().getTitle()).isEqualTo(title);
    }

    @Test
    @DisplayName("Изменение объявления не создателем объявления. Код ответа 403")
    void updateAd_WrongUserTest() throws Exception {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("description", description);
        jsonObject.put("price", price);
        jsonObject.put("title", title);

        mockMvc.perform(
                        patch("/ads/1")
                                .header(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("user2@gmail.com", "password", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(jsonObject)))
                .andExpect(status().isForbidden());
    }

}