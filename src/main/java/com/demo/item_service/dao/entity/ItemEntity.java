package com.demo.item_service.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
@Table(name="order_item_details")
public class ItemEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="item_id")
	private int itemId;
	
	@Column(name="order_id")
	private int orderId;
	
	@Column(name="catalogue_id")
	private int catalogueId;
	
	@Column(name="tailor_id")
	private int tailorId;
	
	@Column(name="item_status")
	private String itemStatus;
	
}
