package com.maicol1912.inventoryservice.service;

import com.maicol1912.inventoryservice.dto.InventoryResponse;
import com.maicol1912.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    //* el metodo sera transaccional, es decir se ejecuta todo lo declarado o nada

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode){
        //* estamos recorriendo el metodo que devuelve un objeto completo de inventory
        //* recorremos cada posicion, y lo convertimos en el id, y si existe o no
        //* retornamos lista de objetos con id y boolean
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                    InventoryResponse.builder().skuCode(inventory.getSkuCode())
                            .isInStock(inventory.getQuantity()>0)
                            .build()
                ).toList();
    }
}
