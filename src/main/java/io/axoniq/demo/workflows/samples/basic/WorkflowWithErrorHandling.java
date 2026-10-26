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
