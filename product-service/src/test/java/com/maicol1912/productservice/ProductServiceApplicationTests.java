package com.maicol1912.productservice;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.maicol1912.productservice.dto.ProductRequest;
import com.maicol1912.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
//* para poder correr TestContainers // sirve para administrar contenedores durante pruebas de integracion de dependencias externas
@Testcontainers
//* configuramos los mocks que usamos
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
	//* sirve para crear un contenedor con la clase que le digamos para probar el servicio externo
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

	@Autowired
	private MockMvc mockMvc;
	//* poder cambiar de json a string y de string a json
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ProductRepository productRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry){
		//* esta enlaza la uri con la uri de la imagen de docker
		//* este crea un contenedeor diferente al local
		dynamicPropertyRegistry.add("spring.data.mongodb.uri",mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				//* indicamos que el contenido que vamos a enviar es de tipo JSON
				.contentType(MediaType.APPLICATION_JSON)
				//* el contenido debe ser en formato String por lo tanto lo convertimos con ObjectMapper from jackson
				.content(productRequestString))
				//* definimos que el status deba ser un created
				//* se debe definimir un metodo estatico para esto en la parte superior
				.andExpect(status().isCreated());

		Assertions.assertEquals(1,productRepository.findAll().size() );
	}

	private ProductRequest getProductRequest(){
		return ProductRequest.builder()
				.name("iphone 13")
				.description("iphone 13")
				.price(BigDecimal.valueOf(1200))
				.build();
	}
}
