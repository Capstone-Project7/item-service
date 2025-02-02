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
	private long tailorId;
	private String itemStatus;
    private CataloguePojo cataloguePojo;

}
