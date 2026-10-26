package io.axoniq.demo.workflows.samples;

import io.axoniq.framework.workflow.annotation.Workflow;
import io.axoniq.framework.workflow.dsl.api.CombinatorWorkflowStepResult;
import io.axoniq.framework.workflow.dsl.api.WorkflowStepResult;
import io.axoniq.framework.workflow.dsl.simple.SimpleWorkflowContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class WorkflowWithCombinators {

    @Workflow(workflowName = "combinators", startOnEventName = "sample.combinators-started", idProperty = "id")
    public void execute(SimpleWorkflowContext context) {
        var resultOne = context.execute("taskOne", (_, payload) -> taskOne(payload));
        var resultTwo = context.execute("taskTwo", (_, payload) -> taskTwo(payload));

        CombinatorWorkflowStepResult guard =
                context.allMatch(WorkflowStepResult::success, resultOne, resultTwo);

        if (guard.success()) {
            // ...
        } else if (guard.failure()) {
            // ...
        }
    }

    private static Map<String, Object> taskOne(Map<String, Object> payload) {
        // Perform execute task here
        return payload;
    }

    private static Map<String, Object> taskTwo(Map<String, Object> payload) {
        // Perform execute task here
        return payload;
    }
}
