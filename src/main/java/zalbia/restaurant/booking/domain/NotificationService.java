package zalbia.restaurant.booking.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zalbia.restaurant.booking.infra.EmailService;
import zalbia.restaurant.booking.infra.SmsService;

@Component
public class NotificationService {
    @Autowired
    SmsService smsService;

    @Autowired
    EmailService emailService;

    public void sendNotification(String message, Reservation reservation) {
        switch (reservation.getPreferredCommunicationMethod()) {
            case CommunicationMethod.SMS -> smsService.send(message, reservation.getPhoneNumber());
            case CommunicationMethod.EMAIL -> emailService.send(message, reservation.getEmail());
        }
    }
}
