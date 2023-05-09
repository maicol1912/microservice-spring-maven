package com.maicol1912.orderservice.dto;

import com.maicol1912.orderservice.model.OrderLineItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
//* lo que hace este Objeto es recibir una lista de Ordenes
public class OrderRequest {
    private List<OrderLineItemsDto>orderLineItemsDtoList;
}
