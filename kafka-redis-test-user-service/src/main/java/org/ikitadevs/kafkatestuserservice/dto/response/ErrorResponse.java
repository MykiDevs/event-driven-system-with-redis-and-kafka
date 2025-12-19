package org.ikitadevs.kafkatestuserservice.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private String error;
    private String message;

    @Builder.Default
    private Instant time = Instant.now();

    private final Map<String, Object> errors;

}
