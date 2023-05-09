package com.maicol1912.inventoryservice.repository;


import com.maicol1912.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    //* este query sirve para filtrar por una atributo de la entity
    Optional<Inventory>findBySkuCode(String skuCode);

    //* este query creado, con el In es para indicar si esta dentro de una lista o no
    List<Inventory>findBySkuCodeIn(List<String>skuCode);
}
