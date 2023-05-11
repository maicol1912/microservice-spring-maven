package com.maicol1912.microservice;

import com.maicol1912.microservice.dto.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class NotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    //* un kafkaListener es algo que se ejcuta cuando llega un mensjae a un topico
    //* cuando un mensaje llega a un topico esto se ejecuta, sabemos que llegara un OrderPlaced ya que lo definimos desde el producer
    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(OrderPlacedEvent orderPlacedEvent){
        //send out an email notification
        log.info("Received Notification for Order {}",orderPlacedEvent.getOrderNumber());
    }
}