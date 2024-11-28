package zalbia.restaurant.booking.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zalbia.restaurant.booking.domain.validation.MissingReservationException;

@Service
public class CustomerBookingServiceImpl implements CustomerBookingService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationValidator reservationValidator;

    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional
    public Reservation bookReservation(BookReservationParams params) {
        Reservation reservation = reservationValidator.validateNewReservation(
                params.name(),
                params.phoneNumber(),
                params.email(),
                params.reservationDateTime(),
                params.numberOfGuests(),
                params.preferredCommunicationMethod()
        );
        Reservation savedReservation = reservationRepository.save(reservation);
        notificationService.sendNotification("You have booked a reservation.",
                savedReservation.getPreferredCommunicationMethod());
        return savedReservation;
    }

    @Override
    @Transactional
    public void cancelReservation(Long reservationId) {
        reservationRepository.findById(reservationId).ifPresentOrElse(reservation -> {
            reservation.cancel();
            reservationRepository.save(reservation);
            notificationService.sendNotification("You have cancelled reservation " + reservationId,
                    reservation.getPreferredCommunicationMethod());
        }, () -> {
            throw new MissingReservationException(reservationId, "Reservation " + reservationId + " not found.");
        });
    }
}
