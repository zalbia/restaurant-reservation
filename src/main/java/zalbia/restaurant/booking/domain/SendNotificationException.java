package zalbia.restaurant.booking.domain;

public class SendNotificationException extends RuntimeException {
    public SendNotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
