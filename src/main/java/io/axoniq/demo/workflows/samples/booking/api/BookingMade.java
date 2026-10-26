package io.axoniq.demo.workflows.samples.booking.api;

import org.axonframework.messaging.eventhandling.annotation.Event;

@Event(name = "BookingMade")
public record BookingMade(String bookingId) {

}
