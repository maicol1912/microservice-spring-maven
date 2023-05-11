package com.maicol1912.inventoryservice.service;

import com.maicol1912.inventoryservice.dto.InventoryResponse;
import com.maicol1912.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    //* el metodo sera transaccional, es decir se ejecuta todo lo declarado o nada

    @Transactional(readOnly = true)
    //* evita colocar las excepciones que puede lanzar el metodo
    @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> skuCode){
        //* estamos recorriendo el metodo que devuelve un objeto completo de inventory
        //* recorremos cada posicion, y lo convertimos en el id, y si existe o no
        //* retornamos lista de objetos con id y boolean
        log.info("Wait Started");
        //* aca definimos que duerma por 10 segundos
        Thread.sleep(10000);

        log.info("Wait Ended");
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                    InventoryResponse.builder().skuCode(inventory.getSkuCode())
                            .isInStock(inventory.getQuantity()>0)
                            .build()
                ).toList();
    }
}
