package ru.skypro.homework.service;

import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;

public interface CommentService {

    CommentDto addCommentToAd(Integer adId,
                              CreateOrUpdateCommentDto createOrUpdateCommentDto);

    CommentDto updateCommentToAd(Integer adId,
                                 Integer commentId,
                                 CreateOrUpdateCommentDto createOrUpdateCommentDto);

    void removeComment(Integer adId, Integer commentId);

    CommentsDto getCommentsByAd(Integer adId);
}