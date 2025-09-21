package com.delivery.boxes.repository;

import com.delivery.boxes.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByBoxId(Long boxId);

    List<Item> findByBoxTxref(String txref);
}