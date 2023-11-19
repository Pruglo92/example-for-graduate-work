package ru.skypro.homework.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.skypro.homework.TestContainerInitializer;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

class ImageControllerTest extends TestContainerInitializer {

    @Test
    @DisplayName("Проверка на получение картинки, когда картинка с данным именем существует")
    void givenImageName_whenGetImage_thenReturnThisImage() throws Exception {
        String imageName = "user1-avatar.jpg";
        ClassPathResource imageResource = new ClassPathResource("images/" + imageName);

        mockMvc.perform(MockMvcRequestBuilders.get("/images/{name}", imageName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(content().bytes(imageResource.getInputStream().readAllBytes()));
    }

    @Test
    @DisplayName("Проверка на получение картинки, когда картинка с данным именем не существует")
    void givenImageName_whenGetImage_thenReturnNotFound() throws Exception {
        String imageName = "not_present_image.jpg";

        mockMvc.perform(MockMvcRequestBuilders.get("/images/{name}", imageName))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(content().string("Не найдено изображение по данному пути!"));
    }
}