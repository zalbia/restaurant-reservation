package zalbia.restaurant.booking.domain.internal;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zalbia.restaurant.booking.domain.CommunicationMethod;
import zalbia.restaurant.booking.domain.validation.ReservationValidationException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReservationFactoryTests {

    ReservationFactory reservationFactory;

    @BeforeEach
    public void beforeEach() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            reservationFactory = new ReservationFactory(validator);
        }
    }

    @Test
    @DisplayName("Accepts a valid reservation")
    public void acceptsValidReservation() {
        assertDoesNotThrow(() ->
                reservationFactory.createNewReservation(
                        1L,
                        1L,
                        "Customer",
                        "+639160000000",
                        "customer@example.com",
                        LocalDateTime.now().plusHours(4),
                        1,
                        CommunicationMethod.SMS
                )
        );
    }

    @Test
    @DisplayName("Rejects invalid customer name")
    public void rejectsInvalidCustomerName() {
        assertThrows(ReservationValidationException.class, () ->
                reservationFactory.createNewReservation(
                        1L,
                        1L,
                        " ",
                        "+639160000000",
                        "customer@example.com",
                        LocalDateTime.now().plusHours(4),
                        1,
                        CommunicationMethod.EMAIL
                )
        );

        assertThrows(ReservationValidationException.class, () ->
                reservationFactory.createNewReservation(
                        1L,
                        1L,
                        "",
                        "+639160000000",
                        "customer@example.com",
                        LocalDateTime.now().plusHours(4),
                        1,
                        CommunicationMethod.EMAIL
                )
        );
    }

    @Test
    @DisplayName("Rejects invalid phone number")
    public void rejectsInvalidPhoneNumber() {
        assertThrows(ReservationValidationException.class, () ->
                reservationFactory.createNewReservation(
                        1L,
                        1L,
                        "Customer",
                        "not a phone number",
                        "customer@example.com",
                        LocalDateTime.now().plusHours(4),
                        1,
                        CommunicationMethod.EMAIL
                )
        );
    }

    @Test
    @DisplayName("Rejects invalid email")
    public void rejectsInvalidEmail() {
        assertThrows(ReservationValidationException.class, () ->
                reservationFactory.createNewReservation(
                        1L,
                        1L,
                        "Customer",
                        "+639160000000",
                        "invalid email",
                        LocalDateTime.now().plusHours(4),
                        1,
                        CommunicationMethod.EMAIL
                )
        );
    }

    @Test
    @DisplayName("Rejects reservation datetime in the past")
    public void rejectsPastReservationDateTime() {
        assertThrows(ReservationValidationException.class, () ->
                reservationFactory.createNewReservation(
                        1L,
                        1L,
                        "Customer",
                        "+639160000000",
                        "customer@example.com",
                        LocalDateTime.now().minusHours(4),
                        1,
                        CommunicationMethod.EMAIL
                )
        );
    }

    @Test
    @DisplayName("Rejects invalid number of guests")
    public void rejectsInvalidNumberOfGuests() {
        assertThrows(ReservationValidationException.class, () ->
                reservationFactory.createNewReservation(
                        1L,
                        1L,
                        "Customer",
                        "+639160000000",
                        "customer@example.com",
                        LocalDateTime.now().plusHours(4),
                        0,
                        CommunicationMethod.EMAIL
                )
        );

        assertThrows(ReservationValidationException.class, () ->
                reservationFactory.createNewReservation(
                        1L,
                        1L,
                        "Customer",
                        "+639160000000",
                        "customer@example.com",
                        LocalDateTime.now().plusHours(4),
                        10,
                        CommunicationMethod.EMAIL
                )
        );
    }
}
