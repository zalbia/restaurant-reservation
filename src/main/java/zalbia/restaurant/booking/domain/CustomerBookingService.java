package zalbia.restaurant.booking.domain;

import zalbia.restaurant.booking.domain.internal.Reservation;

import java.time.LocalDateTime;
import java.util.OptionalLong;

public interface CustomerBookingService {
    public Reservation bookReservation(
            OptionalLong guestId,
            String name,
            String phoneNumber,
            String email,
            LocalDateTime reservationDateTime,
            int numberOfGuests,
            CommunicationMethod preferredCommunicationMethod
    );
}
