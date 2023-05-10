package com.maicol1912.orderservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

//* asi se deben definir las clases de configuracion
@Configuration
public class WebClientConfig {

    //* esta es la configuracion para utilizar webFlux
    //* esta es una implementacion de la interfaz de webclient que se vera reflejada cuando la usemos

    //* debemos definir el loadBalanced debido a que podemos tener varias instancias de un microservicio, y este necesita el loadBalancer
    @Bean
    @LoadBalanced
    //* cambiamois la anotaicon a weClient.Builder
    public WebClient.Builder webClientBuilder(){
        return  WebClient.builder();
    }
}
