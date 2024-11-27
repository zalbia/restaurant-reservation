package zalbia.restaurant.booking.domain;

import java.time.LocalDateTime;
import java.util.OptionalLong;

public record BookReservationParams(
        OptionalLong guestId,
        String name,
        String phoneNumber,
        String email,
        LocalDateTime reservationDateTime,
        int numberOfGuests,
        CommunicationMethod preferredCommunicationMethod
) {
}
