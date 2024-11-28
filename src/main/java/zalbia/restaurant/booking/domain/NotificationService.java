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

    public void sendNotification(String message, CommunicationMethod preferredCommunicationMethod) {
        switch (preferredCommunicationMethod) {
            case CommunicationMethod.SMS -> smsService.send(message);
            case CommunicationMethod.EMAIL -> emailService.send(message);
        }
    }
}
