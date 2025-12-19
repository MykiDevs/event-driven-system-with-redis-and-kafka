package org.ikitadevs.kafkatestemailservice.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/tests/")
@Slf4j
public class TestController {
    private final WebClient userWebClient;

    public TestController(@Qualifier("userWebClient") WebClient webClient) {
        this.userWebClient = webClient;
    }

    @GetMapping("/firenforget")
    public ResponseEntity<String> fireNForget(@RequestBody String msg) {
        userWebClient.post()
                .uri("/tests/firenforget")
                .bodyValue(msg)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
        log.info(msg);
        return ResponseEntity.ok("Sent!");
    }

    @GetMapping("/requestdata")
    public Mono<ResponseEntity<String>> helloAndSendWeb(@RequestParam("id") int id) {
        return userWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/tests/responsedata")
                        .queryParam("id", id)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(ResponseEntity::ok);
    }
}
