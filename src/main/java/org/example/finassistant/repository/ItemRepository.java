package org.example.finassistant.repository;

import org.example.finassistant.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item,Long> {
    public List<Item> getAllByDeletedIsFalse();
}
