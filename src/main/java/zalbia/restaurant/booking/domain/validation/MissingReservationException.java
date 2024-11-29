package zalbia.restaurant.booking.domain.validation;

public class MissingReservationException extends CustomerBookingException {
    private final long reservationId;

    public MissingReservationException(long reservationId, String message) {
        super(message);
        this.reservationId = reservationId;
    }

    public long getReservationId() {
        return reservationId;
    }
}
