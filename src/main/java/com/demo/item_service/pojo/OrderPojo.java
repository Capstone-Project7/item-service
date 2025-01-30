package com.demo.item_service.pojo;

import java.time.LocalDate;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderPojo {
	private int orderId;
	private LocalDate orderDate;

	
	private LocalDate deliveryDate;
	private double orderAmount;

	private int customerId;	

}
