package org.ikitadevs.kafkatestemailservice.repository;
import io.lettuce.core.dynamic.annotation.Param;
import org.ikitadevs.kafkatestemailservice.model.Event;
import org.ikitadevs.kafkatestemailservice.model.EventState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {
    @Modifying
    @Query("UPDATE Event e SET e.processedAt = :processedAt, e.eventState = :state WHERE e.uuid = :uuid")
    void updateProcessedTimeAndEventState(@Param("processedAt") Instant time,
                                          @Param("state")EventState state,
                                          @Param("uuid") UUID uuid);
}
