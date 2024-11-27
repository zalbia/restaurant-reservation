package zalbia.restaurant.booking.domain;

import org.springframework.stereotype.Component;

@Component
public class NotificationServiceImpl implements NotificationService {
    @Override
    public void sendNotification(String message, Reservation reservation) {
        // TODO Implement
        System.out.println(message);
    }
}
