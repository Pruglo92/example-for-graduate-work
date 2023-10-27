package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;


@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final AdRepository adRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;


    @Override
    public CommentDto addCommentToAd(Integer adId, CreateOrUpdateCommentDto createOrUpdateCommentDto, Authentication authentication) {
        Comment comment = commentMapper.createOrUpdateCommentDtoToEntity(adId, createOrUpdateCommentDto, authentication);
        commentRepository.save(comment);
        return commentMapper.entityToCommentDto(comment, authentication);
    }

    public void removeComment(Integer adId, Integer commentId, Authentication authentication) {
        Comment comment = commentMapper.deleteCommentDtoToEntity(adId,commentId,authentication);
        if (comment == null) {
            throw new CommentNotFoundException();
        }
            commentRepository.delete(comment);
    }


}

