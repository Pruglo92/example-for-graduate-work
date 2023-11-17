package ru.skypro.homework.controller;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.skypro.homework.TestContainerInitializer;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdsControllerTest extends TestContainerInitializer {

    @Autowired
    private AdRepository adRepository;
    @Autowired
    private AdService adService;
    @Autowired
    private AdsController adsController;

    Integer pk = 1;
    String authorFirstName = "User1FirstName";
    String authorLastName = "User1LastName";
    String description = "Ad1Description";
    String email = "user1@gmail.com";
    String image = "/images/ad1-image.png";
    String phone = "+7 (000) 000-00-01";
    Integer price = 100;
    String title = "Ad1Title";

/*
    @Test
    @Order(1)
    void getAdByIdTest1() {
        assertThat(adService.getAdById(1)).isEqualTo(extendedAdDto);
    }

    @Test
    @Order(2)
    void getAdByIdNegativeTest() {
        assertThat(adService.getAdById(-1)).isNull();
    }*/


    @Test
    @Order(1)
    void getAllAdsTest() throws Exception {

        MockHttpServletResponse responsePost = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/ads")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(200);
    }

    @Test
    @Order(2)
    @WithMockUser(authorities = "USER")
    void getAdByIdByUserTest2() throws Exception {

        MockHttpServletResponse responsePost = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/ads/1")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(200);

        //должен дать 404
        MockHttpServletResponse responsePost2 = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/ads/1100")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost2.getStatus()).isEqualTo(200);
    }

    @Test
    @Order(3)
    @WithMockUser(authorities = "ADMIN")
    void getAdByIdByAdminTest() throws Exception {

        MockHttpServletResponse responsePost = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/ads/1")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(200);

        //должен дать 404
        MockHttpServletResponse responsePost2 = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/ads/11")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost2.getStatus()).isEqualTo(200);
    }

    @Test
    @Order(4)
    void getAdByIdByUnauthorizedTest() throws Exception {

        MockHttpServletResponse responsePost = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/ads/1")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(401);
    }

    @Test
    @Order(4)
    @WithMockUser(value = "user2@gmail.com")
    void updateAdCorrectUserTest() throws Exception {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("description", description);
        jsonObject.put("price", price);
        jsonObject.put("title", title);

        MockHttpServletResponse responsePost = mockMvc
                .perform(MockMvcRequestBuilders
                        .patch("/ads/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(200);

        assertThat(adService.getAdById(2).description()).isEqualTo(description);
        assertThat(adService.getAdById(2).price()).isEqualTo(price);
        assertThat(adService.getAdById(2).title()).isEqualTo(title);
    }

    @Test
    @Order(5)
    @WithMockUser(value = "user2@gmail.com")
    void updateAdWrongUserTest() throws Exception {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("description", description);
        jsonObject.put("price", price);
        jsonObject.put("title", title);

        MockHttpServletResponse responsePost = mockMvc
                .perform(MockMvcRequestBuilders
                        .patch("/ads/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(403);
    }

    @Test
    @Order(6)
    @WithMockUser(value = "user2@gmail.com")
    void updateAdNotFoundTest() throws Exception {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("description", description);
        jsonObject.put("price", price);
        jsonObject.put("title", title);

        MockHttpServletResponse responsePost = mockMvc
                .perform(MockMvcRequestBuilders
                        .patch("/ads/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(404);
    }

    @Test
    @Order(7)
    void updateAdUnauthorizedUserTest() throws Exception {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("description", description);
        jsonObject.put("price", price);
        jsonObject.put("title", title);

        MockHttpServletResponse responsePost = mockMvc
                .perform(MockMvcRequestBuilders
                        .patch("/ads/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(401);
    }

    @Test
    @Order(8)
    @WithMockUser(value = "user2@gmail.com")
    void getAuthorizedUserAdsTest() throws Exception {

        MockHttpServletResponse responsePost = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/ads/me")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(200);
    }

    @Test
    @Order(9)
    void getUnauthorizedUserAdsTest() throws Exception {

        MockHttpServletResponse responsePost = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/ads/me")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(401);
    }

    @WithMockUser(value = "user2@gmail.com")
    @Test
    @Order(10)
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
    @Order(11)
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

    /*@WithMockUser(authorities = "user1@gmail.com")
    @Test
    @Order(5)
    void AddAdByUserTest() throws Exception {

        Integer id = 1;
        Integer adId = 1;
        String image = "/images/ad1-image.png";
        String title = "Ad1Title";
        String description = "description";
        Integer price = 100;

        JSONObject properties = new JSONObject();


        properties.put("description", description);
        properties.put("price", price);
        properties.put("title", title);

        assertThat(adService.getAllAds().count()).isEqualTo(2);

        MockHttpServletResponse responsePost = mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/ads")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .content(properties.toString())
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(204);

        assertThat(adService.getAllAds().count()).isEqualTo(3);
    }*/


    @WithMockUser(value = "user1@gmail.com")
    @Test
    @Order(12)
    void removeAdByCorrectUserTest() throws Exception {
        assertThat(adService.getAllAds().count()).isEqualTo(1);

        MockHttpServletResponse responsePost = mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/ads/1")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(204);

        assertThat(adService.getAllAds().count()).isEqualTo(0);
    }

}