package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Image;
import ru.skypro.homework.exceptions.ImageNotFoundException;
import ru.skypro.homework.repository.ImageRepository;
import ru.skypro.homework.service.ImageService;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Сервис изображений.
 * Осуществляет операции обновления изображения и получения изображения из файла.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    @Value("${upload.dir}")
    private String uploadDir;

    @Value("${upload.prefix}")
    public String prefix;

    public static final String USER_DIR = "user.dir";

    private final ImageRepository imageRepository;

    /**
     * Обновляет изображение.
     *
     * @param file  файл изображения
     * @param image объект изображения
     * @return обновленное изображение
     */
    @Override
    public <T extends Image> T updateImage(final MultipartFile file, final T image) {
        log.info("Was invoked method for : updateImage");
        try {
            String fileName = file.getOriginalFilename();
            String projectRoot = System.getProperty(USER_DIR);
            Path path = Paths.get(projectRoot, prefix + uploadDir);

            Files.createDirectories(path);

            File imageFile = Paths.get(path.toString(), fileName).toFile();
            file.transferTo(imageFile);

            image.setFilePath(uploadDir + fileName);
            image.setFileSize(file.getSize());
            image.setMediaType(file.getContentType());
            image.setFileName(fileName);
            return imageRepository.save(image);
        } catch (IOException e) {
            throw new RuntimeException("Failed to update image: " + e.getMessage());
        }
    }

    /**
     * Получает изображение из файла.
     *
     * @param imageName имя изображения
     * @return ответ с изображением в виде ресурса
     * @throws ImageNotFoundException если изображение не найдено
     */
    @Override
    public Resource getImageFromFile(final String imageName) {
        log.info("Was invoked method for : getImageFromFile");
        String projectRoot = System.getProperty(USER_DIR);
        Path imagePath = Paths.get(projectRoot, prefix + uploadDir);
        File imageFile = Paths.get(imagePath.toString(), imageName).toFile();
        if (imageFile.exists()) {
            Path path = Paths.get(imageFile.getPath());
            UrlResource resource;
            try {
                resource = new UrlResource(path.toUri());
            } catch (MalformedURLException e) {
                log.error("Wrong imageFile path : {}", path);
                throw new RuntimeException("Неправильный путь к файлу");
            }

            if (resource.exists()) {
                log.debug("Image resource found for path: {}", imageFile.getPath());
                return resource;
            }
        }
        throw new ImageNotFoundException();
    }
}