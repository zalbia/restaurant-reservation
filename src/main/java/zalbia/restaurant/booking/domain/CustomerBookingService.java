package zalbia.restaurant.booking.domain;

import jakarta.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CustomerBookingService {
    Reservation bookReservation(ReservationBooking reservationBooking);

    void cancelReservation(long reservationId);

    List<Reservation> getUpcomingReservationsPaginated(long guestId, int page, int size);

    Optional<Reservation> findById(long reservationId);

    Optional<Reservation> updateReservation(long reservationId, @Nullable LocalDateTime newReservationDateTime, @Nullable Integer newNumberOfGuests);
}
