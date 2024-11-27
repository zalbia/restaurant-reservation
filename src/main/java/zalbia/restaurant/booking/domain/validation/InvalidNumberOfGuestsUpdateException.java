package zalbia.restaurant.booking.domain.validation;

public class InvalidNumberOfGuestsUpdateException extends RuntimeException {
    private final int numberOfGuests;

    public InvalidNumberOfGuestsUpdateException(int numberOfGuests, String message) {
        super(message);
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
