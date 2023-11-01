package ru.skypro.homework.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.skypro.homework.enums.Role;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

/**
 * Класс, представляющий пользователя.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {
    /**
     * Логин пользователя.
     */
    @Email
    @Column(name = "login", nullable = false)
    private String login;

    /**
     * Пароль пользователя.
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Имя пользователя.
     */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /**
     * Фамилия пользователя.
     */
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /**
     * Телефон пользователя.
     */
    @Column(name = "phone", nullable = false)
    private String phone;

    /**
     * Роль пользователя.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    /**
     * Изображение пользователя.
     */
    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "user_image_id")
    private UserImage userImage;

    /**
     * Объявления пользователя.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ad> ads;

    /**
     * Комментарии пользователя.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

}
