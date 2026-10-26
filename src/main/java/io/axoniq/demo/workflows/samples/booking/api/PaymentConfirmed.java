package io.axoniq.demo.workflows.samples.booking.api;

import org.axonframework.messaging.eventhandling.annotation.Event;

@Event(name = "PaymentConfirmed")
public record PaymentConfirmed(String bookingId, String paymentId) {

}
