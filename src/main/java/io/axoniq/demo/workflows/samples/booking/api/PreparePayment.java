package io.axoniq.demo.workflows.samples.booking.api;

import org.axonframework.messaging.commandhandling.annotation.Command;

@Command(name = "PreparePayment")
public record PreparePayment(String bookingId) {

}
