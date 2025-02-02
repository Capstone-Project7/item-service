package com.demo.item_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.demo.item_service.dao.ItemRepository;
import com.demo.item_service.dao.entity.ItemEntity;
import com.demo.item_service.pojo.CataloguePojo;
import com.demo.item_service.pojo.ItemCataloguePojo;
import com.demo.item_service.pojo.OrderPojo;
import com.demo.item_service.pojo.TailorPojo;



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
			if(status == "finished") {
				RestClient restClient = RestClient.create();
				ItemCataloguePojo itemCataloguePojo = converItemCataloguePojo(updatedItem);
				TailorPojo tailorPojo2 = restClient.put().uri("http://localhost:9181/api/tailors?id="+updatedItem.getTailorId()+"&workload="+(itemCataloguePojo.getCataloguePojo().getProductWorkload()*-1)).retrieve().body(TailorPojo.class);
			}
            updatedItem.setItemStatus(status);
            return itemRepo.save(updatedItem);
        } else {
            return null;
        }
	}

	public ItemEntity createOrderItem(ItemEntity newOrderItem) {
		
		newOrderItem.setItemStatus("not_started");
		RestClient restClient = RestClient.create();
		List<TailorPojo> tailorPojos = restClient
            .get()
            .uri("http://localhost:8060/api/tailors/workload")
            .retrieve()
            .body(new ParameterizedTypeReference<List<TailorPojo>>(){});

		System.out.println(tailorPojos);

		ItemEntity itemEntity = new ItemEntity();
		ItemCataloguePojo itemCataloguePojo = converItemCataloguePojo(newOrderItem);
			
        if (!tailorPojos.isEmpty()) {
			TailorPojo tailorPojo = tailorPojos.get(0);
			System.out.println("\n\n\n\n"+tailorPojo+"\n\n\n");
			newOrderItem.setTailorId(tailorPojo.getTailorId());
			TailorPojo tailorPojo2 = restClient.put().uri("http://localhost:9181/api/tailors?id="+tailorPojo.getTailorId()+"&workload="+itemCataloguePojo.getCataloguePojo().getProductWorkload()).retrieve().body(TailorPojo.class);
			System.out.println("\n\n\n\n"+tailorPojo2+"\n\n\n");
		}
		itemEntity = itemRepo.saveAndFlush(newOrderItem);

		
		OrderPojo orderPojo = new OrderPojo();
		orderPojo.setOrderId(itemCataloguePojo.getOrderId());
		orderPojo.setOrderAmount(itemCataloguePojo.getCataloguePojo().getProductPrice());
		restClient.put().uri("http://localhost:8081/orders/updateAmount")
				.body(orderPojo).retrieve().body(OrderPojo.class);
		//http://localhost:8060/api/tailors/workload
		//Get a list of all tailors from the above endpoint if the list is not empty get the first tailor and its tailorid and attach it to the itemEntity then save it.
		

		return itemEntity;
	}
}
