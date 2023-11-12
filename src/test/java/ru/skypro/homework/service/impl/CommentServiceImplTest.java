package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exceptions.AdNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdRepository adRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private CommentServiceImpl commentService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //Случай, когда комментарий успешно добавлен.
    @Test
    void addCommentToAd_ValidInput_CommentAdded() throws AdNotFoundException, UsernameNotFoundException {
        // Arrange
        Integer adId = 1;
        CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto("Заголовок комментария");
        CommentDto expectedCommentDto = new CommentDto(1, "/image", "имя", 1L, 1, "Текст комментария");
        Comment expectedComment = new Comment();
        Ad ad = new Ad();

        Authentication authentication = mock(Authentication.class);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(adRepository.findById(adId)).thenReturn(Optional.of(ad));
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.empty());
        when(commentMapper.createCommentDtoToEntity(eq(ad), eq(createOrUpdateCommentDto), isNull())).thenReturn(expectedComment);
        when(commentMapper.entityToCommentDto(expectedComment)).thenReturn(expectedCommentDto);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);

        // Act
        CommentDto actualCommentDto = commentService.addCommentToAd(adId, createOrUpdateCommentDto);

        // Assert
        assertNotNull(actualCommentDto);
        assertEquals(expectedCommentDto, actualCommentDto);
        verify(commentRepository, times(1)).save(expectedComment);
    }

    //Случай, когда объявление не найдено.
    @Test
    void addCommentToAd_AdNotFound_ExceptionThrown() {
        // Arrange
        Integer adId = 1;
        CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto("Заголовок комментария");
        when(adRepository.findById(adId)).thenReturn(Optional.empty());

        // Act / Assert
        assertThrows(AdNotFoundException.class, () ->
                commentService.addCommentToAd(adId, createOrUpdateCommentDto));
        verify(commentRepository, never()).save(any());
    }

    //Случай, когда пользователь не найден.
    @Test
    void addCommentToAd_UserNotFound_ExceptionThrown() throws AdNotFoundException {
        // Arrange
        Integer adId = 1;
        CreateOrUpdateCommentDto createOrUpdateCommentDto = new CreateOrUpdateCommentDto("Заголовок комментария");
        Ad ad = new Ad();
        Authentication authentication = mock(Authentication.class);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(adRepository.findById(adId)).thenReturn(Optional.of(ad));
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.empty());
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        // Act / Assert
        assertThrows(UsernameNotFoundException.class, () ->
                commentService.addCommentToAd(adId, createOrUpdateCommentDto));
        verify(commentRepository, never()).save(any());
    }
}