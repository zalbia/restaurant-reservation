package zalbia.restaurant.booking.domain;

import zalbia.restaurant.booking.domain.internal.Reservation;

public interface NotificationService {
    /**
     * Sends a notification to a guest about their reservation.
     *
     * @param message
     * @param reservation the reservation
     * @throws SendNotificationException if sending fails.
     */
    public void sendNotification(String message, Reservation reservation);
}
