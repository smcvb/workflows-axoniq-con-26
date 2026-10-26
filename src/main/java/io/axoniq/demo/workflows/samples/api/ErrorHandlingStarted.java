package io.axoniq.demo.workflows.samples.api;

import org.axonframework.messaging.eventhandling.annotation.Event;

@Event(name = "error-handling-started")
public record ErrorHandlingStarted(
        String id
) {

}
