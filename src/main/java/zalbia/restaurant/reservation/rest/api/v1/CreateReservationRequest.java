package zalbia.restaurant.reservation.rest.api.v1;

import jakarta.validation.constraints.*;
import zalbia.restaurant.reservation.model.CommunicationMethod;

import java.time.LocalDateTime;

public record CreateReservationRequest(
        @NotNull
        @NotEmpty
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
        LocalDateTime reservationDateTime,

        @Min(value = 1, message = "Number of guests must be at least 1")
        @Max(value = 8, message = "Number of guests cannot exceed 8")
        int numberOfGuests,

        @NotNull
        CommunicationMethod preferredCommunicationMethod
) {
}
