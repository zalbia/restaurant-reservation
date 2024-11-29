package zalbia.restaurant.booking.rest.api.v1;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

public record UpdateReservationRequest(
        @Nullable
        @Future
        LocalDateTime newReservationDateTime,

        @Nullable
        @Min(value = 1, message = "New number of guests must be at least 1")
        @Max(value = 8, message = "New number of guests cannot exceed 8")
        Integer newNumberOfGuests
) {
}
