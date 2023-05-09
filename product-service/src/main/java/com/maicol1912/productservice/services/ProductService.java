package com.maicol1912.productservice.services;

import com.maicol1912.productservice.dto.ProductRequest;
import com.maicol1912.productservice.dto.ProductResponse;
import com.maicol1912.productservice.model.Product;
import com.maicol1912.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
//* sirve para crear el constructor de lo que necesitamos, no debemos declarar los constructores manualmente
@RequiredArgsConstructor
//* crear logger de la clase de manera mas detallada
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest){
        //* creamos la instancia de una clase apartir de otra por medio de el metodo builder
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(product);

        //* log de slf4j
        log.info("Product {} is saved",product.getId());
    }

    public List<ProductResponse>getAllProducts(){
        List<Product>products =  productRepository.findAll();
        //* estamos recorriendo los productos, cada producto va al mapToProduct y se convierte en un ProductResponse
        //* despues se convierten en una lista y se retorna
        return products.stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
