package com.maicol1912.productservice.repository;

import com.maicol1912.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

//* estendemos de la clase de mongo para realizar las operaciones, debemos enviar la clase y el tipo de dato del id
public interface ProductRepository extends MongoRepository<Product,String> {
}
