package zalbia.restaurant.booking.domain.validation;

import java.time.LocalDateTime;

public class UpdateReservationToPastException extends CustomerBookingException {
    private final long reservationId;
    private final LocalDateTime reservationDateTime;

    public UpdateReservationToPastException(Long reservationId, LocalDateTime reservationDateTime, String message) {
        super(message);
        this.reservationId = reservationId;
        this.reservationDateTime = reservationDateTime;
    }

    public long getReservationId() {
        return reservationId;
    }

    public LocalDateTime getReservationDateTime() {
        return reservationDateTime;
    }
}
