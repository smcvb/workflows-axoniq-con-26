package io.axoniq.demo.workflows.samples.basic;

import io.axoniq.framework.workflow.annotation.Workflow;
import io.axoniq.framework.workflow.dsl.api.WorkflowStepResult;
import io.axoniq.framework.workflow.dsl.api.retry.BackoffStrategy;
import io.axoniq.framework.workflow.dsl.api.retry.RetryPolicy;
import io.axoniq.framework.workflow.dsl.simple.SimpleWorkflowContext;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

import static io.axoniq.framework.workflow.dsl.api.Payload.payload;

@Component
public class WorkflowWithRetries {

    @Workflow(workflowName = "retries", startOnEventName = "sample.retries-started", idProperty = "id")
    public void execute(SimpleWorkflowContext context) {
        Map<String, Object> stepPayload = payload("id", context.workflowPayload().get("id")).getValues();

        WorkflowStepResult result = context.execute(
                "retries",                              // step name
                stepPayload,                            // step payload
                (_, payload) -> executeTask(payload),   // step execution
                step -> step.retryPolicy(               // step customization
                        RetryPolicy.maxRetries(5)
                                   .withBackoff(BackoffStrategy.exponential(
                                           Duration.ofMillis(10),
                                           Duration.ofMillis(2)
                                   ))
                )
        );
    }

    private static Map<String, Object> executeTask(Map<String, Object> payload) {
        // Perform execute task here
        return payload;
    }
}
