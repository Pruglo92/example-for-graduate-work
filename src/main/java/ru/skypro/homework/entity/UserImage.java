package ru.skypro.homework.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Класс, представляющий изображение пользователя.
 */
@Entity
@Table(name = "user_images")
public class UserImage extends Image {
}