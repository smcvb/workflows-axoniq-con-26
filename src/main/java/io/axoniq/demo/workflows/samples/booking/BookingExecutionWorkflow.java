package io.axoniq.demo.workflows.samples.booking;

import io.axoniq.demo.workflows.samples.booking.api.BookingMade;
import io.axoniq.demo.workflows.samples.booking.api.ConfirmBooking;
import io.axoniq.demo.workflows.samples.booking.api.NotifyFinance;
import io.axoniq.demo.workflows.samples.booking.api.PreparePayment;
import io.axoniq.demo.workflows.samples.booking.api.RescheduleBooking;
import io.axoniq.framework.workflow.annotation.Workflow;
import io.axoniq.framework.workflow.dsl.api.EventCondition;
import io.axoniq.framework.workflow.dsl.api.PayloadProcessor;
import io.axoniq.framework.workflow.dsl.api.StepFailedException;
import io.axoniq.framework.workflow.dsl.api.WorkflowStepResult;
import io.axoniq.framework.workflow.dsl.simple.SimpleWorkflowContext;
import org.axonframework.messaging.commandhandling.gateway.CommandDispatcher;
import org.axonframework.messaging.core.QualifiedName;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

import static io.axoniq.framework.workflow.dsl.api.EventAssociationsUtils.equalsTo;
import static io.axoniq.framework.workflow.dsl.api.EventConditions.fromQualifiedName;
import static io.axoniq.framework.workflow.runtime.association.Associations.associate;
import static io.axoniq.framework.workflow.runtime.association.PayloadPropertyValueRetriever.payloadProperty;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Component
public class BookingExecutionWorkflow {

    @Workflow(
            workflowNamespace = "booking",
            workflowName = "BookingExecution",
            idProperty = "bookingId",
            startOnEventClass = BookingMade.class
    )
    public void execute(SimpleWorkflowContext context,
                        CommandDispatcher dispatcher) {
        String bookingId = (String) context.workflowPayload().get("bookingId");
        String paymentId = context.awaitExecute("generatePaymentId",
                                                String.class,
                                                BookingExecutionWorkflow::generateId);
        boolean failPreparePayment = Boolean.TRUE.equals(context.workflowPayload().get("failPreparePayment"));
        boolean failNotifyFinance = Boolean.TRUE.equals(context.workflowPayload().get("failNotifyFinance"));

        var paymentConfirmed = context.waitForEvent(
                "paymentConfirmed",
                condition("booking.PaymentConfirmed", "paymentId", paymentId)
        );
        var flightDelayed = context.waitForEvent(
                "flightDelayed",
                condition("booking.FlightDelayed", "flightNr", "523-AB-3")
        );

        context.execute(
                "preparePayment",
                sendCommand(dispatcher, new PreparePayment(bookingId, paymentId, failPreparePayment))
        );
        context.execute(
                "notifyFinance",
                sendCommand(dispatcher, new NotifyFinance(bookingId, paymentId, failNotifyFinance))
        );

        var anySuccess = context.anyMatch(WorkflowStepResult::success, paymentConfirmed, flightDelayed)
                                .success();
        if (anySuccess && paymentConfirmed.isCompleted()) {
            context.execute("confirmBooking", sendCommand(dispatcher, new ConfirmBooking(bookingId, paymentId)));
            return;
        } else if (!context.awaitExecute("checkBookingStillValid", Boolean.class, () -> bookingStillValid(bookingId))) {
            context.execute("rescheduleBooking", sendCommand(dispatcher, new RescheduleBooking(bookingId)));
            return;
        }
        context.fail(new StepFailedException("BookingExecution failed"));
    }

    private EventCondition condition(String name,
                                     String propertyKey,
                                     String propertyValue) {
        return fromQualifiedName(
                new QualifiedName(name),
                associate(payloadProperty(propertyKey), equalsTo(propertyValue))
        );
    }

    private PayloadProcessor sendCommand(CommandDispatcher dispatcher,
                                         Object command) {
        return (_, _) -> {
            dispatcher.send(command, Void.class)
                      .orTimeout(25, MILLISECONDS)
                      .join();
            return Map.of();
        };
    }

    static boolean bookingStillValid(String bookingId) {
        return true;
    }

    static String generateId() {
        return UUID.randomUUID().toString();
    }
}