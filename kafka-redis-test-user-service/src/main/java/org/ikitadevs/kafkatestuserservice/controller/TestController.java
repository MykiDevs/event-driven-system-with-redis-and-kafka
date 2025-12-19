package org.ikitadevs.kafkatestuserservice.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@RequestMapping("/tests/")
public class TestController {

    @PostMapping("/firenforget")
    public void helloFromEmailService(@RequestBody String msg) {
        log.info(msg);
    }

    @GetMapping("/responsedata")
    public Mono<ResponseEntity<String>> sendData(@RequestParam("id") int id) {
        return Mono.just(ResponseEntity.ok("Requested id: " + id));
    }

}
