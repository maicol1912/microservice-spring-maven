package com.maicol1912.orderservice.service;


import com.maicol1912.orderservice.dto.OrderLineItemsDto;
import com.maicol1912.orderservice.dto.OrderRequest;
import com.maicol1912.orderservice.model.Order;
import com.maicol1912.orderservice.model.OrderLineItems;
import com.maicol1912.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    //* se recibe una lista de Ordenes
    public void placeOrder(OrderRequest orderRequest){
        //* instanciamos un objeto de tipo ORDER
        Order order = new Order();
        log.info(String.valueOf(orderRequest));
        //* le definimos un id a la orden
        order.setOrderNumber(UUID.randomUUID().toString());

        //*
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();
        //* guardamos los items del order
        order.setOrderLineItemsList(orderLineItems);

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
