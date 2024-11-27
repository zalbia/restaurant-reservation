package zalbia.restaurant.booking.rest.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zalbia.restaurant.booking.domain.CustomerBookingService;
import zalbia.restaurant.booking.domain.Reservation;

import java.util.List;

@RestController
@Tag(
        name = "Customer Booking Endpoint",
        description = "Allows customers to book reservations at a restaurant and manage them. Confirmations are sent " +
                "via email or SMS per customer preference."
)
@RequestMapping("/api/v1.0/reservations")
@ComponentScan(basePackages = "zalbia.restaurant.booking.domain")
public class CustomerBookingController {
    @Autowired
    private CustomerBookingService customerBookingService;

    @Operation(summary = "Book a reservation with a name, phone number, email, reservation date and time, number of " +
            "guests, and a preferred way to get a confirmation. An email or SMS confirmation will be sent.")
    @PostMapping("/")
    public Reservation bookReservation(@Valid @RequestBody BookReservationRequest reservationRequest) {
        throw new NotImplementedException("Not yet implemented");
    }

    @Operation(summary = "Cancels a reservation given a reservation ID. An email or SMS confirmation will be sent.")
    @DeleteMapping("/{reservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId) {
        // TODO: mark reservation cancelled
        // notify confirming cancellation
        throw new NotImplementedException("Not yet implemented");
    }

    @Operation(summary = "Fetches a paginated list of all upcoming reservations for a guest.")
    @GetMapping("/")
    public List<Reservation> getReservationsPaginated(
            @RequestParam Long guestId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        throw new NotImplementedException("Not yet implemented");
    }

    @Operation(summary = "Fetches a single reservation by ID.")
    @GetMapping("/{reservationId}")
    public Reservation getReservation(@PathVariable Long reservationId) {
        throw new NotImplementedException("Not yet implemented");
    }

    @Operation(summary = "Updates the time and number of guests for a reservation by ID.\n")
    @PutMapping("/{reservationId}")
    public Reservation updateReservation(
            @PathVariable Long reservationId,
            @Valid @RequestBody UpdateBookingRequest updateRequest
    ) {
        throw new NotImplementedException("Not yet implemented");
    }
}
