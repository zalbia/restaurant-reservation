package zalbia.restaurant.booking.rest.api.v1;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateBookingRequest(
        @NotNull
        @Future
        LocalDateTime newDateTime,

        @Min(value = 1, message = "New number of guests must be at least 1")
        @Max(value = 8, message = "New number of guests cannot exceed 8")
        int newNumberOfGuests
) {
}
