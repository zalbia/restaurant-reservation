package zalbia.restaurant.booking.domain.validation;

public class InvalidNumberOfGuestsException extends RuntimeException {
    private final int numberOfGuests;

    public InvalidNumberOfGuestsException(int numberOfGuests, String message) {
        super(message);
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
