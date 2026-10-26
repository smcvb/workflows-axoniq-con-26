package io.axoniq.demo.workflows.samples.basic.api;

import org.axonframework.messaging.eventhandling.annotation.Event;

@Event(name = "wait-started")
public record WaitStarted(
        String id
) {

}
