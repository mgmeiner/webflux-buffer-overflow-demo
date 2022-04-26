package de.mgmeiner.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.MapPropertySource;
import org.testcontainers.containers.MongoDBContainer;

import java.util.Map;

@SuppressWarnings("resource")
@SpringBootApplication
public class WebfluxBufferOverflowApplication {

    public static void main(String[] args) {
        var mongo = new MongoDBContainer("mongo:4.4.4").withReuse(true);

        mongo.start();

        var app = new SpringApplication(WebfluxBufferOverflowApplication.class);

        app.addInitializers(x -> x.getEnvironment()
                .getPropertySources()
                .addFirst(new MapPropertySource(
                        "testcontainers", Map.of("spring.data.mongodb.uri", mongo.getReplicaSetUrl())
                )));

        app.run(args);

    }
}
