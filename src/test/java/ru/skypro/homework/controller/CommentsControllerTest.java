package ru.skypro.homework.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.skypro.homework.TestContainerInitializer;

class CommentsControllerTest extends TestContainerInitializer {

    @Test
    @DisplayName("Проверка удаления комментария в объявлении юзером")
    @WithMockUser(value = "user1@gmail.com")
    void givenAdIdAndCommentId_whenRemoveComment_thenCommentIsDeletedUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/ads/{adId}/comments/{commentId}", 1, 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Проверка удаления комментария в объявлении админом")
    @WithMockUser(authorities = "ADMIN")
    void givenAdIdAndCommentId_whenRemoveComment_thenCommentIsDeletedAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/ads/{adId}/comments/{commentId}", 1, 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Тест удаления комментария в объявлении: отказано в доступе")
    @WithMockUser(value = "user2@gmail.com")
    void givenAdIdAndCommentId_whenRemoveComment_thenAccessDenied() throws Exception {
        // Проверка статуса 403 (FORBIDDEN)
        mockMvc.perform(MockMvcRequestBuilders.delete("/ads/{adId}/comments/{commentId}", 1, 1))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("Тест удаления комментария в объявлении: неавторизованный доступ")
    void givenAdIdAndCommentId_whenRemoveComment_thenUnauthorizedAccess() throws Exception {
        // Проверка статуса 401 (UNAUTHORIZED)
        mockMvc.perform(MockMvcRequestBuilders.delete("/ads/{adId}/comments/{commentId}", 1, 1))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
    @Test
    @DisplayName("Тест удаления комментария в объявлении: отсутствует объявление")
    @WithMockUser(value = "user1@gmail.com")
    void givenAdIdAndCommentId_whenRemoveComment_thenNotFound() throws Exception {
        // Проверка статуса 404 (Not Found)
        mockMvc.perform(MockMvcRequestBuilders.delete("/ads/{adId}/comments/{commentId}", 999, 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    @DisplayName("Тест удаления комментария в объявлении: отсутствует объявление при наличии прав администратора")
    @WithMockUser(authorities = "ADMIN")
    void givenAdIdAndCommentIdAndAdminUser_whenRemoveComment_thenNotFound() throws Exception {
        // Проверка статуса 404 (Not Found)
        mockMvc.perform(MockMvcRequestBuilders.delete("/ads/{adId}/comments/{commentId}", 999, 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    @DisplayName("Получение комментариев объявления:авторизованный доступ")
    @WithMockUser(value = "user1@gmail.com")
    void givenAdId_whenGetCommentsByAd_thenCommentsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/ads/{adId}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Получение комментариев объявления: неавторизованный доступ")
    void givenAdId_whenGetCommentsByAd_thenCommentsReturned_isUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/ads/{adId}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @DisplayName("Добавление комментария объявления: авторизованный доступ")
    @WithMockUser(value = "user1@gmail.com")
    void givenAdIdAndRequestBody_whenAddComment_thenReturnAddedComment() throws Exception {
        String requestBody = "{\"text\": \"Новый комментарий\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/ads/{adId}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    @DisplayName("Добавление комментария объявления: не авторизованный доступ")
    void givenAdIdAndRequestBody_whenAddComment_thenReturnAddedComment_isUnauthorized() throws Exception {
        String requestBody = "{\"text\": \"Новый комментарий\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/ads/{adId}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
    @Test
    @DisplayName("Обновление комментария объявления: другим юзером")
    @WithMockUser(value = "user2@gmail.com")
    void givenCommentIdAndRequestBody_whenUpdateComment_thenReturnUpdatedComment_isForbidden() throws Exception {
        String requestBody = "{\"text\": \"Обновленный комментарий\"}";
        mockMvc.perform(MockMvcRequestBuilders.patch("/ads/{adId}/comments/{commentId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
    @Test
    @DisplayName("Обновление комментария объявления: авторизованный доступ")
    @WithMockUser(value = "user1@gmail.com")
    void givenCommentIdAndRequestBody_whenUpdateComment_thenReturnUpdatedComment() throws Exception {
        String requestBody = "{\"text\": \"Обновленный комментарий\"}";
        mockMvc.perform(MockMvcRequestBuilders.patch("/ads/{adId}/comments/{commentId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    @DisplayName("Обновление комментария объявления: админом")
    @WithMockUser(authorities = "ADMIN")
    void givenCommentIdAndRequestBody_whenUpdateComment_thenReturnUpdatedComment_isAdmin() throws Exception {
        String requestBody = "{\"text\": \"Обновленный комментарий\"}";
        mockMvc.perform(MockMvcRequestBuilders.patch("/ads/{adId}/comments/{commentId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    @DisplayName("Обновление комментария объявления: не авторизованный доступ")
    @WithMockUser(value = "user3@gmail.com")
    void givenCommentIdAndRequestBody_whenUpdateComment_thenReturnUpdatedComment_isUnauthorized() throws Exception {
        String requestBody = "{\"text\": \"Обновленный комментарий\"}";
        mockMvc.perform(MockMvcRequestBuilders.patch("/ads/{adId}/comments/{commentId}", 1, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
}
