package com.maicol1912.orderservice.service;


import com.maicol1912.orderservice.dto.InventoryResponse;
import com.maicol1912.orderservice.dto.OrderLineItemsDto;
import com.maicol1912.orderservice.dto.OrderRequest;
import com.maicol1912.orderservice.model.Order;
import com.maicol1912.orderservice.model.OrderLineItems;
import com.maicol1912.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
//* esto indica que todos los metodos publicos de la clase seran transaccionales, se realiza todo o nada 
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    //* debemos usar la de la clase, y esta tomara la implementacion que ya hizimos en el webClitnConfig
    private final WebClient webClient;

    //* se recibe una lista de Ordenes
    public void placeOrder(OrderRequest orderRequest){
        //* instanciamos un objeto de tipo ORDER
        Order order = new Order();
        //* le definimos un id a la orden
        order.setOrderNumber(UUID.randomUUID().toString());

        //*
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();
        //* guardamos los items del order
        order.setOrderLineItemsList(orderLineItems);

        //* obtenemos los items que hacen parte del order, y los gaurdamos en una lista para ser enviada
        //* por medio de un endpoint a otro microservicio
        List<String> skuCodes = order.getOrderLineItemsList().stream()
                                      //* esto con cada iteracion,crea un objeto y le extrae el SkuCode
                                     .map(OrderLineItems::getSkuCode)
                                     .toList();

        //llama al servicio de inventory-service y mira si hay stock
        InventoryResponse[] inventoryResponseArray = webClient.get()
                 .uri("http://127.0.0.1:8082/api/inventory",
                         //* definimos un uri builder y enviamos un param con nombre SkuCode y enviamos una lista
                         uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build()
                 )
                 .retrieve()
                 //* aca se coloca el tipo de la respuesta en este caso un array de la clase inventoy
                 .bodyToMono(InventoryResponse[].class)
                 //* para definir una peticion sincrona
                 //* osea la ejecucion del codigo se detiene hastsa que este tenga una respuesta o tenga una excepcion
                 .block();
        //* el allMacth es para ver si todos cumplen con la misma condicion en este caso que todos esten en stock
        boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);

        if(!allProductsInStock){
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
        orderRepository.save(order);
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto){
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
