package zalbia.restaurant.booking.rest.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import zalbia.restaurant.booking.domain.CommunicationMethod;
import zalbia.restaurant.booking.domain.CustomerBookingService;
import zalbia.restaurant.booking.domain.validation.ReservationValidationException;
import zalbia.restaurant.booking.domain.validation.UpdateReservationToPastException;
import zalbia.restaurant.booking.domain.validation.UpdateToInvalidNumberOfGuestsException;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.OptionalLong;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerBookingController.class)
public class CustomerBookingExceptionHandlerTests {

    private static BookReservationRequest bookReservationRequest;

    @BeforeAll
    public static void beforeAll() {
        bookReservationRequest = new BookReservationRequest(
                OptionalLong.empty(),
                "Customer",
                "+639170000000",
                "customer@example.com",
                LocalDateTime.now().plusHours(4),
                1,
                CommunicationMethod.EMAIL
        );
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Mock
    @MockitoBean
    private CustomerBookingService customerBookingService;

    @Test
    @DisplayName("Reservation validation errors bubble up as internal server errors")
    public void testReservationValidationErrors() throws Exception {
        Mockito.when(customerBookingService.bookReservation(any()))
                .thenThrow(new ReservationValidationException("oh no"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1.0/reservations/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookReservationRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("Updating reservation time to past bubbles up as an internal server error")
    public void testUpdatingReservationTimeToPast() throws Exception {
        long reservationId = 42L;
        LocalDateTime past = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);

        Mockito.when(customerBookingService.bookReservation(any()))
                .thenThrow(new UpdateReservationToPastException(reservationId, past, "oh no"));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1.0/reservations/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookReservationRequest)))
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

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1.0/reservations/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookReservationRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(Matchers.containsString(String.valueOf(reservationId))))
                .andExpect(content().string(Matchers.containsString(String.valueOf(invalidNumberOfGuests))));
    }
}