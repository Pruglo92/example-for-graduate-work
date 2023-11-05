package ru.skypro.homework.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Абстрактный класс, представляющий изображение.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class Image extends BaseEntity {

    /**
     * Имя файла изображения.
     */
    @Column(name = "file_name")
    private String fileName;

    /**
     * Размер файла изображения.
     */
    @Column(name = "file_size")
    private Long fileSize;

    /**
     * Тип медиа изображения.
     */
    @Column(name = "media_type")
    private String mediaType;

    /**
     * Путь к файлу изображения.
     */
    @Column(name = "file_path", nullable = false)
    private String filePath;
}