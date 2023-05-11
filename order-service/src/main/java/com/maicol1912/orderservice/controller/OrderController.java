package com.maicol1912.orderservice.controller;


import com.maicol1912.orderservice.dto.OrderRequest;
import com.maicol1912.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    //* le llamamos circuit bracker de inventory
    //* llamamos a un metodo que definimos abajo como fallback que es la respuesta
    @CircuitBreaker(name="inventory",fallbackMethod = "fallbackMethod")
    //* el name debe ir deacuerdo al nombre que definimos en las propierdades de application properties .instances.inventory

    //*establece un límite de tiempo para la ejecución de un método en este caso definido en el properties resilience4j.timelimiter.instances.inventory.timeoutDuration
    @TimeLimiter(name="inventory")
    //* este recibe una lista de ordenes
    //*  el CompletableFuture define algo futuro, ya que la respuesta puede ser asincrona dentro del tiempo definido en el TimeLimiter

    //* el retry define el numero de intentos que hace a un lugar antes de lanzar un errot, toma los valores definidos en el properties resilience4j.retry.instances.inventory.maxAttempts
    @Retry(name="inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest){
        return CompletableFuture.supplyAsync(()->orderService.placeOrder(orderRequest));
    }

    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest,RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(()->"Oops something went wrong, please order after some time!");
    }
}
