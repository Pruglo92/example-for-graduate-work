package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Находит пользователя по логину.
     *
     * @param login логин пользователя
     * @return Optional с найденным пользователем или пустым значением, если пользователь не найден
     */
    Optional<User> findByLogin(String login);

    /**
     * Проверяет существование пользователя по логину.
     *
     * @param login логин пользователя
     * @return true, если пользователь существует, иначе false
     */
    boolean existsByLogin(String login);
}
