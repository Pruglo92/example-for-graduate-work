package ru.skypro.homework.controller;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.skypro.homework.TestContainerInitializer;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdsControllerTest extends TestContainerInitializer {

    @Autowired
    private AdRepository adRepository;
    @Autowired
    private AdService adService;
    @Autowired
    private AdMapper adMapper;

    private final static String description = "Ad2Description";
    private final static Integer price = 1002;
    private final static String title = "Ad1Title";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JSONObject jsonObject = new JSONObject();


    @Test
    @DisplayName("Получение объявлений авторизованного пользователя. Код ответа 200")
    void givenAdsDtoListAndAuthorizedUser_whenGetUserAds_thenReturnOk() throws Exception {

        List<AdDto> adsDtoList = adMapper.toAdsDto(adRepository.getAdsByUserId(1));
        mockMvc.perform(
                        get("/ads/me")
                                .header(HttpHeaders.AUTHORIZATION, "Basic " + HttpHeaders.encodeBasicAuth("user1@gmail.com", "password", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results[0].author").value(adsDtoList.get(0).author()))
                .andExpect(jsonPath("$.results[0].pk").value(adsDtoList.get(0).pk()))
                .andExpect(jsonPath("$.results[0].title").value(adsDtoList.get(0).title()))
                .andExpect(jsonPath("$.results[0].price").value(adsDtoList.get(0).price()))
                .andExpect(jsonPath("$.results[0].image").value(adsDtoList.get(0).image()))
                .andExpect(jsonPath("$.results.size()").value(adsDtoList.size()));
    }

    @Test
    @DisplayName("Получение объявлений неавторизованного пользователя. Код ответа 401")
    void givenUnauthorizedUser_whenGetUserAds_thenReturnIsUnauthorized() throws Exception {

        mockMvc.perform(
                        get("/ads/me")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Получение списка всех объявлений")
    void givenAsdDto_whenGetAllAds_thenReturnOk() throws Exception {
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
    void givenExtendedAdDtoAndAdIdAndAuthorizedUser_whenGetAdsInfo_thenReturnOk() throws Exception {
        ExtendedAdDto extendedAdDto = adMapper.toDto(adRepository.getAdById(1).orElseThrow());

        mockMvc.perform(
                        get("/ads/1")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "user1@gmail.com",
                                                "password", StandardCharsets.UTF_8))
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
    void givenUnauthorizedUser_whenGetAdsInfo_thenReturnIsUnauthorized() throws Exception {

        mockMvc.perform(
                        get("/ads/1")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "Unauthorized",
                                                "Unauthorized", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Запрос на получение информации об объявлении которого не существует, авторизованным юзером. Код ответа 404")
    void givenAuthorizedUserAndInvalidAdID_whenGetNonExistentAdsInfo_thenReturnIsNotFound() throws Exception {

        mockMvc.perform(
                        get("/ads/9999")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "user1@gmail.com",
                                                "password", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Изменение объявления создателем объявления. Код ответа 200")
    void givenJSONAndAdCreator_whenPatchAd_thenReturnIsOk() throws Exception {

        jsonObject.put("description", description);
        jsonObject.put("price", price);
        jsonObject.put("title", title);

        mockMvc.perform(
                        patch("/ads/1")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "user1@gmail.com",
                                                "password", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(jsonObject)))
                .andExpect(status().isOk());

        assertThat(adRepository.getAdById(1).orElseThrow().getDescription()).isEqualTo(description);
        assertThat(adRepository.getAdById(1).orElseThrow().getPrice()).isEqualTo(price);
        assertThat(adRepository.getAdById(1).orElseThrow().getTitle()).isEqualTo(title);
    }

    @Test
    @DisplayName("Изменение объявления не создателем объявления. Код ответа 403")
    void givenJSONAndAdNonCreator_whenPatchAd_isForbidden() throws Exception {

        jsonObject.put("description", description);
        jsonObject.put("price", price);
        jsonObject.put("title", title);

        mockMvc.perform(
                        patch("/ads/1")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "user2@gmail.com",
                                                "password", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(jsonObject)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Изменение объявления админом. Код ответа 200")
    void givenJSONAndRoleAdmin_whenPatchAd_thenReturnIsOk() throws Exception {

        jsonObject.put("description", description);
        jsonObject.put("price", price);
        jsonObject.put("title", title);

        mockMvc.perform(
                        patch("/ads/1")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "admin@gmail.com",
                                                "password", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(jsonObject)))
                .andExpect(status().isOk());

        assertThat(adRepository.getAdById(1).orElseThrow().getDescription()).isEqualTo(description);
        assertThat(adRepository.getAdById(1).orElseThrow().getPrice()).isEqualTo(price);
        assertThat(adRepository.getAdById(1).orElseThrow().getTitle()).isEqualTo(title);
    }

    @Test
    @DisplayName("Изменение несуществующего объявления админом. Код ответа 404")
    void givenWrongAdIdAndRoleAdmin_whenPatchNonExistentAd_thenReturnIsNotFound() throws Exception {

        jsonObject.put("description", description);
        jsonObject.put("price", price);
        jsonObject.put("title", title);

        mockMvc.perform(
                        patch("/ads/9999")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "admin@gmail.com",
                                                "password", StandardCharsets.UTF_8))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(jsonObject)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Изменение объявления неавторизованным пользователем. Код ответа 401")
    void givenJSONAndUnauthorizedUser_whenPatchAd_thenReturnIsUnauthorized() throws Exception {

        jsonObject.put("description", description);
        jsonObject.put("price", price);
        jsonObject.put("title", title);

        mockMvc.perform(
                        patch("/ads/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(jsonObject)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Обновление картинки объявления по id объявления, создателем объявления. Код ответа 200")
    void givenImageAndAdIdCreatorUser_whenUpdateAdImage_thenReturnIsOk() throws Exception {

        Integer adId = 1;
        ClassPathResource resource = new ClassPathResource("images/ad2-image.png");
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "ad2-image.png",
                MediaType.IMAGE_PNG_VALUE,
                resource.getInputStream().readAllBytes()
        );
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/ads/{id}/image", adId)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + HttpHeaders.encodeBasicAuth(
                                        "user1@gmail.com",
                                        "password", StandardCharsets.UTF_8))
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Image updated successfully"));

        assertEquals(adRepository.getAdById(adId).orElseThrow().getImage().getFileName(), "ad2-image.png");
    }

    @Test
    @DisplayName("Удаление объявления по идентификационному номеру создателем объявления. Код ответа 204")
    void givenAuthorizedUser_whenDeleteAd_thenReturnIsNoContent() throws Exception {

        mockMvc.perform(
                        delete("/ads/1")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "user1@gmail.com",
                                                "password", StandardCharsets.UTF_8)))
                .andExpect(status().isNoContent());

        assertThat(adRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Удаление объявления по идентификационному номеру администратором. Код ответа 204")
    void givenARoleAdmin_whenDeleteAd_thenReturnIsNoContent() throws Exception {

        mockMvc.perform(
                        delete("/ads/1")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "admin@gmail.com",
                                                "password", StandardCharsets.UTF_8)))
                .andExpect(status().isNoContent());

        assertThat(adRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Удаление объявления по идентификационному номеру юзером. Код ответа 404")
    void givenARoleUser_whenDeleteNonExistentAd_thenReturnIsNotFound() throws Exception {

        mockMvc.perform(
                        delete("/ads/9999")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "user2@gmail.com",
                                                "password", StandardCharsets.UTF_8)))
                .andExpect(status().isNotFound());

        assertThat(adRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Удаление объявления по идентификационному номеру админом. Код ответа 404")
    void givenARoleAdmin_whenDeleteNonExistentAd_thenReturnIsNoContent() throws Exception {

        mockMvc.perform(
                        delete("/ads/9999")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "admin@gmail.com",
                                                "password", StandardCharsets.UTF_8)))
                .andExpect(status().isNotFound());

        assertThat(adRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Удаление объявления по идентификационному номеру не создателем объявления. Код ответа 403")
    void givenANonCreator_whenDeleteAd_thenReturnIsForbidden() throws Exception {

        mockMvc.perform(
                        delete("/ads/1")
                                .header(HttpHeaders.AUTHORIZATION,
                                        "Basic " + HttpHeaders.encodeBasicAuth(
                                                "user2@gmail.com",
                                                "password", StandardCharsets.UTF_8)))
                .andExpect(status().isForbidden());

        assertThat(adRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Удаление объявления по идентификационному номеру неавторизованным пользователем. Код ответа 401")
    void givenAUnauthorizedUser_whenDeleteAd_thenReturnIsUnauthorized() throws Exception {

        mockMvc.perform(
                        delete("/ads/1"))
                .andExpect(status().isUnauthorized());

        assertThat(adRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Обновление картинки объявления по id объявления, администратором. Код ответа 200")
    void givenImageAndAdIdRoleAdmin_whenUpdateAdImage_thenReturnIsOk() throws Exception {
        Integer adId = 1;
        ClassPathResource resource = new ClassPathResource("images/ad2-image.png");
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "ad2-image.png",
                MediaType.IMAGE_PNG_VALUE,
                resource.getInputStream().readAllBytes()
        );
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/ads/{id}/image", adId)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + HttpHeaders.encodeBasicAuth(
                                        "admin@gmail.com",
                                        "password", StandardCharsets.UTF_8))
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Image updated successfully"));

        assertEquals(adRepository.getAdById(adId).orElseThrow().getImage().getFileName(), "ad2-image.png");
    }

    @Test
    @DisplayName("Обновление картинки объявления по id объявления, неавторизованным пользователем. Код ответа 401")
    void givenImageAndAdIdUnauthorizedUser_whenUpdateAdImage_thenReturnIsOk() throws Exception {

        Integer adId = 1;
        ClassPathResource resource = new ClassPathResource("images/ad2-image.png");
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "ad2-image.png",
                MediaType.IMAGE_PNG_VALUE,
                resource.getInputStream().readAllBytes()
        );
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/ads/{id}/image", adId)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))

                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        assertEquals(adRepository.getAdById(adId).orElseThrow().getImage().getFileName(), "ad1-image.png");
    }

    @Test
    @DisplayName("Обновление картинки объявления по id объявления, не создателем объявления. Код ответа 401")
    void givenImageAndAdIdNonCreatorUser_whenUpdateAdImage_thenReturnIsOk() throws Exception {

        Integer adId = 1;
        ClassPathResource resource = new ClassPathResource("images/ad2-image.png");
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "ad2-image.png",
                MediaType.IMAGE_PNG_VALUE,
                resource.getInputStream().readAllBytes()
        );
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/ads/{id}/image", adId)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))

                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        assertEquals(adRepository.getAdById(adId).orElseThrow().getImage().getFileName(), "ad1-image.png");
    }

    @Test
    @DisplayName("Добавление объявления юзером. Код ответа 200")
    void givenImageAndJSON_whenAddAd_thenReturnIsOk() throws Exception {

        ClassPathResource resource = new ClassPathResource("images/ad2-image.png");

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "ad2-image.png",
                MediaType.IMAGE_PNG_VALUE,
                resource.getInputStream().readAllBytes()
        );

        jsonObject.put("description", description);
        jsonObject.put("price", price);
        jsonObject.put("title", "user title");

        MockMultipartFile body = new MockMultipartFile(
                "properties",
                "jsonObject",
                MediaType.APPLICATION_JSON_VALUE,
                jsonObject.toJSONString().getBytes()
        );

        int adCount = adRepository.findAll().size();

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/ads")
                        .file(image)
                        .file(body)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + HttpHeaders.encodeBasicAuth(
                                        "user1@gmail.com",
                                        "password", StandardCharsets.UTF_8)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertThat(adRepository.findAll().size()).isEqualTo(adCount + 1);
        assertThat(adRepository.findAll().stream()
                .filter(ad -> ad.getTitle().equals("user title"))
                .findFirst().orElseThrow().getUser().getFirstName()).isEqualTo("User1FirstName");
    }

    @Test
    @DisplayName("Добавление объявления админом. Код ответа 200")
    void givenImageAndJSONRoleAdmin_whenAddAd_thenReturnIsOk() throws Exception {

        ClassPathResource resource = new ClassPathResource("images/ad2-image.png");

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "ad2-image.png",
                MediaType.IMAGE_PNG_VALUE,
                resource.getInputStream().readAllBytes()
        );

        jsonObject.put("description", description);
        jsonObject.put("price", price);
        jsonObject.put("title", "admin title");

        MockMultipartFile body = new MockMultipartFile(
                "properties",
                "jsonObject",
                MediaType.APPLICATION_JSON_VALUE,
                jsonObject.toJSONString().getBytes()
        );

        int adCount = adRepository.findAll().size();

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/ads")
                        .file(image)
                        .file(body)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + HttpHeaders.encodeBasicAuth(
                                        "admin@gmail.com",
                                        "password", StandardCharsets.UTF_8)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertThat(adRepository.findAll().size()).isEqualTo(adCount + 1);
        assertThat(adRepository.findAll().stream()
                .filter(ad -> ad.getTitle().equals("admin title"))
                .findFirst().orElseThrow().getUser().getFirstName()).isEqualTo("AdminFirstName");
    }

    @Test
    @DisplayName("Добавление объявления неавторизованным пользователем. Код ответа 401")
    void givenImageAndJSONUnauthorizedUser_whenAddAd_thenReturnIsUnauthorized() throws Exception {

        ClassPathResource resource = new ClassPathResource("images/ad2-image.png");

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "ad2-image.png",
                MediaType.IMAGE_PNG_VALUE,
                resource.getInputStream().readAllBytes()
        );

        jsonObject.put("description", description);
        jsonObject.put("price", price);
        jsonObject.put("title", title);

        MockMultipartFile body = new MockMultipartFile(
                "properties",
                "jsonObject",
                MediaType.APPLICATION_JSON_VALUE,
                jsonObject.toJSONString().getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/ads")
                        .file(image)
                        .file(body)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}