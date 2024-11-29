package zalbia.restaurant.booking.domain;

import java.util.List;
import java.util.Optional;

public interface CustomerBookingService {
    public Reservation bookReservation(ReservationBooking reservationBooking);

    public void cancelReservation(Long reservationId);

    List<Reservation> getUpcomingReservationsPaginated(Long guestId, int page, int size);

    Optional<Reservation> findById(Long reservationId);
}
