package com.maicol1912.productservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

//* este es la forma de declarar tablas en mongoDB
@Document(value = "product")
//* generar constructor con todos los argumentos
@AllArgsConstructor
//* construir contrcutor sin argumentos
@NoArgsConstructor
//* para instanciar objetos por medio de builder
@Builder
//* para crear los metodos getter y setter
@Data
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
}
