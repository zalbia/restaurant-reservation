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

        @Min(1)
        @Max(8)
        int newNumberOfGuests
) {
}
