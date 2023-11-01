package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    /**
     * Возвращает список комментариев по идентификатору объявления.
     *
     * @param adId идентификатор объявления
     * @return список комментариев
     */
    List<Comment> findAllByAdId(Integer adId);
}
