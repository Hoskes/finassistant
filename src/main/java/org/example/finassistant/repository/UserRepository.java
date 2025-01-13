package org.example.finassistant.repository;

import org.example.finassistant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findUserByEmailAndPassword(String email,String password);
    @Query("SELECT u.name as name, e.count as count FROM ( " +
            "SELECT t.author.id AS userId, COUNT(t.author.id) AS count " +
            "FROM Transaction t " +
            "GROUP BY t.author.id) AS e " +
            "JOIN User u ON e.userId = u.id")
    List<Object[]> findUserTransactionCounts();
}

