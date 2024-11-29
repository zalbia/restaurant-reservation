package zalbia.restaurant.booking.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import zalbia.restaurant.booking.domain.validation.ReservationValidationException;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ReservationBookingValidator {

    private final Validator validator;

    public ReservationBookingValidator(Validator validator) {
        this.validator = validator;
    }

    /**
     * Validates a new reservation. Throws a {@link ReservationValidationException} for invalid reservations.
     * Validation is performed by an auto-injected {@link Validator} dependency.
     * <p>
     * See {@link ReservationBooking} for constraints.
     *
     * @throws ReservationValidationException if reservation arguments are invalid
     */
    public void validateBooking(ReservationBooking reservationBooking) {
        Set<ConstraintViolation<ReservationBooking>> violations = validator.validate(reservationBooking);
        if (!violations.isEmpty()) {
            throw new ReservationValidationException(
                    violations.stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.joining(", "))
            );
        }
    }
}
