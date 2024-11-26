package zalbia.restaurant.reservation.rest.api.v1;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ReservationController {

    /**
     * make a reservation by providing my name, phone number, email, reservation date and time, and number of guests.
     * The system should confirm my reservation and notify me through my preferred method of communication (i.e. SMS, Email).
     */
    @PostMapping("/reservations")
    public ReservationCreated createReservation(@RequestBody CreateReservationRequest reservationRequest) {
        throw new NotImplementedException("Not yet implemented");
    }
}
