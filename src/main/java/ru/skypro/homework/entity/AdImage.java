package ru.skypro.homework.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Класс AdImage представляет изображение, связанное с объявлением.
 */
@Entity
@Table(name = "ad_images")
public class AdImage extends Image {
}
