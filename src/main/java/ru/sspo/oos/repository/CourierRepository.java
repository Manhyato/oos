package ru.sspo.oos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sspo.oos.model.Courier;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с курьерами.
 */
@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {

    /**
     * Найти всех доступных (свободных) курьеров.
     */
    List<Courier> findByAvailableTrue();

    /**
     * Найти курьера по телефону.
     */
    Optional<Courier> findByPhone(String phone);
}

