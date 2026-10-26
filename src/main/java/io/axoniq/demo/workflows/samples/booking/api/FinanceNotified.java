package io.axoniq.demo.workflows.samples.booking.api;

import org.axonframework.messaging.eventhandling.annotation.Event;

@Event(name = "FinanceNotified")
public record FinanceNotified(String bookingId, String paymentId) {

}
