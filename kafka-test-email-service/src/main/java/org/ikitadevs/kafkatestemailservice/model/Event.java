package org.ikitadevs.kafkatestemailservice.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;



import java.time.Instant;
import java.util.UUID;

@Table(name = "events")
@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
        this.processedAt = Instant.now();
    }
}
