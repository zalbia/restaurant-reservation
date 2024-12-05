package zalbia.restaurant.booking.domain;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    @Query("SELECT * FROM reservation WHERE guest_id = :guestId AND is_cancelled = false " +
            "ORDER BY reservation_date_time LIMIT :limit OFFSET :offset")
    List<Reservation> getUpcomingReservationsPaginated(long guestId, int limit, int offset);

    @Query("SELECT * FROM reservation WHERE id = :reservationId AND is_cancelled = false")
    Optional<Reservation> findActiveById(long reservationId);

    @Query("SELECT nextval('guest_id_seq')")
    Long getNextGuestId();
}
