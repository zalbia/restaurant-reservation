package zalbia.restaurant.booking.rest.api.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zalbia.restaurant.booking.domain.CustomerBookingService;
import zalbia.restaurant.booking.domain.Reservation;

import java.util.List;

@RestController
@Tag(
        name = "Customer Booking Endpoint",
        description = "Allows customers to book reservations at a restaurant and manage them. Confirmations are sent " +
                "via email or SMS notifications per customer preference."
)
@RequestMapping("/api/v1.0/reservations")
@ComponentScan(basePackages = "zalbia.restaurant.booking.domain")
@ComponentScan(basePackages = "zalbia.restaurant.booking.infra")
public class CustomerBookingController {
    @Autowired
    private CustomerBookingService customerBookingService;

    @Operation(summary = "Book a reservation with a name, phone number, email, reservation date and time, number of " +
            "guests, and a preferred way to get a confirmation. A notification confirming the reservation will be " +
            "sent. Returning guests may specify a guestId to book a new reservation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation booked"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid reservation booking with validation errors",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal error",
                    content = @Content(mediaType = MediaType.TEXT_PLAIN_VALUE)
            )
    })
    @PostMapping("")
    public Reservation bookReservation(@Valid @RequestBody ReservationBookingRequest request) {
        return customerBookingService.bookReservation(request.toParams());
    }

    @Operation(summary = "Cancels a reservation given a reservation ID. A notification confirming the cancellation" +
            "will be sent.")
    @DeleteMapping("/{reservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId) {
        throw new NotImplementedException("Not yet implemented");
    }

    @Operation(summary = "Fetches a paginated list of all upcoming reservations for a guest.")
    @GetMapping("")
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
