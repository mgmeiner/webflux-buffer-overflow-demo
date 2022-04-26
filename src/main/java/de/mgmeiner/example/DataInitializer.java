package de.mgmeiner.example;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Component
class DataInitializer {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    DataInitializer(ReactiveMongoTemplate reactiveMongoTemplate) {
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    @PostConstruct
    void initData() {
        if (!reactiveMongoTemplate.collectionExists("data").blockOptional().orElse(false)) {
            Flux.range(0, 1000)
                    .map(x -> new DataContainer(randomString(70000)))
                    .concatMap(c -> reactiveMongoTemplate.save(c, "data"))
                    .blockLast();
        }
    }

    private static String randomString(int size) {
        var result = new StringBuilder();

        while (result.length() < size) {
            result.append(UUID.randomUUID());
        }

        return result.toString();
    }
}
