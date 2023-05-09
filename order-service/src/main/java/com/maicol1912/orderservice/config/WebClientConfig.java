package com.maicol1912.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

//* asi se deben definir las clases de configuracion
@Configuration
public class WebClientConfig {

    //* esta es la configuracion para utilizar webFlux
    //* esta es una implementacion de la interfaz de webclient que se vera reflejada cuando la usemos
    @Bean
    public WebClient webClient(){
        return  WebClient.builder().build();
    }
}
