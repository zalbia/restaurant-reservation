package zalbia.restaurant.booking.domain.validation;

/**
 * Root of the exception hierarchy for customer reservation booking.
 */
public abstract class CustomerBookingException extends RuntimeException {
    public CustomerBookingException(String message) {
        super(message);
    }
}
