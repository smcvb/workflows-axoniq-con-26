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
