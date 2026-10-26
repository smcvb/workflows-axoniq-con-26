/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.axoniq.demo.workflows.samples.endpoint;

import io.axoniq.demo.workflows.samples.booking.api.BookingMade;
import io.axoniq.demo.workflows.samples.basic.api.CombinatorsStarted;
import io.axoniq.demo.workflows.samples.basic.api.ErrorHandlingStarted;
import io.axoniq.demo.workflows.samples.basic.api.ExecuteStarted;
import io.axoniq.demo.workflows.samples.basic.api.LifecycleListenersStarted;
import io.axoniq.demo.workflows.samples.basic.api.PublishStarted;
import io.axoniq.demo.workflows.samples.basic.api.RetriesStarted;
import io.axoniq.demo.workflows.samples.basic.api.TimeoutsStarted;
import io.axoniq.demo.workflows.samples.basic.api.WaitStarted;
import org.axonframework.messaging.eventhandling.gateway.EventGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public CompletableFuture<String> startBookingExecution(
            @RequestParam(required = false) String identifier,
            @RequestBody(required = false) BookingOptions options
    ) {
        BookingOptions effective = options != null ? options : new BookingOptions(false, false);
        return publish(identifier, id -> new BookingMade(id,
                                                         effective.failPreparePayment(),
                                                         effective.failNotifyFinance()));
    }

    /**
     * Toggles for the booking-execution sample, deciding whether the respective command handler throws.
     */
    public record BookingOptions(boolean failPreparePayment, boolean failNotifyFinance) {

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
