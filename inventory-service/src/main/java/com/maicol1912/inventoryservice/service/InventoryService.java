package com.maicol1912.inventoryservice.service;

import com.maicol1912.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    //* el metodo sera transaccional, es decir se ejecuta todo lo declarado o nada
    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode){
        //* esto es para que apartir de que si el opcional existe devuelva un boolean o no
        return inventoryRepository.findBySkuCode(skuCode).isPresent();
    }
}
