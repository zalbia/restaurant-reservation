package zalbia.restaurant.booking.rest.api.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import zalbia.restaurant.booking.domain.CommunicationMethod;
import zalbia.restaurant.booking.infra.EmailService;
import zalbia.restaurant.booking.domain.Reservation;
import zalbia.restaurant.booking.infra.SmsService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
    public void rejectsInvalidReservationBookingRequests() throws Exception {
        ReservationBookingRequest invalidRequest = new ReservationBookingRequest(
                1L,
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
    @Order(1)
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

    @Test
    @DisplayName("Can get reservations given guest ID")
    @Order(2)
    public void canGetGuestReservations() throws Exception {
        String expectedJson = objectMapper.writeValueAsString(List.of(validReservation));

        mockMvc.perform(MockMvcRequestBuilders.get(RESERVATIONS_URI + "?guestId=1&page=0&size=10"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        mockMvc.perform(MockMvcRequestBuilders.get(RESERVATIONS_URI + "?guestId=404&page=0&size=10"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("Can get reservation by reservation ID")
    @Order(2)
    public void canGetReservationById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(RESERVATIONS_URI + "/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(validReservation)));

        String nonExistentReservationID = "404";
        mockMvc.perform(MockMvcRequestBuilders.get(RESERVATIONS_URI + "/" + nonExistentReservationID))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    @DisplayName("Can get guest reservations by page from earliest to latest")
    @Order(2)
    public void canGetGuestReservationsByPage() throws Exception {
        for (int i = 2; i <= 10; i++) {
            ReservationBookingRequest guestReservation = new ReservationBookingRequest(
                    1L,
                    "Customer",
                    "+639170000000",
                    "customer@example.com",
                    futureDate.plusDays(10 - i),
                    1,
                    CommunicationMethod.EMAIL
            );
            String requestAsJson = objectMapper.writeValueAsString(guestReservation);
            mockMvc.perform(MockMvcRequestBuilders.post(RESERVATIONS_URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestAsJson))
                    .andExpect(status().is2xxSuccessful());
        }

        int pageSize = 5;
        String jsonResult =
                mockMvc.perform(
                                MockMvcRequestBuilders.get(RESERVATIONS_URI + "?guestId=1&page=0&size=" + pageSize)
                        )
                        .andExpect(status().is2xxSuccessful())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        List<Reservation> reservations = objectMapper.readValue(jsonResult, new TypeReference<>() {
        });

        assertEquals(5, reservations.size());
        List<Reservation> reservationsFromEarliestToLatest = reservations
                .stream()
                .sorted(Comparator.comparing(Reservation::getReservationDateTime)).toList();
        assertEquals(reservationsFromEarliestToLatest, reservations);
    }

    @Test
    @DisplayName("Can cancel existing reservation and get notified via SMS or email")
    @Order(4)
    public void canCancelReservation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(RESERVATIONS_URI + "/1"))
                .andExpect(status().isNoContent());

        verify(emailService).send(anyString());
        verifyNoInteractions(smsService);

        mockMvc.perform(MockMvcRequestBuilders.get(RESERVATIONS_URI + "/1"))
                .andExpect(status().isNotFound());
    }
}
