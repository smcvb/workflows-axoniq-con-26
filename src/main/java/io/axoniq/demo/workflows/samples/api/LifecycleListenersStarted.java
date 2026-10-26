package io.axoniq.demo.workflows.samples.api;

import org.axonframework.messaging.eventhandling.annotation.Event;

@Event(name = "lifecycle-listeners-started")
public record LifecycleListenersStarted(
        String id
) {

}
