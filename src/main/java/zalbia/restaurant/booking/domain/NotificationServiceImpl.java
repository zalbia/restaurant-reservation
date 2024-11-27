package zalbia.restaurant.booking.domain;

import org.springframework.stereotype.Component;
import zalbia.restaurant.booking.domain.internal.Reservation;

@Component
public class NotificationServiceImpl implements NotificationService {
    @Override
    public void sendNotification(String message, Reservation reservation) {
        // TODO Implement
        System.out.println(message);
    }
}
