package io.axoniq.demo.workflows.samples.booking.api;

import org.axonframework.messaging.eventhandling.annotation.Event;

@Event(name = "BookingConfirmed")
public record BookingConfirmed(String bookingId, String paymentId) {

}
