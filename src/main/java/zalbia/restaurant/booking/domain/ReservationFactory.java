package zalbia.restaurant.booking.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zalbia.restaurant.booking.domain.validation.ReservationValidationException;

import java.time.LocalDateTime;
import java.util.OptionalLong;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReservationFactory {

    @Autowired
    private Validator validator;

    public Reservation createNewReservation(
            Long id,
            Long guestId,
            String name,
            String phoneNumber,
            String email,
            LocalDateTime reservationDateTime,
            int numberOfGuests,
            CommunicationMethod preferredCommunicationMethod
    ) {
        final Reservation reservation = new Reservation(
                id,
                guestId,
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
