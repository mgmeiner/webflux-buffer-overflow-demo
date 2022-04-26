package de.mgmeiner.example;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
class DataRestController {
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    DataRestController(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @GetMapping(path = "/stream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<DataContainer> stream() {
        return reactiveMongoTemplate.findAll(DataContainer.class, "data");
    }
}