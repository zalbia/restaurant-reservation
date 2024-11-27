package zalbia.restaurant.booking.rest.api.v1;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.FieldError;

import java.util.Date;
import java.util.List;

/**
 * Schema for validation JSON error response
 * <p>
 * Copied from <a href="https://stackoverflow.com/questions/78954282/is-there-a-ready-made-class-in-spring-boot-that-represents-the-openapi-schema-wh">...</a>
 */
public record ErrorResponse(
        @NotNull String error,
        @NotNull Date timestamp,
        @NotNull String path,
        @NotNull int status,
        @NotNull String message,
        @Nullable List<FieldError> errors
) {
}
