package com.maicol1912.inventoryservice;

import com.maicol1912.inventoryservice.model.Inventory;
import com.maicol1912.inventoryservice.repository.InventoryRepository;
import com.maicol1912.inventoryservice.service.InventoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
//* sirve para definir que la aplicacion es un cliente de eureka para poderse conectarse al servidor eureka
@EnableEurekaClient
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}

	//* este bean se correra apenas la aplicacion se inicie
	//* lo que hace es que inyecta un repository para poder hacer guardado de datos alli
	//* srive para mockear datos en la base de datos
	@Bean
	public CommandLineRunner loadData(InventoryRepository inventoryRepository){
		return args -> {
			Inventory inventory =  new Inventory();
			inventory.setSkuCode("iphone_13");
			inventory.setQuantity(100);

			Inventory inventory1 =  new Inventory();
			inventory1.setSkuCode("iphone_13_red");
			inventory1.setQuantity(0);

			inventoryRepository.save(inventory);
			inventoryRepository.save(inventory1);
		};
	}
}
