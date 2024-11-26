package zalbia.restaurant.booking.rest.api.v1;

import jakarta.validation.Valid;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zalbia.restaurant.booking.domain.Reservation;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0")
public class CustomerBookingController {

    /**
     * Book a reservation by providing my name, phone number, email, reservation date and time, and number of guests.
     * The system should confirm my reservation and notify me through my preferred method of communication (i.e. SMS, Email).
     */
    @PostMapping("/reservations")
    public ReservationCreated bookReservation(@Valid @RequestBody BookReservationRequest reservationRequest) {
        throw new NotImplementedException("Not yet implemented");
    }

    @DeleteMapping("/reservations/{reservationId}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId) {
        // TODO: mark reservation cancelled
        // notify confirming cancellation
        return new ResponseEntity<>(HttpStatusCode.valueOf(204));
    }

    @GetMapping("/reservations")
    public List<Reservation> getReservationsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        throw new NotImplementedException("Not yet implemented");
    }

    @GetMapping("/reservations/{reservationId}")
    public Reservation getReservation(@PathVariable Long reservationId) {
        throw new NotImplementedException("Not yet implemented");
    }

    @PutMapping("/reservations/{reservationId}")
    public Reservation updateReservation(
            @PathVariable Long reservationId,
            @RequestBody UpdateBookingRequest updateRequest
    ) {
        throw new NotImplementedException("Not yet implemented");
    }
}
