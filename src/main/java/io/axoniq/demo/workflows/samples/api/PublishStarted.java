package io.axoniq.demo.workflows.samples.api;

import org.axonframework.messaging.eventhandling.annotation.Event;

@Event(name = "publish-started")
public record PublishStarted(
        String id
) {

}
