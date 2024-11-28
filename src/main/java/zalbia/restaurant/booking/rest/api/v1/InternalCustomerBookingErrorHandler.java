package zalbia.restaurant.booking.rest.api.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import zalbia.restaurant.booking.domain.validation.MissingReservationException;
import zalbia.restaurant.booking.domain.validation.ReservationValidationException;
import zalbia.restaurant.booking.domain.validation.UpdateReservationToPastException;
import zalbia.restaurant.booking.domain.validation.UpdateToInvalidNumberOfGuestsException;

/**
 * Provides API layer error handling for internal errors in customer booking.
 */
@RestControllerAdvice
public class InternalCustomerBookingErrorHandler {
    private static final String INTERNAL_ERROR_PREFIX = "An internal error occurred while ";

    @ExceptionHandler(ReservationValidationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleReservationValidationException(ReservationValidationException ignored) {
        return new ResponseEntity<>(INTERNAL_ERROR_PREFIX + "booking a reservation.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UpdateReservationToPastException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleUpdateReservationToPast(UpdateReservationToPastException e) {
        String message = INTERNAL_ERROR_PREFIX + "updating the reservation time of reservation with ID " + e.getReservationId() + " to " +
                e.getReservationDateTime() + ".";
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UpdateToInvalidNumberOfGuestsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleUpdateToInvalidNumberOfGuests(UpdateToInvalidNumberOfGuestsException e) {
        String message = INTERNAL_ERROR_PREFIX + "updating number of guests of reservation with ID " + e.getReservationId() + " to" +
                e.getNumberOfGuests() + ".";
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MissingReservationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleMissingReservation(MissingReservationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}
