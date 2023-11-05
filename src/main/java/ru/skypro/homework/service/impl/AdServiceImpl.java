package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
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

import java.util.List;

import static ru.skypro.homework.utils.AuthUtils.getUserFromAuthentication;

/**
 * Сервис объявлений.
 * Осуществляет операции добавления, обновления, удаления и получения объявлений.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;
    private final ImageService imageService;

    /**
     * Удаляет объявление по его идентификатору.
     *
     * @param id идентификатор объявления
     */
    @Override
    @PreAuthorize("hasRole('ADMIN') or authentication.name == @adRepository.getAdById(#id).user.login")
    public void removeAd(Integer id) {
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
        User user = getUserFromAuthentication(userRepository);
        AdImage image = (AdImage) imageService.updateImage(file, new AdImage());

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
        return adMapper.toDto(adRepository.getAdById(id));
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
    public AdDto updateAd(Integer id, CreateOrUpdateAdDto createOrUpdateAdDto) throws AdNotFoundException {
        AdImage image = adRepository.getAdById(id).getImage();
        Ad ad = adMapper.updateAdDtoToAd(id, createOrUpdateAdDto, getUserByAdId(id), image);
        adRepository.save(ad);
        return adMapper.toAdDto(ad);
    }

    /**
     * Возвращает все объявления, принадлежащие авторизованному пользователю.
     *
     * @return объект AdsDto с количеством и списком объявлений авторизованного пользователя
     */
    @Override
    public AdsDto getAuthorizedUserAds() {
        User user = getUserFromAuthentication(userRepository);
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
    public void updateAdImage(Integer id, final MultipartFile file) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException(id));
        AdImage image = (AdImage) imageService.updateImage(file, new AdImage());
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
        List<Ad> list = adRepository.findAll();
        List<AdDto> adsDtoList = adMapper.toAdsDto(list);

        return new AdsDto(adsDtoList.size(), adsDtoList);
    }

    /**
     * Получает пользователя по идентификатору объявления.
     *
     * @param adId идентификатор объявления
     * @return объект User, владелец объявления
     * @throws AdNotFoundException если объявление не найдено
     */
    protected User getUserByAdId(Integer adId) {
        return adRepository.findById(adId)
                .orElseThrow(() ->
                        new AdNotFoundException(adId))
                .getUser();
    }
}