package zalbia.restaurant.booking.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zalbia.restaurant.booking.domain.validation.MissingReservationException;

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

    @Override
    @Transactional
    public Reservation bookReservation(ReservationBooking reservationBooking) {
        reservationBookingValidator.validateBooking(reservationBooking);
        long reservationId = reservationBooking.guestId() == null ?
                reservationRepository.bookReservationForNewGuest(
                        reservationBooking.name(),
                        reservationBooking.phoneNumber(),
                        reservationBooking.email(),
                        reservationBooking.preferredCommunicationMethod(),
                        reservationBooking.reservationDateTime(),
                        reservationBooking.numberOfGuests()
                ) :
                reservationRepository.bookReservationForExistingGuest(
                        reservationBooking.guestId(),
                        reservationBooking.name(),
                        reservationBooking.phoneNumber(),
                        reservationBooking.email(),
                        reservationBooking.preferredCommunicationMethod(),
                        reservationBooking.reservationDateTime(),
                        reservationBooking.numberOfGuests()
                );
        Reservation reservation = reservationRepository.findById(reservationId).get();
        notificationService.sendNotification("You have booked a reservation.",
                reservation.getPreferredCommunicationMethod());
        return reservation;
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

    @Override
    public List<Reservation> getUpcomingReservationsPaginated(Long guestId, int page, int pageSize) {
        return reservationRepository.getUpcomingReservationsPaginated(guestId, pageSize, page * pageSize);
    }

    @Override
    public Optional<Reservation> findById(Long reservationId) {
        return reservationRepository.findById(reservationId).filter(r -> !r.isCancelled());
    }
}
