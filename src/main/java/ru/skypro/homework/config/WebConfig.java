package ru.skypro.homework.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Класс конфигурации для веб-приложения.
 */
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Конфигурирует Cross-Origin Resource Sharing (CORS) для приложения.
     *
     * @param registry Реестр CORS-маппингов
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("Was invoked method for : addCorsMappings");

        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}