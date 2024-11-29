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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zalbia.restaurant.booking.domain.CustomerBookingService;

import java.util.List;

@RestController
@Tag(
        name = "Customer Booking Endpoint",
        description = "Allows customers to book reservations at a restaurant and manage them. Confirmations are sent " +
                "via email or SMS notifications per customer preference."
)
@RequestMapping(value = "/api/v1.0/reservations")
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
                    description = "Invalid reservation booking",
                    content = @Content(schema = @Schema(example = """
                            {
                              "phoneNumber": "Invalid phone number",
                              "preferredCommunicationMethod": "must not be null",
                              "reservationDateTime": "must be a future date",
                              "name": "size must be between 1 and 100",
                              "numberOfGuests": "Number of guests must be at least 1",
                              "email": "must be a well-formed email address"
                            }"""
                    ))
            )
    })
    @PostMapping(value = "", consumes = "application/json", produces = "application/json")
    public ReservationResponseBody bookReservation(@Valid @RequestBody ReservationBookingRequest request) {
        return ReservationResponseBody.fromReservation(customerBookingService.bookReservation(request.toParams()));
    }

    @Operation(summary = "Cancels a reservation given a reservation ID. A notification confirming the cancellation" +
            "will be sent.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reservation deleted"),
            @ApiResponse(responseCode = "404", description = "No reservation deleted, reservation not found")
    })
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Void> cancelReservation(@PathVariable Long reservationId) {
        customerBookingService.cancelReservation(reservationId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Fetches a paginated list of all upcoming reservations for a guest.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "A page of a guest's upcoming reservations from earliest to latest")
    })
    @GetMapping(value = "", produces = "application/json")
    public List<ReservationResponseBody> getReservationsPaginated(
            @RequestParam Long guestId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return customerBookingService.getUpcomingReservationsPaginated(guestId, page, size)
                .stream()
                .map(ReservationResponseBody::fromReservation)
                .toList();
    }

    @Operation(summary = "Fetches a single reservation by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation found for ID"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationResponseBody> getReservation(@PathVariable Long reservationId) {
        return ResponseEntity.of(
                customerBookingService
                        .findById(reservationId)
                        .map(ReservationResponseBody::fromReservation)
        );
    }

    @Operation(summary = "Updates the time and number of guests for a reservation by ID.\n")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation updated"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")
    })
    @PatchMapping(value = "/{reservationId}", consumes = "application/json", produces = "application/json")
    public ReservationResponseBody updateReservation(
            @PathVariable Long reservationId,
            @Valid @RequestBody UpdateBookingRequest updateRequest
    ) {
        throw new NotImplementedException("Not yet implemented");
    }
}
