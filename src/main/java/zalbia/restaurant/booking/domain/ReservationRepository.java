package zalbia.restaurant.booking.domain;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends
        PagingAndSortingRepository<Reservation, Long>,
        CrudRepository<Reservation, Long> {

    @Modifying
    @Query("INSERT INTO reservation (name, phone_number, email, preferred_communication_method," +
            " reservation_date_time, number_of_guests) VALUES (:name, :phoneNumber, :email, " +
            ":preferredCommunicationMethod, :reservationDateTime, :numberOfGuests)")
    int bookReservation(
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
    int bookReservationForGuest(
            Long guestId,
            String name,
            String phoneNumber,
            String email,
            CommunicationMethod preferredCommunicationMethod,
            LocalDateTime reservationDateTime,
            int numberOfGuests
    );

    @Query("SELECT * FROM reservation WHERE guest_id = :guestId LIMIT :limit OFFSET :offset")
    List<Reservation> getReservationsPaginated(Long guestId, int limit, int offset);
}
