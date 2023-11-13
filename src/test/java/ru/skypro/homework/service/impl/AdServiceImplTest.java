package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import ru.skypro.homework.dto.AdDto;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateOrUpdateAdDto;
import ru.skypro.homework.entity.*;
import ru.skypro.homework.enums.Role;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.utils.AuthUtils;

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
    @Mock
    private AuthUtils authUtils;

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
        User user = new User("login", "password", "firstName", "lastname", "7999999999", Role.USER, null, null, null);
        AdImage adImage = new AdImage();
        when(authUtils.getUserFromAuthentication(any())).thenReturn(user);
        when(imageService.updateImage(any(), any())).thenReturn(adImage);
        CreateOrUpdateAdDto createOrUpdateAdDto = new CreateOrUpdateAdDto("title", 123, "description");
        Ad ad = new Ad(123, "title", "description", user, adImage, null);
        when(adMapper.createAdDtoToAd(any(), any(), any())).thenReturn(ad);
        AdDto adDto = new AdDto(1, "image", 1, 123,"title title title");
        when(adMapper.toAdDto(any())).thenReturn(adDto);

        assertEquals(adDto, adService.addAd(any(), any()));
    }


}