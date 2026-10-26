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

import io.axoniq.demo.workflows.samples.basic.api.PaymentAccepted;
import io.axoniq.framework.workflow.annotation.Workflow;
import io.axoniq.framework.workflow.dsl.api.EventCondition;
import io.axoniq.framework.workflow.dsl.api.WorkflowStepResult;
import io.axoniq.framework.workflow.dsl.simple.SimpleWorkflowContext;
import org.axonframework.messaging.core.QualifiedName;
import org.springframework.stereotype.Component;

import java.util.Map;

import static io.axoniq.framework.workflow.dsl.api.EventAssociationsUtils.equalsTo;
import static io.axoniq.framework.workflow.dsl.api.EventConditions.fromQualifiedName;
import static io.axoniq.framework.workflow.runtime.association.Associations.associate;
import static io.axoniq.framework.workflow.runtime.association.PayloadPropertyValueRetriever.payloadProperty;

@Component
public class WorkflowWait {

    @Workflow(workflowName = "wait", startOnEventName = "sample.wait-started", idProperty = "id")
    public void execute(SimpleWorkflowContext context) {
        EventCondition condition = fromQualifiedName(
                new QualifiedName(PaymentAccepted.class),            // event name
                associate(payloadProperty("id"), equalsTo("someId")) // event association
        );

        WorkflowStepResult asyncWait = context.waitForEvent(
                "async",    // step name
                condition   // event condition
        );

        Map<String, Object> syncWait = context.awaitEvent("sync", condition);
    }
}
