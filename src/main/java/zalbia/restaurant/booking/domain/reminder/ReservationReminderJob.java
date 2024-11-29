package zalbia.restaurant.booking.domain.reminder;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zalbia.restaurant.booking.infra.EmailService;
import zalbia.restaurant.booking.infra.SmsService;

@Component
public class ReservationReminderJob implements Job {

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String message = jobDataMap.getString("message");
        String email = jobDataMap.getString("email");
        String phoneNumber = jobDataMap.getString("phoneNumber");
        emailService.send(message, email);
        smsService.send(message, phoneNumber);
    }
}
