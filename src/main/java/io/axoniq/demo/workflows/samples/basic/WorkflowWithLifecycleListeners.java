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
package io.axoniq.demo.workflows.samples.basic;

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
