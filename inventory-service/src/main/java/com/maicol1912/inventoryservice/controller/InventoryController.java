package com.maicol1912.inventoryservice.controller;

import com.maicol1912.inventoryservice.dto.InventoryResponse;
import com.maicol1912.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    //* http://localhost:8082/api/inventory/iphone_13,iphone_13_red
    //* http://localhost:8082/api/inventory?sku-code=iphone_13&sku-code=iphone_13_red
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    //* de esta manera pdoemos recibir varios parametros enviados, separados por medio de una coma
    public List<InventoryResponse> isInsStock(@RequestParam List<String> skuCode){
        return inventoryService.isInStock(skuCode);
    }
}
