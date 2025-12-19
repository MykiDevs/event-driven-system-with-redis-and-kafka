package org.ikitadevs.kafkatestuserservice.models;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.ikitadevs.kafkatestuserservice.models.enums.EventState;


import java.time.Instant;
import java.util.UUID;

@Table(name = "events")
@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @Builder.Default
    public UUID uuid = UUID.randomUUID();
    private EventState eventState;
    private String userEmail;
    private Instant addedAt;
    private Instant processedAt;

    @PrePersist
    void prePersist() {
        this.addedAt = Instant.now();
        this.uuid = UUID.randomUUID();
    }
}
