package zalbia.restaurant.booking.domain.validation;

import java.time.LocalDateTime;

public class InvalidReservationDateTimeException extends RuntimeException {
    private final LocalDateTime reservationDateTime;

    public InvalidReservationDateTimeException(LocalDateTime reservationDateTime, String message) {
        super(message);
        this.reservationDateTime = reservationDateTime;
    }

    public LocalDateTime getReservationDateTime() {
        return reservationDateTime;
    }
}
