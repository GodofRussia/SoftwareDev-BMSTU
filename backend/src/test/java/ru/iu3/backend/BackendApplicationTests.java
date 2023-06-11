package ru.iu3.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "ru.iu3.backend.repositories")
@EntityScan("ru.iu3.backend.models")
@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
    }

}
