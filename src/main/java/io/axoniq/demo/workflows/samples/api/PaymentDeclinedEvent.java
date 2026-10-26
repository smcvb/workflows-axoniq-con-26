package io.axoniq.demo.workflows.samples.api;

import org.axonframework.messaging.eventhandling.annotation.Event;

@Event(name = "payment-declined")
public record PaymentDeclinedEvent() {

}
