package io.axoniq.demo.workflows.samples.basic.api;

import org.axonframework.messaging.eventhandling.annotation.Event;

@Event(name = "publish-started")
public record PublishStarted(
        String id
) {

}
