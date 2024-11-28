package zalbia.restaurant.booking.infra;

import org.springframework.stereotype.Component;

@Component
public class SysOutSmsService implements SmsService {
    @Override
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}
