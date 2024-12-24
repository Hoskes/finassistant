package org.example.finassistant.repository;

import org.example.finassistant.model.Supply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplyRepository extends JpaRepository<Supply,Long> {
}
