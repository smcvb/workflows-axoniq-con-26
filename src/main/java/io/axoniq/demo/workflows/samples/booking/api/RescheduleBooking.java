package io.axoniq.demo.workflows.samples.booking.api;

import org.axonframework.messaging.commandhandling.annotation.Command;

@Command(name = "PreparePayment")
public record RescheduleBooking(String bookingId) {

}
