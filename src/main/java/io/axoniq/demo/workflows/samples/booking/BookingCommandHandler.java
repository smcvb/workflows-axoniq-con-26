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
package io.axoniq.demo.workflows.samples.booking;

import io.axoniq.demo.workflows.samples.booking.api.BookingConfirmed;
import io.axoniq.demo.workflows.samples.booking.api.BookingRescheduled;
import io.axoniq.demo.workflows.samples.booking.api.ConfirmBooking;
import io.axoniq.demo.workflows.samples.booking.api.RescheduleBooking;
import org.axonframework.messaging.commandhandling.annotation.CommandHandler;
import org.axonframework.messaging.eventhandling.gateway.EventAppender;
import org.springframework.stereotype.Component;

@Component
class BookingCommandHandler {

    @CommandHandler
    void handle(ConfirmBooking command, EventAppender eventAppender) {
        eventAppender.append(new BookingConfirmed(command.bookingId(), command.paymentId()));
    }

    @CommandHandler
    void handle(RescheduleBooking command, EventAppender eventAppender) {
        eventAppender.append(new BookingRescheduled(command.bookingId()));
    }
}
