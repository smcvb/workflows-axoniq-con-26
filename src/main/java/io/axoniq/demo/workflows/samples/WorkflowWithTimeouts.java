package io.axoniq.demo.workflows.samples;

import io.axoniq.framework.workflow.annotation.Workflow;
import io.axoniq.framework.workflow.dsl.api.WorkflowStepResult;
import io.axoniq.framework.workflow.dsl.simple.SimpleWorkflowContext;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

import static io.axoniq.framework.workflow.dsl.api.Payload.payload;

@Component
public class WorkflowWithTimeouts {

    @Workflow(workflowName = "timeout", startOnEventName = "sample.timeout-started", idProperty = "id")
    public void execute(SimpleWorkflowContext context) {
        Map<String, Object> stepPayload = payload("id", context.workflowPayload().get("id")).getValues();

        WorkflowStepResult result = context.execute(
                "retries",                                  // step name
                stepPayload,                                // step payload
                (_, payload) -> executeTask(payload),       // step execution
                step -> step.timeout(Duration.ofSeconds(5)) // step customization
        );
    }

    private static Map<String, Object> executeTask(Map<String, Object> payload) {
        // Perform execute task here
        return payload;
    }
}
