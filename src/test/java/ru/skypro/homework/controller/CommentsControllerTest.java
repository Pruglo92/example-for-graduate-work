package ru.skypro.homework.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.skypro.homework.TestContainerInitializer;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.service.CommentService;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class CommentsControllerTest extends TestContainerInitializer {

    @Mock
    private CommentService commentService;

    @Test
    @DisplayName("Проверка удаления комментария в объявлении юзером")
    @WithMockUser(value = "user1@gmail.com", roles = "USER")
    void givenAdIdAndCommentId_whenRemoveComment_thenCommentIsDeletedUser() throws Exception {
        commentService.removeComment(1, 1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/ads/{adId}/comments/{commentId}", 1, 1))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(commentService, times(1)).removeComment(1, 1);
    }

    @Test
    @DisplayName("Проверка удаления комментария в объявлении админом")
    @WithMockUser(authorities = "ADMIN")
    void givenAdIdAndCommentId_whenRemoveComment_thenCommentIsDeletedAdmin() throws Exception {
        commentService.removeComment(1, 1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/ads/{adId}/comments/{commentId}", 1, 1))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(commentService, times(1)).removeComment(1, 1);
    }

    @Test
    @DisplayName("Тест удаления комментария в объявлении: отказано в доступе")
    @WithMockUser(value = "user2@gmail.com", roles = "USER")
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
    @DisplayName("Получение комментариев объявления:авторизованный доступ")
    @WithMockUser(roles = "USER")
    void givenAdId_whenGetCommentsByAd_thenCommentsReturned() throws Exception {
        List<CommentDto> comments = Collections.emptyList();
        CommentsDto commentsDto = new CommentsDto(0, comments);

        when(commentService.getCommentsByAd(1)).thenReturn(commentsDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/ads/{adId}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Получение комментариев объявления: неавторизованный доступ")
    void givenAdId_whenGetCommentsByAd_thenCommentsReturned_isUnauthorized() throws Exception {
        List<CommentDto> comments = Collections.emptyList();
        CommentsDto commentsDto = new CommentsDto(0, comments);

        when(commentService.getCommentsByAd(1)).thenReturn(commentsDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/ads/{adId}/comments", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}