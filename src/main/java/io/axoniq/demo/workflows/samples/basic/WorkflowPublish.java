package io.axoniq.demo.workflows.samples.basic;

import io.axoniq.demo.workflows.samples.basic.api.PaymentDeclinedEvent;
import io.axoniq.framework.workflow.annotation.Workflow;
import io.axoniq.framework.workflow.dsl.api.WorkflowStepResult;
import io.axoniq.framework.workflow.dsl.simple.SimpleWorkflowContext;
import org.springframework.stereotype.Component;

@Component
public class WorkflowPublish {

    @Workflow(workflowName = "publish", startOnEventName = "sample.publish-started", idProperty = "id")
    public void execute(SimpleWorkflowContext context) {
        WorkflowStepResult asyncPublish = context.publish(
                "async", // step name
                new PaymentDeclinedEvent() // event
        );

        context.awaitPublish(
                "sync",
                new PaymentDeclinedEvent()
        );
    }
}
