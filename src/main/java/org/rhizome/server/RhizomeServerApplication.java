package org.rhizome.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RhizomeServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RhizomeServerApplication.class, args);
    }
}
