package zalbia.restaurant.booking.domain;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zalbia.restaurant.booking.domain.internal.Reservation;
import zalbia.restaurant.booking.domain.internal.ReservationFactory;

@Service
public class CustomerBookingServiceImpl implements CustomerBookingService {

    @Autowired
    ReservationFactory reservationFactory;

    public Reservation bookReservation(BookReservationParams params) {
        throw new NotImplementedException("TODO");
    }
}
