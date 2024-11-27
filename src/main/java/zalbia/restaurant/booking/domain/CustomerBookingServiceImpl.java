package zalbia.restaurant.booking.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerBookingServiceImpl implements CustomerBookingService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationValidator reservationValidator;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public Reservation bookReservation(BookReservationParams params) {
        reservationValidator.validateNewReservation(
                params.name(),
                params.phoneNumber(),
                params.email(),
                params.reservationDateTime(),
                params.numberOfGuests(),
                params.preferredCommunicationMethod()
        );
        // this should really be a returning * query
        int reservationId = reservationRepository.bookReservation(
                params.name(),
                params.phoneNumber(),
                params.email(),
                params.preferredCommunicationMethod(),
                params.reservationDateTime(),
                params.numberOfGuests()
        );
        Reservation reservation = reservationRepository.findById((long) reservationId).get();
        notificationService.sendNotification("You have booked a reservation.", reservation);
        return reservation;
    }
}
