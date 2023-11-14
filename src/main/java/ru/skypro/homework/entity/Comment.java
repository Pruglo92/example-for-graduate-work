package ru.skypro.homework.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * Класс, представляющий комментарий к объявлению.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comment extends BaseEntity {

    /**
     * Текст комментария.
     */
    @Column(name = "text", nullable = false)
    private String text;

    /**
     * Дата и время создания комментария.
     */
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Пользователь, оставивший комментарий.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Объявление, к которому оставлен комментарий.
     */
    @ManyToOne
    @JoinColumn(name = "ad_id", nullable = false)
    private Ad ad;
}