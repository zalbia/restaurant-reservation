package zalbia.restaurant.booking.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CustomerBookingService {
    Reservation bookReservation(ReservationBooking reservationBooking);

    void cancelReservation(Long reservationId);

    List<Reservation> getUpcomingReservationsPaginated(Long guestId, int page, int size);

    Optional<Reservation> findById(Long reservationId);

    Optional<Reservation> updateReservation(Long reservationId, LocalDateTime localDateTime, int i);
}
