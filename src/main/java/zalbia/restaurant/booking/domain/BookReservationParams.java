package zalbia.restaurant.booking.domain;

import java.time.LocalDateTime;

public record BookReservationParams(
        Long guestId,
        String name,
        String phoneNumber,
        String email,
        LocalDateTime reservationDateTime,
        int numberOfGuests,
        CommunicationMethod preferredCommunicationMethod
) {
}
