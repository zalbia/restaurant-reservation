package zalbia.restaurant.booking.domain.validation;

public class ReservationValidationException extends CustomerBookingException {
    public ReservationValidationException(String message) {
        super(message);
    }
}
