package zalbia.restaurant.booking.infra;

import org.springframework.stereotype.Component;

@Component
public class SysOutEmailService implements EmailService {
    @Override
    public void send(String message, String email) {
        System.out.println("Sending Email to " + email + ": " + message);
    }
}
