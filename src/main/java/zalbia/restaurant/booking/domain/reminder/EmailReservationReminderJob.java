package zalbia.restaurant.booking.domain.reminder;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zalbia.restaurant.booking.infra.EmailService;

@Component
public class EmailReservationReminderJob implements Job {

    @Autowired
    private EmailService emailService;

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String message = jobDataMap.getString("message");
        String email = jobDataMap.getString("email");
        emailService.send(message, email);
    }
}
