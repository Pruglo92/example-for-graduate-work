package ru.skypro.homework.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.entity.Image;

public interface ImageService {

    <T extends Image> T updateImage(MultipartFile file, T image);

    Resource getImageFromFile(String imageName);
}