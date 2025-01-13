package org.example.finassistant.repository;

import org.example.finassistant.model.Period;
import org.example.finassistant.model.Supply;
import org.example.finassistant.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Modifying
    @Query("SELECT t from Transaction t where t.date_created between :startDate And :finishDate")
    List<Transaction> getTransactionByDate_createdBetween(LocalDateTime startDate, LocalDateTime finishDate);

    @Query("SELECT SUM(t.quantity * t.payableItem.price) FROM Transaction t")
    Double getTotalSales();
    // Дневная выручка
    @Query("SELECT SUM(t.quantity * i.price) FROM Transaction t JOIN t.payableItem i WHERE FUNCTION('DATE', t.date_created) = CURRENT_DATE")
    Long getDaySum();

    // Запрос для получения количества транзакций за месяц
    @Query("SELECT COUNT(t) FROM Transaction t WHERE MONTH(t.date_created) = :month AND FUNCTION('YEAR', t.date_created) = :year")
    Long getTransactionCountByMonth(@Param("month") int month, @Param("year") int year);
    @Query("SELECT i.title, COUNT(t) AS count FROM Transaction t JOIN t.payableItem i GROUP BY i.id, i.title ORDER BY count DESC LIMIT 5")
    List<Object[]> getTop5Items();

    @Query(value = "SELECT SUM(t.quantity * i.price) FROM transaction t JOIN public.item i on i.id = t.item_id \n" +
            "            WHERE t.date_created >= DATE_TRUNC('quarter', CURRENT_DATE) \n" +
            "            AND t.date_created < DATE_TRUNC('quarter', CURRENT_DATE) + INTERVAL '3 months'",
            nativeQuery = true)
    Long getQuartalPros();


    @Query("SELECT s from Transaction s where s.date_created between :startDate And :finishDate")
    List<Transaction> getTransactionsByDate_createdBetween(LocalDateTime startDate, LocalDateTime finishDate);


    @Query(value = "SELECT\n" +
            "    CASE\n" +
            "        WHEN :group_by = 'day' THEN date_created::DATE\n" +
            "        WHEN :group_by = 'month' THEN DATE_TRUNC('month', date_created)\n" +
            "        WHEN :group_by = 'year' THEN DATE_TRUNC('year', date_created)\n" +
            "        END AS grouped_date,\n" +
            "    SUM(quantity) AS total_quantity\n" +
            "FROM\n" +
            "    transaction\n" +
            "GROUP BY\n" +
            "    grouped_date\n" +
            "ORDER BY\n" +
            "    grouped_date;",nativeQuery = true)
    List<Object[]> getLinearChartData(String group_by);

    @Query(value = "SELECT\n" +
            "    CASE\n" +
            "        WHEN :group_by = 'day' THEN date_created::DATE\n" +
            "        WHEN :group_by = 'month' THEN DATE_TRUNC('month', date_created)\n" +
            "        WHEN :group_by = 'year' THEN DATE_TRUNC('year', date_created)\n" +
            "        END AS grouped_date,\n" +
            "    SUM(quantity) AS total_quantity\n" +
            "FROM\n" +
            "    (SELECT * FROM transaction\n" +
            "     WHERE public.transaction.date_created::date  BETWEEN :dateStart AND :dateEnd) transaction\n" +
            "GROUP BY\n" +
            "    grouped_date\n" +
            "ORDER BY\n" +
            "    grouped_date;",nativeQuery = true)
    List<Object[]> getLinearChartDataWithDate(String group_by,LocalDateTime dateStart,LocalDateTime dateEnd);
}
