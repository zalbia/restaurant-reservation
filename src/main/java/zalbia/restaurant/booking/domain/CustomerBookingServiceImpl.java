package zalbia.restaurant.booking.domain;

import jakarta.annotation.Nullable;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zalbia.restaurant.booking.domain.reminder.ReservationReminderJob;
import zalbia.restaurant.booking.domain.validation.MissingReservationException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerBookingServiceImpl implements CustomerBookingService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationBookingValidator reservationBookingValidator;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private Scheduler scheduler;

    @Override
    @Transactional
    public Reservation bookReservation(ReservationBooking reservationBooking) {
        reservationBookingValidator.validateBooking(reservationBooking);
        Reservation newReservation = reservationRepository.save(buildReservation(reservationBooking));
        notificationService.sendNotification("You have booked a reservation.", newReservation);
        scheduleReminder(newReservation);
        return newReservation;
    }

    private Reservation buildReservation(ReservationBooking reservationBooking) {
        Long guestId = reservationBooking.guestId() == null ?
                reservationRepository.getNextGuestId() :
                reservationBooking.guestId();
        return new Reservation(
                null,
                guestId,
                reservationBooking.name(),
                reservationBooking.phoneNumber(),
                reservationBooking.email(),
                reservationBooking.reservationDateTime(),
                reservationBooking.numberOfGuests(),
                reservationBooking.preferredCommunicationMethod()
        );
    }

    private void scheduleReminder(Reservation newReservation) {
        LocalDateTime reminderDateTime = newReservation.getReservationDateTime().minusHours(4);
        if (reminderDateTime.isAfter(LocalDateTime.now())) {
            JobDetail job = JobBuilder.newJob(ReservationReminderJob.class)
                    .usingJobData("phoneNumber", newReservation.getPhoneNumber())
                    .usingJobData("email", newReservation.getEmail())
                    .withIdentity(newReservation.getId().toString())
                    .usingJobData("message", "You have a reservation in 4 hours")
                    .build();
            Date reminderDate = Date.from(reminderDateTime.atZone(ZoneId.systemDefault()).toInstant());
            Trigger trigger = TriggerBuilder.newTrigger().startAt(reminderDate).build();
            try {
                scheduler.scheduleJob(job, trigger);
            } catch (SchedulerException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    @Transactional
    public void cancelReservation(long reservationId) {
        reservationRepository.findActiveById(reservationId).ifPresentOrElse(reservation -> {
            reservation.cancel();
            reservationRepository.save(reservation);
            String message = "You have cancelled reservation " + reservationId;
            notificationService.sendNotification(message, reservation);
        }, () -> {
            throw new MissingReservationException(reservationId, "Reservation " + reservationId + " not found.");
        });
    }

    @Override
    public List<Reservation> getUpcomingReservationsPaginated(long guestId, int page, int pageSize) {
        return reservationRepository.getUpcomingReservationsPaginated(guestId, pageSize, page * pageSize);
    }

    @Override
    public Optional<Reservation> findById(long reservationId) {
        return reservationRepository.findActiveById(reservationId);
    }

    @Override
    public Optional<Reservation> updateReservation(
            @Nullable long reservationId,
            @Nullable LocalDateTime newReservationDateTime,
            @Nullable Integer newNumberOfGuests
    ) {
        return reservationRepository.findActiveById(reservationId).map(existingReservation -> {
            if (newReservationDateTime != null) {
                existingReservation.updateReservationDateTime(newReservationDateTime);
            }
            if (newNumberOfGuests != null) {
                existingReservation.updateNumberOfGuests(newNumberOfGuests);
            }
            reservationRepository.save(existingReservation);
            notificationService.sendNotification("You have updated your reservation", existingReservation);
            return existingReservation;
        });
    }
}
