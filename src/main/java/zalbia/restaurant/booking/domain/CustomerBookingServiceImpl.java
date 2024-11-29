package zalbia.restaurant.booking.domain;

import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zalbia.restaurant.booking.domain.validation.MissingReservationException;

import java.time.LocalDateTime;
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
        Reservation reservationToSave = reservationBookingValidator.validateBooking(reservationBooking);
        Reservation newReservation = reservationRepository.save(reservationToSave);
        // <hack> support guest IDs
        Long nextGuestId = reservationRepository.getNextGuestId();
        if (newReservation.getGuestId() == Reservation.GUEST_ID_SENTINEL) {
            newReservation.setGuestId(nextGuestId);
            reservationRepository.save(newReservation);
        }
        // </hack>
        notificationService.sendNotification("You have booked a reservation.", newReservation);
        return newReservation;
    }

    @Override
    @Transactional
    public void cancelReservation(Long reservationId) {
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
    public List<Reservation> getUpcomingReservationsPaginated(Long guestId, int page, int pageSize) {
        return reservationRepository.getUpcomingReservationsPaginated(guestId, pageSize, page * pageSize);
    }

    @Override
    public Optional<Reservation> findById(Long reservationId) {
        return reservationRepository.findActiveById(reservationId);
    }

    @Override
    public Optional<Reservation> updateReservation(
            @Nullable Long reservationId,
            @Nullable LocalDateTime newReservationDateTime,
            Integer newNumberOfGuests
    ) {
        return reservationRepository.findActiveById(reservationId)
                .map(existingReservation -> {
                    if (newReservationDateTime != null) {
                        existingReservation.updateReservationDateTime(newReservationDateTime);
                    }
                    if (newNumberOfGuests != null) {
                        existingReservation.updateNumberOfGuests(newNumberOfGuests);
                    }
                    reservationRepository.save(existingReservation);
                    return existingReservation;
                });
    }
}
