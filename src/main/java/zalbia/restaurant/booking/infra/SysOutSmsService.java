package zalbia.restaurant.booking.infra;

import org.springframework.stereotype.Component;

@Component
public class SysOutSmsService implements SmsService {
    @Override
    public void send(String message, String phoneNumber) {
        System.out.println("Sending SMS to " + phoneNumber + ": " + message);
    }
}
