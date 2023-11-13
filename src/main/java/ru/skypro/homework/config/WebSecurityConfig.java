package ru.skypro.homework.config;

import liquibase.repackaged.org.apache.commons.text.translate.NumericEntityUnescaper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Конфигурация безопасности веб-приложения.
 * Этот класс определяет настройки безопасности для веб-приложения.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends GlobalMethodSecurityConfiguration {

    private final ApplicationContext context;

    /**
     * Белый список авторизации.
     * Этот список содержит URL-адреса, на которые авторизация не требуется.
     */
    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login",
            "/register"
    };

    /**
     * Фильтр цепочки безопасности.
     * Этот метод определяет правила фильтрации HTTP запросов и настройки безопасности.
     *
     * @param http объект HttpSecurity для настройки безопасности
     * @return фильтр цепочки безопасности
     * @throws Exception если возникнет ошибка при настройке безопасности
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("Was invoked method for : filterChain");

        http.csrf()
                .disable()
                .authorizeHttpRequests(
                        authorization ->
                                authorization
                                        .mvcMatchers(AUTH_WHITELIST)
                                        .permitAll()
                                        .mvcMatchers(HttpMethod.GET, "/ads")
                                        .permitAll()
                                        .mvcMatchers("/ads/**", "/users/**")
                                        .authenticated())
                .cors()
                .and()
                .httpBasic(withDefaults());
        return http.build();
    }

    /**
     * Кодировщик паролей.
     * Этот метод создает экземпляр BCryptPasswordEncoder для кодирования паролей.
     *
     * @return кодировщик паролей BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Создает и настраивает обработчик выражений безопасности для использования в конфигурации Spring Security.
     *
     * @return Обработчик выражений безопасности для использования в приложении Spring Security.
     */
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        log.info("Was invoked method for : createExpressionHandler");

        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setDefaultRolePrefix("");
        expressionHandler.setApplicationContext(context);
        return expressionHandler;
    }
}