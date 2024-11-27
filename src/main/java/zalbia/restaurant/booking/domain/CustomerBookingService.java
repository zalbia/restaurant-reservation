package zalbia.restaurant.booking.domain;

import zalbia.restaurant.booking.domain.internal.Reservation;

import java.time.LocalDateTime;
import java.util.OptionalLong;

public interface CustomerBookingService {
    public Reservation bookReservation(BookReservationParams params);
}
