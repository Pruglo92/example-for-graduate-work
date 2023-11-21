package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.dto.ExtendedAdDto;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.AdImage;
import ru.skypro.homework.entity.User;
import ru.skypro.homework.exceptions.AdNotFoundException;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.utils.AuthUtils;

import java.util.List;

/**
 * Сервис объявлений.
 * Осуществляет операции добавления, обновления, удаления и получения объявлений.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;
    private final ImageService imageService;
    private final AuthUtils authUtils;

    /**
     * Удаляет объявление по его идентификатору.
     *
     * @param id идентификатор объявления
     */
    @Override
    @PreAuthorize("hasRole('ADMIN') or authentication.name == @adRepository.getById(#id).getUser().login")
    public void removeAd(Integer id) {
        log.info("Was invoked method for : removeAd");

        adRepository.removeAdById(id);
    }

    /**
     * Добавляет новое объявление.
     *
     * @param createOrUpdateAdDto объект с данными для создания или обновления объявления
     * @param file                изображение для объявления
     * @return созданное объявление в виде объекта AdDto
     */
    @Override
    public AdDto addAd(CreateOrUpdateAdDto createOrUpdateAdDto, MultipartFile file) {
        log.info("Was invoked method for : addAd");

        User user = authUtils.getUserFromAuthentication(userRepository);
        AdImage image = imageService.updateImage(file, new AdImage());
        Ad ad = adMapper.createAdDtoToAd(createOrUpdateAdDto, user, image);
        adRepository.save(ad);

        return adMapper.toAdDto(ad);
    }

    /**
     * Возвращает расширенную информацию по объявлению по его идентификатору.
     *
     * @param id идентификатор объявления
     * @return объект ExtendedAdDto с расширенной информацией по объявлению
     */
    @Override
    public ExtendedAdDto getAdById(Integer id) {
        log.info("Was invoked method for : getAdById");

        return adMapper.toDto(adRepository.getAdById(id).orElseThrow(AdNotFoundException::new));
    }

    /**
     * Обновляет объявление по его идентификатору.
     *
     * @param id                  идентификатор объявления
     * @param createOrUpdateAdDto объект с данными для создания или обновления объявления
     * @return обновленное объявление в виде объекта AdDto
     * @throws AdNotFoundException если объявление не найдено
     */
    @Override
    @PreAuthorize("hasRole('ADMIN') or authentication.name == @adRepository.getById(#id).getUser().login")
    public AdDto updateAd(Integer id, CreateOrUpdateAdDto createOrUpdateAdDto) throws AdNotFoundException {
        log.info("Was invoked method for : updateAd");

        Ad ad = adRepository.getAdById(id).orElseThrow(AdNotFoundException::new);
        Ad updatedAd = adMapper.updateAdDtoToAd(ad, createOrUpdateAdDto);
        adRepository.save(updatedAd);
        return adMapper.toAdDto(updatedAd);
    }

    /**
     * Возвращает все объявления, принадлежащие авторизованному пользователю.
     *
     * @return объект AdsDto с количеством и списком объявлений авторизованного пользователя
     */
    @Override
    public AdsDto getAuthorizedUserAds() {
        log.info("Was invoked method for : getAuthorizedUserAds");

        User user = authUtils.getUserFromAuthentication(userRepository);
        List<Ad> list = adRepository.getAdsByUserId(user.getId());
        List<AdDto> adsDtoList = adMapper.toAdsDto(list);

        return new AdsDto(adsDtoList.size(), adsDtoList);
    }

    /**
     * Обновляет изображение объявления.
     *
     * @param id   идентификатор объявления
     * @param file новое изображение
     */
    @Override
    @PreAuthorize("hasRole('ADMIN') or authentication.name == @adRepository.getById(#id).getUser().login")
    public void updateAdImage(Integer id, final MultipartFile file) {
        log.info("Was invoked method for : updateAdImage");

        Ad ad = adRepository.findById(id)
                .orElseThrow(AdNotFoundException::new);
        AdImage image = imageService.updateImage(file, new AdImage());
        ad.setImage(image);
        adRepository.save(ad);
    }

    /**
     * Возвращает все объявления.
     *
     * @return объект AdsDto с количеством и списком всех объявлений
     */
    @Override
    public AdsDto getAllAds() {
        log.info("Was invoked method for : getAllAds");

        List<Ad> list = adRepository.findAll();
        List<AdDto> adsDtoList = adMapper.toAdsDto(list);

        return new AdsDto(adsDtoList.size(), adsDtoList);
    }
}