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
