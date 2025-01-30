package com.demo.item_service.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import com.demo.item_service.dao.entity.ItemEntity;
import com.demo.item_service.pojo.CataloguePojo;
import com.demo.item_service.pojo.ItemCataloguePojo;
import com.demo.item_service.service.ItemService;


@Controller
@RequestMapping("/api")
public class ItemController {

    @Autowired
    private ItemService itemService;

    // Get all items
    @GetMapping("/items")
    public ResponseEntity<List<ItemEntity>> getAllOrderItems() {
        List<ItemEntity> items = itemService.getAllOrderItems();
        return ResponseEntity.ok(items);
    }

    // Get item by ID
    @GetMapping("/{itemId}")
    public ResponseEntity<?> getItemById(@PathVariable int itemId) {
        Optional<ItemEntity> item = itemService.getItemById(itemId);
        if (item.isPresent()) {
            RestClient restClient = RestClient.create();
            CataloguePojo cataloguePojo = restClient
                    .get()
                    .uri("http://localhost:8020/catalogue/product-type/" + item.get().getCatalogueId())
                    .retrieve()
                    .body(CataloguePojo.class);

            ItemCataloguePojo itemCataloguePojo = new ItemCataloguePojo(item.get().getItemId(),
                    item.get().getOrderId(),
                    item.get().getTailorId(),
                    item.get().getItemStatus(),
                    cataloguePojo);
                    return ResponseEntity.status(HttpStatus.OK).body(itemCataloguePojo);
                } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found");
        }
    }

    // Get items by tailor ID
    @GetMapping("/tailor/{tailorId}")
    public ResponseEntity<List<ItemEntity>> getItemsByTailorId(@PathVariable int tailorId) {
        List<ItemEntity> items = itemService.getItemByTailorId(tailorId);
        return ResponseEntity.ok(items);
    }

    // Get items by order ID
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<ItemEntity>> getItemsByOrderId(@PathVariable int orderId) {
        List<ItemEntity> items = itemService.getItemByOrderId(orderId);
        return ResponseEntity.ok(items);
    }

    // Create a new order item
    @PostMapping("/items")
    public ResponseEntity<ItemEntity> createOrderItem(@RequestBody ItemEntity newOrderItem) {
        ItemEntity createdItem = itemService.createOrderItem(newOrderItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    @PutMapping("/updateStatus/inProgress/{itemId}")
    public ResponseEntity<ItemEntity> updateItemStatusToInProgress(@PathVariable int itemId) {
        ItemEntity updatedItemEntity = itemService.updateStatus(itemId, "in_progress");
        return ResponseEntity.ok(updatedItemEntity);
    }
    @PutMapping("/updateStatus/finished/{itemId}")
    public ResponseEntity<ItemEntity> updateItemStatusToFinished(@PathVariable int itemId) {
        ItemEntity updatedItemEntity = itemService.updateStatus(itemId, "finished");
        return ResponseEntity.ok(updatedItemEntity);
    }
}
