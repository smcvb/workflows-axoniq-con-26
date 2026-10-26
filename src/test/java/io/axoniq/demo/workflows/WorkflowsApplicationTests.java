package io.axoniq.demo.workflows;

import io.axoniq.framework.testcontainer.AxonServerContainer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
class WorkflowsApplicationTests {

    @Container
    @ServiceConnection
    static final AxonServerContainer axonServer = new AxonServerContainer()
            .withDevMode(true)
            .withDcbContext(true);

    @Test
    void contextLoads() {
    }
}
