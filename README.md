# Axoniq Workflows samples

A small Spring Boot application with examples of Axoniq Framework Workflows, put together for Axoniq Con 2026.

Each sample is a workflow that starts when an event is published. The application has one REST endpoint per sample, and
each endpoint publishes that sample's start event.

## Stack

- Java 25
- Spring Boot 4.1
- Axoniq Framework 5.4.1, including the workflow DSL
- Axon Server as the event store, run in Docker
- Spring Data JPA with an in-memory H2 database, which backs the event processors' token store

## Running it

1. Use JDK 25. `.sdkmanrc` is set up for SDKMAN, so `sdk env` picks it up.
2. Start Axon Server with `docker compose up -d`. The dashboard is at http://localhost:8024.
3. Start the application, for example with `mvn spring-boot:run`. It listens on port 8080 and connects to Axon Server on
   `localhost:8124`.
4. Trigger a sample from `samples.http` in IntelliJ, or with any HTTP client.

The application needs an Axon Server context with Dynamic Consistency Boundaries (DCB) enabled.

The test (`mvn test`) starts its own DCB-enabled Axon Server in a container, so it needs Docker but not the compose
file.

## The samples

All endpoints are `POST /samples/<name>`. Each takes an optional `identifier` URL parameter and generates a random one
if it is missing. The identifier is returned in the response.

### Basic workflows

These live in `samples/basic`. Each one shows a single workflow feature.

| Endpoint               | Shows                                                                |
|------------------------|----------------------------------------------------------------------|
| `/execute`             | Running a step asynchronously and synchronously                      |
| `/publish`             | Publishing an event from a step                                      |
| `/wait`                | Waiting for an event that matches a condition                        |
| `/combinators`         | Combining step results with `allMatch`                               |
| `/error-handling`      | Catching a failed step and running a compensating step               |
| `/retries`             | A step with a retry policy and exponential backoff                   |
| `/timeouts`            | A step with a timeout                                                |
| `/lifecycle-listeners` | Handlers for a workflow starting, completing, timing out and failing |

### Booking execution

This is the larger example, in `samples/booking`. It is started with `POST /samples/booking-execution`.

The request can carry an optional JSON body that makes parts of the flow fail on purpose:

```json
{
  "failPreparePayment": false,
  "failNotifyFinance": false
}
```

When the body is left out, both values are `false`.

The workflow starts on a `BookingMade` event. It then:

1. Generates a payment id with `PaymentIdService`.
2. Waits for a `PaymentConfirmed` event with that payment id, and for a `FlightDelayed` event.
3. Sends a `PreparePayment` command. Its handler publishes `PaymentConfirmed`, unless `failPreparePayment` is true, in
   which case it throws.
4. Sends a `NotifyFinance` command. Its handler throws if `failNotifyFinance` is true.
5. Depending on the outcome, either sends `ConfirmBooking` (handled by publishing `BookingConfirmed`) or
   `RescheduleBooking` (handled by publishing `BookingRescheduled`), or fails the workflow.

Each command has its own small command handler component in `samples/booking`.

## Project layout

```
src/main/java/io/axoniq/demo/workflows
├── WorkflowsApplication.java     Spring Boot entry point
└── samples
    ├── basic                     one workflow class per feature, plus api/ with the events
    ├── booking                   the booking workflow, its command handlers, and api/ with its commands and events
    └── endpoint                  SamplesEndpoint, the REST endpoints
```

## License

This project is licensed under the Apache License, Version 2.0. See [LICENSE](LICENSE) for the full text.
