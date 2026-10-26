package io.axoniq.demo.workflows.samples;

import io.axoniq.framework.workflow.annotation.Workflow;
import io.axoniq.framework.workflow.dsl.api.Payload;
import io.axoniq.framework.workflow.dsl.api.WorkflowStepResult;
import io.axoniq.framework.workflow.dsl.simple.SimpleWorkflowContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WorkflowExecute {

    @Workflow(workflowName = "execute", startOnEventName = "sample.execute-started", idProperty = "id")
    public void execute(SimpleWorkflowContext context) {

        Map<String, Object> stepPayload = Payload.payload("id", context.workflowPayload().get("id")).getValues();

        WorkflowStepResult asyncExecute = context.execute(
                "async",                                // step name
                stepPayload,                            // step payload
                (_, payload) -> executeTask(payload)    // step execution
        );

        Map<String, Object> syncExecute = context.awaitExecute(
                "sync", stepPayload, (_, payload) -> payload
        );
    }

    private static Map<String, Object> executeTask(Map<String, Object> payload) {
        // Perform execute task here
        return payload;
    }
}
