package zalbia.restaurant.booking.domain.validation;

import java.time.LocalDateTime;

public class ReservationUpdateToPastException extends RuntimeException {
    private final LocalDateTime reservationDateTime;

    public ReservationUpdateToPastException(LocalDateTime reservationDateTime, String message) {
        super(message);
        this.reservationDateTime = reservationDateTime;
    }

    public LocalDateTime getReservationDateTime() {
        return reservationDateTime;
    }
}
