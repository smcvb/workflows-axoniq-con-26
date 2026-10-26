package io.axoniq.demo.workflows.samples.booking.api;

import org.axonframework.messaging.commandhandling.annotation.Command;

@Command(name = "NotifyFinance")
public record NotifyFinance(String bookingId, String paymentId, boolean fail) {

}
