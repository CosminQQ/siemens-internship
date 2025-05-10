package com.siemens.internship;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;
public interface ItemRepository extends JpaRepository<Item, UUID> {
    /**
     * Changed the query structure so that it no longer fails at runtime, it
     * needs the alias 'item'
     * @return now it returns a list of al items UUIDs
     */
    @Query("SELECT item.id FROM Item item")
    List<UUID> findAllIds();
}
