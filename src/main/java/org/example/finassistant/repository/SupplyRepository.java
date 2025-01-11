package org.example.finassistant.repository;

import org.example.finassistant.model.Supply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SupplyRepository extends JpaRepository<Supply,Long> {
    @Modifying
    @Query("SELECT s from Supply s where s.date_created between :startDate And :finishDate")
    List<Supply> getSuppliesByDate_createdBetween(LocalDateTime startDate, LocalDateTime finishDate);

    @Query(value = "SELECT SUM(s.quantity * s.price) FROM supply s " +
            "WHERE s.date_created >= DATE_TRUNC('quarter', CURRENT_DATE) " +
            "AND s.date_created < DATE_TRUNC('quarter', CURRENT_DATE) + INTERVAL '3 months'",
            nativeQuery = true)
    Long getQuartalCons();
}
