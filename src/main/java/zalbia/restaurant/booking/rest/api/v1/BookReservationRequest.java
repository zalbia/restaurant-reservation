package zalbia.restaurant.booking.rest.api.v1;

import jakarta.validation.constraints.*;
import zalbia.restaurant.booking.domain.CommunicationMethod;
import zalbia.restaurant.booking.domain.validation.PhoneNumber;

import java.time.LocalDateTime;

public record BookReservationRequest(
        @NotNull
        @Size(min = 1, max = 100)
        String name,

        @NotNull
        @PhoneNumber
        String phoneNumber,

        @NotNull
        @Email
        String email,

        @NotNull
        @Future
        @Min(value = 4, message = "Reservation must be 4 hours ahead")
        LocalDateTime reservationDateTime,

        @Min(value = 1, message = "Number of guests must be at least 1")
        @Max(value = 8, message = "Number of guests cannot exceed 8")
        int numberOfGuests,

        @NotNull
        CommunicationMethod preferredCommunicationMethod
) {
}
