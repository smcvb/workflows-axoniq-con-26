package io.axoniq.demo.workflows.samples.booking;

import io.axoniq.demo.workflows.samples.booking.api.FinanceNotified;
import io.axoniq.demo.workflows.samples.booking.api.NotifyFinance;
import io.axoniq.demo.workflows.samples.booking.api.PaymentConfirmed;
import io.axoniq.demo.workflows.samples.booking.api.PreparePayment;
import org.axonframework.messaging.commandhandling.annotation.CommandHandler;
import org.axonframework.messaging.eventhandling.gateway.EventAppender;
import org.springframework.stereotype.Component;

@Component
class FinanceCommandHandler {

    @CommandHandler
    void handle(NotifyFinance command, EventAppender eventAppender) {
        if (command.fail()) {
            throw new IllegalStateException("Notifying finance failed for booking [%s]".formatted(command.bookingId()));
        }
        eventAppender.append(new FinanceNotified(command.bookingId(), command.paymentId()));
    }

    @CommandHandler
    void handle(PreparePayment command, EventAppender eventAppender) {
        if (command.fail()) {
            throw new IllegalStateException("Preparing the payment failed for booking [%s]".formatted(command.bookingId()));
        }
        eventAppender.append(new PaymentConfirmed(command.bookingId(), command.paymentId()));
    }
}
