package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsDto;
import ru.skypro.homework.dto.CreateOrUpdateCommentDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.Comment;
import ru.skypro.homework.exceptions.AdNotFoundException;
import ru.skypro.homework.exceptions.CommentInconsistencyToAdException;
import ru.skypro.homework.exceptions.CommentNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.skypro.homework.utils.AuthUtils.getUserFromAuthentication;

/**
 * Сервис комментариев.
 * Осуществляет операции добавления, обновления, удаления и получения комментариев.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AdRepository adRepository;
    private final CommentMapper commentMapper;

    /**
     * Добавляет комментарий к объявлению.
     *
     * @param adId                     идентификатор объявления
     * @param createOrUpdateCommentDto данные для создания или обновления комментария
     * @return добавленный комментарий
     * @throws AdNotFoundException       если объявление не найдено
     * @throws UsernameNotFoundException если пользователя не найдено
     */
    @Override
    public CommentDto addCommentToAd(Integer adId,
                                     CreateOrUpdateCommentDto createOrUpdateCommentDto)
            throws AdNotFoundException, UsernameNotFoundException {
        log.info("Was invoked method for : addCommentToAd");

        Comment comment = commentMapper.createCommentDtoToEntity(
                getAdByAdId(adId),
                createOrUpdateCommentDto,
                getUserFromAuthentication(userRepository));
        commentRepository.save(comment);
        return commentMapper.entityToCommentDto(comment);
    }

    /**
     * Обновляет комментарий к объявлению.
     *
     * @param adId                     идентификатор объявления
     * @param commentId                идентификатор комментария
     * @param createOrUpdateCommentDto данные для создания или обновления комментария
     * @return обновленный комментарий
     * @throws CommentNotFoundException  если комментарий не найден
     * @throws AdNotFoundException       если объявление не найдено
     * @throws UsernameNotFoundException если пользователя не найдено
     */
    @Override
    public CommentDto updateCommentToAd(Integer adId, Integer commentId,
                                        CreateOrUpdateCommentDto createOrUpdateCommentDto)
            throws CommentNotFoundException, AdNotFoundException, UsernameNotFoundException, CommentInconsistencyToAdException {
        log.info("Was invoked method for : updateCommentToAd");

        if (adId.equals(getCommentByCommentId(commentId).getAd().getId())) {
            Comment comment = commentMapper.updateCommentDtoToEntity(
                    getAdByAdId(adId),
                    commentId,
                    createOrUpdateCommentDto,
                    findCommentLocalDateTime(commentId),
                    getUserFromAuthentication(userRepository));
            commentRepository.save(comment);
            return commentMapper.entityToCommentDto(comment);
        } else {
            throw new CommentInconsistencyToAdException();
        }
    }

    /**
     * Удаляет комментарий к объявлению.
     *
     * @param adId      идентификатор объявления
     * @param commentId идентификатор комментария
     * @throws CommentNotFoundException          если комментарий не найден
     * @throws CommentInconsistencyToAdException если комментарий не соответствует объявлению
     */
    @Override
    public void removeComment(Integer adId, Integer commentId)
            throws CommentNotFoundException, CommentInconsistencyToAdException {
        log.info("Was invoked method for : removeComment");

        if (adId.equals(getCommentByCommentId(commentId).getAd().getId())) {
            commentRepository.delete(getCommentByCommentId(commentId));
        } else {
            throw new CommentInconsistencyToAdException();
        }
    }

    /**
     * Получает все комментарии к объявлению.
     *
     * @param adId идентификатор объявления
     * @return список комментариев к объявлению
     */
    @Override
    public CommentsDto getCommentsByAd(Integer adId) {
        log.info("Was invoked method for : getCommentsByAd");

        List<CommentDto> commentsByAd = commentRepository.findAllByAdId(adId).stream()
                .map(commentMapper::entityToCommentDto).toList();
        return commentMapper.commentDtoListToCommentsDto(commentsByAd);
    }

    /**
     * Возвращает объявление по его идентификатору.
     *
     * @param adId идентификатор объявления
     * @return объявление
     * @throws AdNotFoundException если объявление не найдено
     */
    protected Ad getAdByAdId(Integer adId) {
        log.info("Was invoked method for : getAdByAdId");

        return adRepository.findById(adId)
                .orElseThrow(() ->
                        new AdNotFoundException(adId));
    }

    protected Comment getCommentByCommentId(Integer commentId) {
        log.info("Was invoked method for : getCommentByCommentId");

        return commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new CommentNotFoundException(commentId));
    }

    /**
     * Возвращает комментарий по его идентификатору.
     *
     * @param commentId идентификатор комментария
     * @return комментарий
     * @throws CommentNotFoundException если комментарий не найден
     */
    protected LocalDateTime findCommentLocalDateTime(Integer commentId) {
        log.info("Was invoked method for : findCommentLocalDateTime");

        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId))
                .getCreatedAt();
    }
}