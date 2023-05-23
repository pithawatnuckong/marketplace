package com.example.marketplace.component.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaProducerComponent {

    public KafkaProducerComponent(@Qualifier("marketplaceKafkaTemplate") KafkaTemplate<String, String> marketplaceKafkaTemplate) {
        this.marketplaceKafkaTemplate = marketplaceKafkaTemplate;
    }

    private final KafkaTemplate<String, String> marketplaceKafkaTemplate;


    public void sendMessageToKafka(String message, String topic) {
        this.marketplaceKafkaTemplate.send(topic, message)
                .whenComplete((result, throwable) -> {
            if(null == throwable){
                log.info("Kafka send to {} done {}",
                        result.getRecordMetadata().topic(),
                        result.getProducerRecord().value());
            } else{
                log.error("Kafka send exception {}", throwable.getMessage());
            }
                });
    }
}
