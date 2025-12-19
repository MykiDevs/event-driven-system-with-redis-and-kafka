package org.ikitadevs.kafkatestemailservice.listener;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ikitadevs.kafkatestemailservice.model.Event;
import org.ikitadevs.kafkatestemailservice.model.EventState;
import org.ikitadevs.kafkatestemailservice.repository.EventRepository;
import org.ikitadevs.kafkatestemailservice.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.beans.Transient;
import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationListener {
    private final EmailService emailService;
    private final EventRepository eventRepository;
    private final KafkaTemplate<String, Event> kafkaTemplate;
    private final ObjectMapper objectMapper;
    @KafkaListener(topics = "user.email.send")
    @Transactional
    public void listenSendEmailEvents(Event event) throws JsonProcessingException {
        log.info("Event is getted! {}", objectMapper.writeValueAsString(event));
        Event localEvent = eventRepository.findById(event.getUuid()).orElse(null);
        if (localEvent != null && localEvent.getEventState() == EventState.FINISHED) {
            log.warn("Second event is ignored!", event.getUuid());
            return;
        }
        if (localEvent == null) {
            localEvent = Event.builder()
                    .uuid(event.getUuid())
                    .userEmail(event.getUserEmail())
                    .eventState(EventState.PROCESSING)
                    .addedAt(event.getAddedAt())
                    .build();
            eventRepository.save(localEvent);
        }

        emailService.sendWelcomeEmail(event.getUserEmail(), "Thank you for creating account! " + objectMapper.writeValueAsString(event));
        localEvent.setProcessedAt(Instant.now());
        localEvent.setEventState(EventState.FINISHED);
        eventRepository.save(localEvent);
        kafkaTemplate.send("user.email.sent", localEvent.getUuid().toString(), event);

    }
}
