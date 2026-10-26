package io.axoniq.demo.workflows.samples.booking.api;

import org.axonframework.messaging.commandhandling.annotation.Command;

@Command(name = "ConfirmBooking")
public record ConfirmBooking(String bookingId) {

}
