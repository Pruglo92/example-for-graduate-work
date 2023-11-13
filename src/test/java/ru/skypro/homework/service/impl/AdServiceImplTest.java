package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.entity.*;
import ru.skypro.homework.enums.Role;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdServiceImplTest {

    @Mock
    private AdService adService;
    @Mock
    private ImageService imageService;
    @Mock
    private AdRepository adRepository;
    @Mock
    private AdMapper adMapper;

    @Test
    public void test1() {
        AdDto adDto1 = new AdDto(1, "image", 1, 100, "title title title");
        AdDto adDto2 = new AdDto(2, "image2", 2, 200, "title2 title2 title2");

        ArrayList<AdDto> list = new ArrayList<>(List.of(adDto1, adDto2));
        AdsDto adsDto = new AdsDto(2, list);

        when(adService.getAllAds()).thenReturn(adsDto);

        assertEquals(adsDto, adService.getAllAds());
    }

    @Test
    @WithMockUser(username = "ADMIN")
    public void test2() {
        AdDto adDto1 = new AdDto(1, "image", 1, 100, "title title title");

        AdImage adImage = null;
        User user = null;
        List<Comment> comments = null;
        Ad ad = new Ad(100, "title", "description", user, adImage, comments);

        when(adMapper.toAdDto(any())).thenReturn(adDto1);
        //when(adService.addAd(any(), any())).thenReturn(adDto1);


        assertEquals(adDto1, adService.addAd(any(), any()));
    }

    @Test
    public void test3() {
        AdDto adDto1 = new AdDto(1, "image", 1, 100, "title title title");
        AdDto adDto2 = new AdDto(2, "image2", 2, 200, "title2 title2 title2");

        UserImage userImage = new UserImage();
        AdImage adImage = new AdImage();
        List<Ad> listAds = new ArrayList<>();
        List<Comment> listComments = new ArrayList<>();
        List<AdDto> adDtoList = new ArrayList<>(List.of(adDto1, adDto2));

        User user = new User("login", "password", "firstName", "lastName", "7999999999", Role.USER, userImage, listAds, listComments);

        Ad ad1 = new Ad(100, "title", "description", user, adImage, listComments);
        Ad ad2 = new Ad(200, "title2", "description2", user, adImage, listComments);

        listAds.add(ad1);
        listAds.add(ad2);


        //ArrayList<AdDto> list = new ArrayList<>(List.of(adDto1, adDto2));

        when(adRepository.findAll()).thenReturn(listAds);
        AdsDto adsDto = new AdsDto(2, adDtoList);
        assertEquals(adsDto, adService.getAllAds());
    }


}