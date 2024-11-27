package zalbia.restaurant.booking.domain.validation;

public class UpdateToInvalidNumberOfGuestsException extends CustomerBookingException {
    private final long reservationId;
    private final int numberOfGuests;

    public UpdateToInvalidNumberOfGuestsException(Long reservationId, int numberOfGuests, String message) {
        super(message);
        this.reservationId = reservationId;
        this.numberOfGuests = numberOfGuests;
    }

    public long getReservationId() {
        return reservationId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
