package org.ikitadevs.kafkatestuserservice.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.ikitadevs.kafkatestuserservice.models.Event;
import org.ikitadevs.kafkatestuserservice.models.enums.EventState;
import org.ikitadevs.kafkatestuserservice.repository.EventRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ListenerService {
    private final EventRepository eventRepository;

    public ListenerService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @KafkaListener(topics = "user.email.sent", groupId = "notification-service")
    @Transactional
    public void listenSuccessSentMessages(Event event) {
        Event localEvent = eventRepository.findById(event.getUuid()).orElse(null);
        if(localEvent == null || localEvent.getEventState() != EventState.FINISHED) return;
        localEvent.setEventState(EventState.FINISHED);
        localEvent.setProcessedAt(event.getProcessedAt());
        eventRepository.save(localEvent);
        log.info("Event {} processed!", event.getUuid());
    }
}
