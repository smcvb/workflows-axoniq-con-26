package io.axoniq.demo.workflows.samples.api;

import org.axonframework.messaging.eventhandling.annotation.Event;

@Event(name = "timeout-started")
public record TimeoutsStarted(
        String id
) {

}
