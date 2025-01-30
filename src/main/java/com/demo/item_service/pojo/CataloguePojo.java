package com.demo.item_service.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CataloguePojo {

    private Long catalogueId;
    private String productCategory;
    private Double productPrice;
    private String productImageUrl;
    private int productWorkload;
}
