package zalbia.restaurant.booking.domain.reminder;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zalbia.restaurant.booking.infra.SmsService;

@Component
public class SmsReservationReminderJob implements Job {

    @Autowired
    private SmsService smsService;

    @Override
    public void execute(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        String message = jobDataMap.getString("message");
        String phoneNumber = jobDataMap.getString("phoneNumber");
        smsService.send(message, phoneNumber);
    }
}
