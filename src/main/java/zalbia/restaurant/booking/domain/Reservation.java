package zalbia.restaurant.booking.domain;

import jakarta.validation.constraints.*;
import org.springframework.data.annotation.Id;
import zalbia.restaurant.booking.domain.validation.*;

import java.time.LocalDateTime;
import java.util.Objects;

public final class Reservation {
    @Id
    private final Long id;
    private final Long guestId;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 100)
    private final String name;

    @NotNull
    @PhoneNumber
    private final String phoneNumber;

    @NotNull
    @Email
    private final String email;

    @NotNull
    private final CommunicationMethod preferredCommunicationMethod;
    // Only reservation date time and number of guests can be changed once a reservation is booked.

    @NotNull
    @Future
    private LocalDateTime reservationDateTime;

    @Min(value = 1, message = "Number of guests must be at least 1")
    @Max(value = 8, message = "Number of guests cannot exceed 8")
    private int numberOfGuests;
    // Can be cancelled.
    private boolean isCancelled;

    // Reservations can only be created within this package
    Reservation(
            Long id,
            Long guestId,
            String name,
            String phoneNumber,
            String email,
            LocalDateTime reservationDateTime,
            int numberOfGuests,
            CommunicationMethod preferredCommunicationMethod
    ) {
        this.id = id;
        this.guestId = guestId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.reservationDateTime = reservationDateTime;
        this.numberOfGuests = numberOfGuests;
        this.preferredCommunicationMethod = preferredCommunicationMethod;
        this.isCancelled = false;
    }

    public Long getId() {
        return id;
    }

    public Long getGuestId() {
        return guestId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getReservationDateTime() {
        return reservationDateTime;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public CommunicationMethod getPreferredCommunicationMethod() {
        return preferredCommunicationMethod;
    }

    /**
     * Updates a reservation to a new future date and time.
     *
     * @throws UpdateReservationToPastException if newReservationDateTime is missing or not in the future
     */
    public void updateReservationDateTime(@NotNull LocalDateTime newReservationDateTime) {
        if (newReservationDateTime == null) {
            throw new UpdateReservationToPastException(id, newReservationDateTime, "A new reservation date time is required.");
        }
        if (newReservationDateTime.isBefore(LocalDateTime.now())) {
            throw new UpdateReservationToPastException(id, newReservationDateTime,
                    "Reservation must be in the future. Got " + newReservationDateTime + ".");
        }
        this.reservationDateTime = newReservationDateTime;
    }

    /**
     * Updates a reservation to a new number of guests.
     *
     * @throws UpdateToInvalidNumberOfGuestsException if the numberOfGuests is not between 1 and 8
     */
    public void updateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 1) {
            throw new UpdateToInvalidNumberOfGuestsException(id, numberOfGuests, "Number of guests must be at least 1");
        } else if (numberOfGuests > 8) {
            throw new UpdateToInvalidNumberOfGuestsException(id, numberOfGuests, "Number of guests cannot exceed 8");
        }
        this.numberOfGuests = numberOfGuests;
    }

    @NotNull
    public CommunicationMethod preferredCommunicationMethod() {
        return preferredCommunicationMethod;
    }

    /**
     * Cancels an existing reservation. Canceling a reservation that's already cancelled is a noop.
     */
    public void cancel() {
        isCancelled = true;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Reservation) obj;
        return Objects.equals(this.id, that.id); // reservation is an entity
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // matching equals
    }

    @Override
    public String toString() {
        return "Reservation[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "phoneNumber=" + phoneNumber + ", " +
                "email=" + email + ", " +
                "reservationDateTime=" + reservationDateTime + ", " +
                "numberOfGuests=" + numberOfGuests + ", " +
                "preferredCommunicationMethod=" + preferredCommunicationMethod + ']';
    }
}
