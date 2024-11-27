package zalbia.restaurant.booking.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zalbia.restaurant.booking.domain.internal.Reservation;
import zalbia.restaurant.booking.domain.internal.ReservationFactory;

@Service
public class CustomerBookingServiceImpl implements CustomerBookingService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationFactory reservationFactory;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public Reservation bookReservation(BookReservationParams params) {
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
