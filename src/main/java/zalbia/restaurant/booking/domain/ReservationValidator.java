package zalbia.restaurant.booking.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import zalbia.restaurant.booking.domain.validation.ReservationValidationException;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ReservationValidator {

    private final Validator validator;

    public ReservationValidator(Validator validator) {
        this.validator = validator;
    }

    /**
     * Validates a new reservation. Throws a {@link ReservationValidationException} for invalid reservations.
     * Validation is performed by an auto-injected {@link Validator} dependency.
     * <p>
     * See {@link Reservation} for constraints.
     *
     * @throws ReservationValidationException if reservation arguments are invalid
     */
    public Reservation validateNewReservation(
            String name,
            String phoneNumber,
            String email,
            LocalDateTime reservationDateTime,
            int numberOfGuests,
            CommunicationMethod preferredCommunicationMethod
    ) {
        final Reservation reservation = new Reservation(
                name,
                phoneNumber,
                email,
                reservationDateTime,
                numberOfGuests,
                preferredCommunicationMethod
        );
        Set<ConstraintViolation<Reservation>> violations = validator.validate(reservation);
        if (!violations.isEmpty()) {
            throw new ReservationValidationException(
                    violations.stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.joining(", "))
            );
        }
        return reservation;
    }
}
