package io.axoniq.demo.workflows.samples.basic.api;

import org.axonframework.messaging.eventhandling.annotation.Event;

@Event(name = "execute-started")
public record ExecuteStarted(
        String id
) {

}
