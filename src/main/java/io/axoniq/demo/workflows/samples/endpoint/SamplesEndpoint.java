package io.axoniq.demo.workflows.samples.endpoint;

import io.axoniq.demo.workflows.samples.BookingExecutionWorkflow.BookingMade;
import io.axoniq.demo.workflows.samples.api.CombinatorsStarted;
import io.axoniq.demo.workflows.samples.api.ErrorHandlingStarted;
import io.axoniq.demo.workflows.samples.api.ExecuteStarted;
import io.axoniq.demo.workflows.samples.api.LifecycleListenersStarted;
import io.axoniq.demo.workflows.samples.api.PublishStarted;
import io.axoniq.demo.workflows.samples.api.RetriesStarted;
import io.axoniq.demo.workflows.samples.api.TimeoutsStarted;
import io.axoniq.demo.workflows.samples.api.WaitStarted;
import org.axonframework.messaging.eventhandling.gateway.EventGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Publishes the "started" event of each workflow sample through the {@link EventGateway}. Every endpoint accepts an
 * optional {@code identifier} request parameter, generating a random one when absent, and returns the identifier used.
 */
@RestController
@RequestMapping("/samples")
public class SamplesEndpoint {

    private final EventGateway eventGateway;

    public SamplesEndpoint(EventGateway eventGateway) {
        this.eventGateway = eventGateway;
    }

    @PostMapping("/booking-execution")
    public CompletableFuture<String> startBookingExecution(@RequestParam(required = false) String identifier) {
        return publish(identifier, BookingMade::new);
    }

    @PostMapping("/execute")
    public CompletableFuture<String> startExecute(@RequestParam(required = false) String identifier) {
        return publish(identifier, ExecuteStarted::new);
    }

    @PostMapping("/publish")
    public CompletableFuture<String> startPublish(@RequestParam(required = false) String identifier) {
        return publish(identifier, PublishStarted::new);
    }

    @PostMapping("/wait")
    public CompletableFuture<String> startWait(@RequestParam(required = false) String identifier) {
        return publish(identifier, WaitStarted::new);
    }

    @PostMapping("/error-handling")
    public CompletableFuture<String> startErrorHandling(@RequestParam(required = false) String identifier) {
        return publish(identifier, ErrorHandlingStarted::new);
    }

    @PostMapping("/combinators")
    public CompletableFuture<String> startCombinators(@RequestParam(required = false) String identifier) {
        return publish(identifier, CombinatorsStarted::new);
    }

    @PostMapping("/retries")
    public CompletableFuture<String> startRetries(@RequestParam(required = false) String identifier) {
        return publish(identifier, RetriesStarted::new);
    }

    @PostMapping("/timeouts")
    public CompletableFuture<String> startTimeouts(@RequestParam(required = false) String identifier) {
        return publish(identifier, TimeoutsStarted::new);
    }

    @PostMapping("/lifecycle-listeners")
    public CompletableFuture<String> startLifecycleListeners(@RequestParam(required = false) String identifier) {
        return publish(identifier, LifecycleListenersStarted::new);
    }

    private CompletableFuture<String> publish(String identifier, Function<String, Object> eventFactory) {
        String id = identifier != null ? identifier : UUID.randomUUID().toString();
        return eventGateway.publish(List.of(eventFactory.apply(id)))
                           .thenApply(unused -> id);
    }
}
