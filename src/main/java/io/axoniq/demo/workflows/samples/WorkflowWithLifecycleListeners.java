package io.axoniq.demo.workflows.samples;

import io.axoniq.framework.workflow.annotation.Workflow;
import io.axoniq.framework.workflow.annotation.WorkflowCompletedHandler;
import io.axoniq.framework.workflow.annotation.WorkflowFailedHandler;
import io.axoniq.framework.workflow.annotation.WorkflowStartedHandler;
import io.axoniq.framework.workflow.annotation.WorkflowTimedOutHandler;
import io.axoniq.framework.workflow.dsl.api.WorkflowStatus;
import io.axoniq.framework.workflow.dsl.simple.SimpleWorkflowContext;
import org.springframework.stereotype.Component;

@Component
public class WorkflowWithLifecycleListeners {

    @Workflow(workflowName = "lifecycle-listeners", startOnEventName = "sample.lifecycle-listeners-started", idProperty = "id")
    public void execute(SimpleWorkflowContext context) {  /*...*/}

    @WorkflowStartedHandler(workflowName = "lifecycle-listeners")
    public void started(WorkflowStatus status, SimpleWorkflowContext context) { /*...*/ }

    @WorkflowCompletedHandler(workflowName = "lifecycle-listeners")
    public void completed() { /*...*/ }

    @WorkflowTimedOutHandler(workflowName = "lifecycle-listeners")
    public void timeout() { /*...*/ }

    @WorkflowFailedHandler(workflowName = "lifecycle-listeners")
    public void failed() { /*...*/ }
}
