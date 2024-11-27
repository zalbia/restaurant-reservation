package zalbia.restaurant.booking.rest.api.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import zalbia.restaurant.booking.domain.validation.CustomerBookingException;
import zalbia.restaurant.booking.domain.validation.ReservationValidationException;
import zalbia.restaurant.booking.domain.validation.UpdateReservationToPastException;
import zalbia.restaurant.booking.domain.validation.UpdateToInvalidNumberOfGuestsException;

/**
 * Provides API layer error handling for internal errors in customer booking.
 */
@RestControllerAdvice
public class CustomerBookingExceptionHandler {
    private static final String UNKNOWN_INTERNAL_ERROR = "An unknown internal error has occurred";
    private static final String PREFIX = "An internal error occurred while ";

    @ExceptionHandler(CustomerBookingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleCustomerBookingErrors(CustomerBookingException cbe) {
        String errorMessage = switch (cbe) {
            case ReservationValidationException ignored -> PREFIX + "booking a reservation.";
            case UpdateReservationToPastException e ->
                    PREFIX + "updating the reservation time of reservation with ID " + e.getReservationId() + " to " +
                            e.getReservationDateTime() + ".";
            case UpdateToInvalidNumberOfGuestsException e ->
                    PREFIX + "updating number of guests of reservation with ID " + e.getReservationId() + " to" +
                            e.getNumberOfGuests() + ".";
            default -> UNKNOWN_INTERNAL_ERROR;
        };
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
