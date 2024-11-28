package zalbia.restaurant.booking.rest.api.v1;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import zalbia.restaurant.booking.domain.CustomerBookingService;
import zalbia.restaurant.booking.domain.validation.MissingReservationException;
import zalbia.restaurant.booking.domain.validation.ReservationValidationException;
import zalbia.restaurant.booking.domain.validation.UpdateReservationToPastException;
import zalbia.restaurant.booking.domain.validation.UpdateToInvalidNumberOfGuestsException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class InternalCustomerBookingErrorHandlerTests extends CommonApiTestFixture {
    @Mock
    @MockitoBean
    private CustomerBookingService customerBookingService;

    @Test
    @DisplayName("Reservation validation errors bubble up as internal server errors")
    public void testReservationValidationErrors() throws Exception {
        Mockito.when(customerBookingService.bookReservation(any()))
                .thenThrow(new ReservationValidationException("oh no"));

        mockMvc.perform(MockMvcRequestBuilders.post(RESERVATIONS_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validReservationBookingRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Updating reservation time to past bubbles up as an internal server error")
    public void testUpdatingReservationTimeToPast() throws Exception {
        long reservationId = 42L;
        LocalDateTime past = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);

        Mockito.when(customerBookingService.bookReservation(any()))
                .thenThrow(new UpdateReservationToPastException(reservationId, past, "oh no"));

        mockMvc.perform(MockMvcRequestBuilders.post(RESERVATIONS_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validReservationBookingRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(Matchers.containsString(String.valueOf(reservationId))))
                .andExpect(content().string(Matchers.containsString(past.toString())));
    }

    @Test
    @DisplayName("Updating number of guests to invalid number bubbles up as an internal server error")
    public void testUpdatingToInvalidNumberOfGuests() throws Exception {
        long reservationId = 42L;
        int invalidNumberOfGuests = 1024;

        Mockito.when(customerBookingService.bookReservation(any()))
                .thenThrow(new UpdateToInvalidNumberOfGuestsException(reservationId, invalidNumberOfGuests, "oh no"));

        mockMvc.perform(MockMvcRequestBuilders.post(RESERVATIONS_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validReservationBookingRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(Matchers.containsString(String.valueOf(reservationId))))
                .andExpect(content().string(Matchers.containsString(String.valueOf(invalidNumberOfGuests))));
    }

    @Test
    @DisplayName("Cancelling a missing reservation bubbles up as a 404")
    public void testCancellingMissingReservation() throws Exception {
        long missingReservationId = 404;

        Mockito.doThrow(new MissingReservationException(missingReservationId, "oh no "))
                .when(customerBookingService).cancelReservation(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete(RESERVATIONS_URI + "/" + missingReservationId))
                .andExpect(status().isNotFound());
    }
}
