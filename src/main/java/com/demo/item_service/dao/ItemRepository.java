package com.demo.item_service.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.item_service.dao.entity.ItemEntity;


@Repository
public interface ItemRepository extends JpaRepository<ItemEntity,Integer> {

	List<ItemEntity> findByTailorId(int tailorId);
	List<ItemEntity> findByOrderId(int orderId);

}
