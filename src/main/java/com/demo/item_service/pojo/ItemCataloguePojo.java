package com.demo.item_service.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ItemCataloguePojo {

    private int itemId;
	private int orderId;
	private int tailorId;
	private String itemStatus;
    private CataloguePojo cataloguePojo;

}
