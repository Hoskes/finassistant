package org.example.finassistant.repository;

import org.example.finassistant.model.Tax;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface TaxRepository extends CrudRepository<Tax, Long> {

    // Метод для сброса всех отметок isActual в false
    @Modifying
    @Transactional
    @Query("UPDATE Tax t SET t.actual = false")
    void resetAllActualToFalse();

    // Метод для обновления isActual до true для объекта с заданным id
    @Modifying
    @Transactional
    @Query("UPDATE Tax t SET t.actual = true WHERE t.id = :id")
    void updateActualToTrueById(@Param("id") Long id);

    Tax findByActualIsTrue();
}
