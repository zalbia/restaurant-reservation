package zalbia.restaurant.booking.domain.internal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zalbia.restaurant.booking.domain.CommunicationMethod;
import zalbia.restaurant.booking.domain.validation.UpdateToInvalidNumberOfGuestsException;
import zalbia.restaurant.booking.domain.validation.UpdateReservationToPastException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReservationTests {

    private Reservation reservation;

    @BeforeEach
    public void beforeEach() {
        reservation = new Reservation(
                1L,
                1L,
                "Customer",
                "+639160000000",
                "customer@example.com",
                LocalDateTime.now().plusHours(4),
                1,
                CommunicationMethod.EMAIL
        );
    }

    @Test
    @DisplayName("Can update reservation to future time")
    public void canUpdateReservationTime() {
        final LocalDateTime newReservationDateTime = LocalDateTime.now().plusHours(3);

        reservation.updateReservationDateTime(newReservationDateTime);

        assertEquals(newReservationDateTime, reservation.getReservationDateTime());
    }

    @Test
    @DisplayName("Can update number of guests")
    public void canUpdateNumberOfGuests() {
        final int newNumberOfGuests = 8;

        reservation.updateNumberOfGuests(newNumberOfGuests);

        assertEquals(newNumberOfGuests, reservation.getNumberOfGuests());
    }

    @Test
    @DisplayName("Rejects updating reservation to time in the past")
    public void rejectsUpdateToPastTime() {
        final LocalDateTime pastReservationTime = LocalDateTime.now().minusHours(3);

        assertThrows(UpdateReservationToPastException.class, () ->
                reservation.updateReservationDateTime(pastReservationTime)
        );
    }

    @Test
    @DisplayName("Rejects updating to invalid number of guests")
    public void rejectsUpdateToInvalidNumberOfGuests() {
        assertThrows(UpdateToInvalidNumberOfGuestsException.class, () -> {
            final int newNumberOfGuests = 0;
            reservation.updateNumberOfGuests(newNumberOfGuests);
        });

        assertThrows(UpdateToInvalidNumberOfGuestsException.class, () -> {
            final int newNumberOfGuests = 9;
            reservation.updateNumberOfGuests(newNumberOfGuests);
        });
    }
}
