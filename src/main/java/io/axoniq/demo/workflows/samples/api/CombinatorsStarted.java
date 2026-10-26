package io.axoniq.demo.workflows.samples.api;

import org.axonframework.messaging.eventhandling.annotation.Event;

@Event(name = "combinators-started")
public record CombinatorsStarted(
        String id
) {

}
