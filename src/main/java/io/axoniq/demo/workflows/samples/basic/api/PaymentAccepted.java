package io.axoniq.demo.workflows.samples.basic.api;

import org.axonframework.messaging.eventhandling.annotation.Event;

import java.util.UUID;

@Event(name = "payment-accepted")
public record PaymentAccepted() {

    public String paymentId() {
        return UUID.randomUUID().toString();
    }
}
