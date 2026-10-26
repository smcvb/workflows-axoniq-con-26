package io.axoniq.demo.workflows.samples.booking;

import io.axoniq.demo.workflows.samples.booking.api.BookingMade;
import io.axoniq.demo.workflows.samples.booking.api.ConfirmBooking;
import io.axoniq.demo.workflows.samples.booking.api.FlightDelayed;
import io.axoniq.demo.workflows.samples.booking.api.NotifyFinance;
import io.axoniq.demo.workflows.samples.booking.api.PaymentConfirmed;
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

        var paymentConfirmed = context.waitForEvent(
                "paymentConfirmed", condition(PaymentConfirmed.class, "paymentId", "1234-ab"));
        var flightDelayed = context.waitForEvent(
                "flightDelayed", condition(FlightDelayed.class, "flightNr", "523-AB-3"));

        context.execute("preparePayment",
                        sendCommand(dispatcher, new PreparePayment(bookingId)));
        context.execute("notifyFinance",
                        sendCommand(dispatcher, new NotifyFinance(bookingId)));

        var anySuccess = context.anyMatch(WorkflowStepResult::success,
                                          paymentConfirmed, flightDelayed).success();
        if (anySuccess && paymentConfirmed.isCompleted()) {
            context.execute("notifyFinance",
                            sendCommand(dispatcher, new ConfirmBooking(bookingId)));
            return;
        } else if (!context.awaitExecute("checkBookingStillValid", Boolean.class, () -> bookingStillValid(bookingId))) {
            context.execute("notifyFinance",
                            sendCommand(dispatcher, new RescheduleBooking(bookingId)));
            return;
        }
        context.fail(new StepFailedException("BookingExecution failed"));
    }

    private EventCondition condition(Class<?> name,
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
}