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
