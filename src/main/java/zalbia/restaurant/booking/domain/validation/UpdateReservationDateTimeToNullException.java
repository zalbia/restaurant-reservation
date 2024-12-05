package zalbia.restaurant.booking.domain.validation;

import zalbia.restaurant.booking.domain.Reservation;

import java.time.LocalDateTime;

/**
 * Thrown when passing `null` to {@link Reservation#updateReservationDateTime(LocalDateTime)}.
 */
public class UpdateReservationDateTimeToNullException extends RuntimeException {
    private final long reservationId;

    public UpdateReservationDateTimeToNullException(long reservationId, String message) {
        super(message);
        this.reservationId = reservationId;
    }

    public long getReservationId() {
        return reservationId;
    }
}
