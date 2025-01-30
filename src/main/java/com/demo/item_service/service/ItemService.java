package com.demo.item_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.demo.item_service.dao.ItemRepository;
import com.demo.item_service.dao.entity.ItemEntity;
import com.demo.item_service.pojo.CataloguePojo;
import com.demo.item_service.pojo.ItemCataloguePojo;
import com.demo.item_service.pojo.OrderPojo;



@Service
public class ItemService {
	
	@Autowired
	ItemRepository itemRepo;

	public ItemCataloguePojo converItemCataloguePojo(ItemEntity entity) {
		RestClient restClient = RestClient.create();
            CataloguePojo cataloguePojo = restClient
                    .get()
                    .uri("http://localhost:8020/catalogue/product-type/" + entity.getCatalogueId())
                    .retrieve()
                    .body(CataloguePojo.class);

            ItemCataloguePojo itemCataloguePojo = new ItemCataloguePojo(entity.getItemId(),
                    entity.getOrderId(),
                    entity.getTailorId(),
                    entity.getItemStatus(),
                    cataloguePojo);
			return itemCataloguePojo;
	}
	
	public List<ItemEntity> getAllOrderItems() {
		return 	itemRepo.findAll();
	}

	public Optional<ItemEntity> getItemById(int itemId){
		return itemRepo.findById(itemId);
	}
	
	public List<ItemEntity> getItemByTailorId(int tailorId) {
		return itemRepo.findByTailorId(tailorId);
	}
	
	public List<ItemEntity> getItemByOrderId(int orderId) {
		return itemRepo.findByOrderId(orderId);
	}

	//not_started, in_progress, finished set status
	public ItemEntity updateStatus(int itemId, String status) {
		Optional<ItemEntity> item = itemRepo.findById(itemId);
        if (item.isPresent()) {
            ItemEntity updatedItem = item.get();
            updatedItem.setItemStatus(status);
            return itemRepo.save(updatedItem);
        } else {
            return null;
        }
	}

	public ItemEntity createOrderItem(ItemEntity newOrderItem) {
		
		newOrderItem.setItemStatus("not_started");
		ItemEntity itemEntity = itemRepo.saveAndFlush(newOrderItem);
		ItemCataloguePojo itemCataloguePojo = converItemCataloguePojo(itemEntity);

		RestClient restClient = RestClient.create();
		OrderPojo orderPojo = new OrderPojo();
		orderPojo.setOrderId(itemCataloguePojo.getOrderId());
		orderPojo.setOrderAmount(itemCataloguePojo.getCataloguePojo().getProductPrice());
		restClient.put().uri("http://localhost:8081/orders/updateAmount")
				.body(orderPojo).retrieve().body(OrderPojo.class);
		
				return itemEntity;
	}
}
