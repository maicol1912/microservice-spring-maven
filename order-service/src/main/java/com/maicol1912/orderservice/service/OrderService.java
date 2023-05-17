package com.maicol1912.orderservice.service;


import com.maicol1912.orderservice.dto.InventoryResponse;
import com.maicol1912.orderservice.dto.OrderLineItemsDto;
import com.maicol1912.orderservice.dto.OrderRequest;
import com.maicol1912.orderservice.event.OrderPlacedEvent;
import com.maicol1912.orderservice.model.Order;
import com.maicol1912.orderservice.model.OrderLineItems;
import com.maicol1912.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.kafka.core.KafkaTemplate;
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
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final Tracer tracer;
    private final KafkaTemplate<String,OrderPlacedEvent> kafkaTemplate;

    public String placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                                      //* esto con cada iteracion,crea un objeto y le extrae el SkuCode
                                     .map(OrderLineItems::getSkuCode)
                                     .toList();
        log.info("Calling inventory service");

        Span inventoryServiceLookup = tracer.nextSpan().name("Inventory-ServiceLookup");

        try(Tracer.SpanInScope spanInScope = tracer.withSpan(inventoryServiceLookup.start())){
            InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build()
                    )
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();
            boolean allProductsInStock = Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);

            if(!allProductsInStock){
                throw new IllegalArgumentException("Product is not in stock, please try again later");

            }
            orderRepository.save(order);
            kafkaTemplate.send("notificationTopic",new OrderPlacedEvent(order.getOrderNumber()));
            return "Order Placed Successfully";
        }finally {
            inventoryServiceLookup.end();
        }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto){
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
