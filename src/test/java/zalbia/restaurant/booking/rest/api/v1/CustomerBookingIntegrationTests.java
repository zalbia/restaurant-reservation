package zalbia.restaurant.booking.rest.api.v1;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import zalbia.restaurant.booking.domain.CommunicationMethod;
import zalbia.restaurant.booking.infra.EmailService;
import zalbia.restaurant.booking.infra.SmsService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CustomerBookingIntegrationTests extends CommonApiTestFixture {

    @MockitoBean
    SmsService smsService;

    @MockitoBean
    EmailService emailService;

    @MockitoBean
    Scheduler scheduler;

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
                        .andExpect(status().isBadRequest())
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
    @DisplayName("Can book reservation, get notified via SMS or email with reminder scheduled 4 hours before")
    @Order(1)
    public void canBookReservation() throws Exception {
        String requestAsJson = objectMapper.writeValueAsString(validReservationBookingRequest);
        String expectedJson = objectMapper.writeValueAsString(reservationResponseBody);

        mockMvc.perform(MockMvcRequestBuilders.post(RESERVATIONS_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        verify(emailService).send(anyString(), eq(validReservationBookingRequest.email()));
        verifyNoInteractions(smsService);

        ArgumentCaptor<JobDetail> jobDetailArgumentCaptor = ArgumentCaptor.forClass(JobDetail.class);
        ArgumentCaptor<Trigger> triggerArgumentCaptor = ArgumentCaptor.forClass(Trigger.class);
        verify(scheduler).scheduleJob(jobDetailArgumentCaptor.capture(), triggerArgumentCaptor.capture());

        String key = jobDetailArgumentCaptor.getValue().getKey().getName();
        assertEquals(validReservation.getId(), Long.valueOf(key));

        Date startTime = triggerArgumentCaptor.getValue().getStartTime();
        LocalDateTime startDateTime = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        assertEquals(validReservation.getReservationDateTime().minusHours(4), startDateTime);
    }

    @Test
    @DisplayName("Can get reservations given guest ID")
    @Order(2)
    public void canGetGuestReservations() throws Exception {
        String expectedJson = objectMapper.writeValueAsString(List.of(reservationResponseBody));

        mockMvc.perform(MockMvcRequestBuilders.get(RESERVATIONS_URI + "?guestId=1&page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        mockMvc.perform(MockMvcRequestBuilders.get(RESERVATIONS_URI + "?guestId=404&page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    @DisplayName("Can get reservation by reservation ID")
    @Order(2)
    public void canGetReservationById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(RESERVATIONS_URI + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(reservationResponseBody)));

        String nonExistentReservationID = "404";
        mockMvc.perform(MockMvcRequestBuilders.get(RESERVATIONS_URI + "/" + nonExistentReservationID))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    @DisplayName("Can get guest reservations by page from earliest to latest")
    @Order(2)
    public void canGetGuestReservationsByPage() throws Exception {
        // add more guest reservations
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
                    .andExpect(status().isOk());
        }

        int pageSize = 5;
        MockHttpServletRequestBuilder mockRequest =
                MockMvcRequestBuilders.get(RESERVATIONS_URI + "?guestId=1&page=0&size=" + pageSize);
        String jsonResult = mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<ReservationResponseBody> reservations = objectMapper.readValue(jsonResult, new TypeReference<>() {
        });

        assertEquals(pageSize, reservations.size());
        List<ReservationResponseBody> reservationsFromEarliestToLatest = reservations
                .stream()
                .sorted(Comparator.comparing(ReservationResponseBody::reservationDateTime)).toList();
        assertEquals(reservationsFromEarliestToLatest, reservations);
    }

    @Test
    @DisplayName("Invalid updates return validation errors")
    @Order(2)
    public void invalidUpdatesReturnValidationErrors() throws Exception {
        UpdateReservationRequest invalidRequest = new UpdateReservationRequest(
                LocalDateTime.now().minusHours(4),
                0
        );
        String invalidRequestJson = objectMapper.writeValueAsString(invalidRequest);

        mockMvc.perform(MockMvcRequestBuilders.patch(RESERVATIONS_URI + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequestJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.newReservationDateTime").isString())
                .andExpect(jsonPath("$.newNumberOfGuests").isString());
    }

    @Test
    @DisplayName("Can update reservation datetime and number of guests")
    @Order(3)
    public void canUpdateReservation() throws Exception {
        LocalDateTime newReservationDateTime = futureDate.plusHours(4);
        int newNumberOfGuests = 8;
        UpdateReservationRequest updateRequest = new UpdateReservationRequest(newReservationDateTime, newNumberOfGuests);
        String updateRequestJson = objectMapper.writeValueAsString(updateRequest);

        ReservationResponseBody expectedResponse = new ReservationResponseBody(
                1L,
                1L,
                "Customer",
                "+639170000000",
                "customer@example.com",
                CommunicationMethod.EMAIL,
                newReservationDateTime,
                newNumberOfGuests,
                false
        );
        String expectedJson = objectMapper.writeValueAsString(expectedResponse);
        mockMvc.perform(MockMvcRequestBuilders.patch(RESERVATIONS_URI + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequestJson))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    @DisplayName("Can cancel existing reservation and get notified via SMS or email")
    @Order(4)
    public void canCancelReservation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(RESERVATIONS_URI + "/1"))
                .andExpect(status().isNoContent());

        verify(emailService).send(anyString(), eq(validReservationBookingRequest.email()));
        verifyNoInteractions(smsService);

        mockMvc.perform(MockMvcRequestBuilders.get(RESERVATIONS_URI + "/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Cancelling cancelled reservations ")
    @Order(5)
    public void cancellingCancelledReservations() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(RESERVATIONS_URI + "/1"))
                .andExpect(status().isNotFound());

        verifyNoInteractions(emailService);
        verifyNoInteractions(smsService);
    }

    @Test
    @DisplayName("Can get notifications by SMS with a reminder scheduled for four hours before")
    @Order(6)
    public void canGetSmsNotification() throws Exception {
        String requestAsJson = objectMapper.writeValueAsString(smsReservationBookingRequest);

        mockMvc.perform(MockMvcRequestBuilders.post(RESERVATIONS_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestAsJson))
                .andExpect(status().isOk());

        verify(smsService).send(anyString(), eq(smsReservationBookingRequest.phoneNumber()));
        verifyNoInteractions(emailService);
    }
}
