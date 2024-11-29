package zalbia.restaurant.booking.domain;

import jakarta.validation.constraints.*;
import zalbia.restaurant.booking.domain.validation.PhoneNumber;

import java.time.LocalDateTime;

/**
 * A reservation booking for a restaurant. Only valid bookings are used to create a {@link Reservation}.
 */
public record ReservationBooking(
        Long guestId,

        @NotNull
        @NotBlank
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

        @NotNull
        @Min(value = 1, message = "Number of guests must be at least 1")
        @Max(value = 8, message = "Number of guests cannot exceed 8")
        int numberOfGuests,

        @NotNull
        CommunicationMethod preferredCommunicationMethod
) {
}
