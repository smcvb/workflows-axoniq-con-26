package io.axoniq.demo.workflows.samples;

import io.axoniq.framework.workflow.annotation.Workflow;
import io.axoniq.framework.workflow.dsl.api.StepFailedException;
import io.axoniq.framework.workflow.dsl.simple.SimpleWorkflowContext;
import org.axonframework.messaging.core.unitofwork.ProcessingContext;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.Map;

import static io.axoniq.framework.workflow.dsl.api.Payload.payload;

@Component
public class WorkflowWithErrorHandling {

    @Workflow(workflowName = "error-handling", startOnEventName = "sample.error-handling-started", idProperty = "id")
    public void execute(SimpleWorkflowContext context) {
        try {
            context.awaitExecute("task",
                                 payload("id", "ab-371-de", "amount", 1337).getValues(),
                                 SomeService::task);
        } catch (StepFailedException e) {
            context.awaitExecute("compensate",
                                 payload("id", "ab-371-de").getValues(),
                                 CompensatingService::compensatingTask);
            context.fail(new RuntimeException("Payment failed: " + e.getMessage()));
        }
    }

    class SomeService {

        public static Map<String, @Nullable Object> task(ProcessingContext context,
                                                         Map<String, Object> stringObjectMap) {
            return null;
        }
    }

    class CompensatingService {

        public static Map<String, @Nullable Object> compensatingTask(ProcessingContext context,
                                                                     Map<String, @Nullable Object> stringObjectMap) {
            return null;
        }

        public static Map<String, @Nullable Object> reserve(ProcessingContext context,
                                                            Map<String, Object> stringObjectMap) {
            return null;
        }
    }
}
