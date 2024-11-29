package zalbia.restaurant.booking.rest.api.v1;

import jakarta.validation.constraints.*;
import zalbia.restaurant.booking.domain.CommunicationMethod;
import zalbia.restaurant.booking.domain.Reservation;

import java.time.LocalDateTime;

/**
 * Response body DTO for reservations. Annotations are for API documentation.
 */
public record ReservationResponseBody(
        @NotNull
        @Min(1)
        long id,

        @NotNull
        @Min(1)
        long guestId,

        @NotNull
        @NotBlank
        @Size(min = 1, max = 100)
        String name,

        @NotNull
        String phoneNumber,

        @NotNull
        @Email
        String email,

        @NotNull
        CommunicationMethod preferredCommunicationMethod,

        @NotNull
        LocalDateTime reservationDateTime,

        @NotNull
        @Min(value = 1, message = "Number of guests must be at least 1")
        @Max(value = 8, message = "Number of guests cannot exceed 8")
        int numberOfGuests,

        @NotNull
        boolean isCancelled
) {
    public static ReservationResponseBody fromReservation(Reservation reservation) {
        return new ReservationResponseBody(
                reservation.getId(),
                reservation.getGuestId(),
                reservation.getName(),
                reservation.getPhoneNumber(),
                reservation.getEmail(),
                reservation.getPreferredCommunicationMethod(),
                reservation.getReservationDateTime(),
                reservation.getNumberOfGuests(),
                reservation.isCancelled()
        );
    }
}
