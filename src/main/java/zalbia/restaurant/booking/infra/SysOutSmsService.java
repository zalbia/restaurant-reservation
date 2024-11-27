package zalbia.restaurant.booking.infra;

import org.springframework.stereotype.Component;
import zalbia.restaurant.booking.domain.SmsService;

@Component
public class SysOutSmsService implements SmsService {
    @Override
    public void send(String message) {
        System.out.println(message);
    }
}
