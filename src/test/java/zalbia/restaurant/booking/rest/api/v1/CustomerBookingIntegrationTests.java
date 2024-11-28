package zalbia.restaurant.booking.rest.api.v1;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import zalbia.restaurant.booking.domain.EmailService;
import zalbia.restaurant.booking.domain.SmsService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.OptionalLong;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerBookingIntegrationTests extends CommonApiTestFixture {

    @MockitoBean
    SmsService smsService;

    @MockitoBean
    EmailService emailService;

    @Test
    @DisplayName("Responds to invalid reservation booking requests with bad request with field errors")
    @Order(1)
    public void rejectsInvalidReservationBookingRequests() throws Exception {
        ReservationBookingRequest invalidRequest = new ReservationBookingRequest(
                OptionalLong.of(1L),
                "",
                "not a phone number",
                "not an email",
                LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC),
                0,
                null
        );

        MvcResult result =
                mockMvc.perform(MockMvcRequestBuilders.post(RESERVATIONS_URI)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
                        .andExpect(status().is4xxClientError())
                        .andReturn();
        Map<String, String> fieldErrors = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);

        Set<String> expectedKeys = Set.of(
                "name",
                "phoneNumber",
                "email",
                "reservationDateTime",
                "numberOfGuests",
                "preferredCommunicationMethod"
        );

        assertEquals(expectedKeys, fieldErrors.keySet());
        verifyNoInteractions(emailService);
        verifyNoInteractions(smsService);
    }

    @Test
    @DisplayName("Can book reservation and get notified via SMS or email")
    @Order(2)
    public void canBookReservation() throws Exception {
        String requestAsJson = objectMapper.writeValueAsString(validReservationBookingRequest);
        String expectedJson = objectMapper.writeValueAsString(validReservation);

        mockMvc.perform(MockMvcRequestBuilders.post(RESERVATIONS_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJson))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(emailService).send(anyString());
        verifyNoInteractions(smsService);
    }
}
