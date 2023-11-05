package ru.skypro.homework.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

/**
 * Класс Ad представляет сущность объявления, используемую в системе.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ads")

public class Ad extends BaseEntity {
    /**
     * Цена объявления.
     */
    @Column(name = "price", nullable = false)
    private Integer price;
    /**
     * Заголовок объявления.
     */
    @Column(name = "title", nullable = false)
    private String title;
    /**
     * Описание объявления.
     */
    @Column(name = "description")
    private String description;
    /**
     * Пользователь, разместивший объявление.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    /**
     * Изображение объявления.
     */
    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private AdImage image;
    /**
     * Комментарии к объявлению.
     */
    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
}
