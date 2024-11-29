package zalbia.restaurant.booking.domain;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    @Modifying
    @Query("INSERT INTO reservation (name, phone_number, email, preferred_communication_method," +
            " reservation_date_time, number_of_guests) VALUES (:name, :phoneNumber, :email, " +
            ":preferredCommunicationMethod, :reservationDateTime, :numberOfGuests)")
    int bookReservationForNewGuest(
            String name,
            String phoneNumber,
            String email,
            CommunicationMethod preferredCommunicationMethod,
            LocalDateTime reservationDateTime,
            int numberOfGuests
    );

    @Modifying
    @Query("INSERT INTO reservation (guest_id, name, phone_number, email, preferred_communication_method," +
            " reservation_date_time, number_of_guests) VALUES (:guestId, :name, :phoneNumber, :email, " +
            ":preferredCommunicationMethod, :reservationDateTime, :numberOfGuests)")
    int bookReservationForExistingGuest(
            Long guestId,
            String name,
            String phoneNumber,
            String email,
            CommunicationMethod preferredCommunicationMethod,
            LocalDateTime reservationDateTime,
            int numberOfGuests
    );

    @Query("SELECT * FROM reservation WHERE guest_id = :guestId AND is_cancelled = false " +
            "ORDER BY reservation_date_time LIMIT :limit OFFSET :offset")
    List<Reservation> getUpcomingReservationsPaginated(Long guestId, int limit, int offset);

    @Query("SELECT * FROM reservation WHERE id = :reservationId AND is_cancelled = false")
    Optional<Reservation> findActiveById(Long reservationId);
}
