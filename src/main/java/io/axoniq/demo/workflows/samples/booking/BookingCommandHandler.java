package io.axoniq.demo.workflows.samples.booking;

import io.axoniq.demo.workflows.samples.booking.api.BookingConfirmed;
import io.axoniq.demo.workflows.samples.booking.api.BookingRescheduled;
import io.axoniq.demo.workflows.samples.booking.api.ConfirmBooking;
import io.axoniq.demo.workflows.samples.booking.api.RescheduleBooking;
import org.axonframework.messaging.commandhandling.annotation.CommandHandler;
import org.axonframework.messaging.eventhandling.gateway.EventAppender;
import org.springframework.stereotype.Component;

@Component
class BookingCommandHandler {

    @CommandHandler
    void handle(ConfirmBooking command, EventAppender eventAppender) {
        eventAppender.append(new BookingConfirmed(command.bookingId(), command.paymentId()));
    }

    @CommandHandler
    void handle(RescheduleBooking command, EventAppender eventAppender) {
        eventAppender.append(new BookingRescheduled(command.bookingId()));
    }
}
