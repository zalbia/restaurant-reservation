package zalbia.restaurant.booking.domain;

import java.util.List;
import java.util.Optional;

public interface CustomerBookingService {
    public Reservation bookReservation(BookReservationParams params);

    public void cancelReservation(Long reservationId);

    List<Reservation> getReservationsPaginated(Long guestId, int page, int size);

    Optional<Reservation> findById(Long reservationId);
}
