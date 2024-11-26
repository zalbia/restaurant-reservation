package zalbia.restaurant.booking.domain.validation;

public class ReservationValidationException extends RuntimeException {
    public ReservationValidationException(String message) {
        super(message);
    }
}
