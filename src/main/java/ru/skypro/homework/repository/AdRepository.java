package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.Ad;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdRepository extends JpaRepository<Ad, Integer> {

    /**
     * Удаляет объявление по его идентификатору.
     *
     * @param id идентификатор объявления
     */
    void removeAdById(Integer id);

    /**
     * Возвращает объявление по его идентификатору.
     *
     * @param id идентификатор объявления
     * @return объект `Ad` или `null`, если объявление не найдено
     */
    Optional<Ad> getAdById(Integer id);

    /**
     * Возвращает список объявлений, принадлежащих пользователю с заданным идентификатором.
     *
     * @param id идентификатор пользователя
     * @return список объявлений, принадлежащих пользователю
     */
    List<Ad> getAdsByUserId(Integer id);
}