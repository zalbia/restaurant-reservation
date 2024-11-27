package zalbia.restaurant.booking.rest.api.v1;

import jakarta.validation.constraints.*;
import zalbia.restaurant.booking.domain.BookReservationParams;
import zalbia.restaurant.booking.domain.CommunicationMethod;
import zalbia.restaurant.booking.domain.validation.PhoneNumber;

import java.time.LocalDateTime;
import java.util.OptionalLong;

public record BookReservationRequest(
        OptionalLong guestId,

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
        LocalDateTime reservationDateTime,

        @Min(value = 1, message = "Number of guests must be at least 1")
        @Max(value = 8, message = "Number of guests cannot exceed 8")
        int numberOfGuests,

        @NotNull
        CommunicationMethod preferredCommunicationMethod
) {
    public BookReservationParams toParams() {
        return new BookReservationParams(
                guestId,
                name,
                phoneNumber,
                email,
                reservationDateTime,
                numberOfGuests,
                preferredCommunicationMethod
        );
    }
}
