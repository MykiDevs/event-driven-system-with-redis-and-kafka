package org.ikitadevs.kafkatestuserservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ikitadevs.kafkatestuserservice.models.Event;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {
    private final KafkaTemplate<String, Event> kafkaTemplate;

    public void sendEvent(Event event, String topic) {
        kafkaTemplate.send(topic, event.getUuid().toString(), event)
                .whenComplete((result, ex) -> {
                    if(ex != null) {
                        log.info("Failed!", ex);
                    } else {
                        log.info("Success! {}", result);
                    }
                });
        log.info("Event {} with was sent!", event);
    }
}
