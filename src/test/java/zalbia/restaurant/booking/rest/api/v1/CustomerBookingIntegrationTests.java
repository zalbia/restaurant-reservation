package zalbia.restaurant.booking.rest.api.v1;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import zalbia.restaurant.booking.domain.EmailService;
import zalbia.restaurant.booking.domain.SmsService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerBookingIntegrationTests extends CommonApiTestFixture {

    private static final String BOOKING_PREFIX = "/api/v1.0/reservations/";

    @MockitoBean
    SmsService smsService;

    @MockitoBean
    EmailService emailService;

    @Test
    @DisplayName("Invalid reservation booking requests get back bad request with errors")
    @Order(1)
    public void rejectsInvalidReservationBookingRequests() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(BOOKING_PREFIX)
                .contentType(MediaType.APPLICATION_JSON)
                .content("Not JSON"))
                .andExpect(status().is4xxClientError());

        verifyNoInteractions(emailService);
        verifyNoInteractions(smsService);
    }

    @Test
    @DisplayName("Can book reservation and get notified via SMS or email")
    @Order(2)
    public void canBookReservation() throws Exception {
        String requestAsJson = objectMapper.writeValueAsString(validReservationBookingRequest);
        String expectedJson = objectMapper.writeValueAsString(validReservation);

        mockMvc.perform(MockMvcRequestBuilders.post(BOOKING_PREFIX)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJson))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(emailService).send(anyString());
        verifyNoInteractions(smsService);
    }
}
