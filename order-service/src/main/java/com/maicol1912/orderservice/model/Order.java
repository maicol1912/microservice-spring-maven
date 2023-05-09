package com.maicol1912.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.List;

//* para crear una clase en mysql
@Entity
@Table(name= "t_orders")
//* getter de las clases
@Getter
//* setter de las clases
@Setter
//* constructor y no constructor
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNumber;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderLineItems>orderLineItemsList;
}
