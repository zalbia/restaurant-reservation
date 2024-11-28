package zalbia.restaurant.booking.rest.api.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import zalbia.restaurant.booking.domain.CommunicationMethod;
import zalbia.restaurant.booking.domain.ReservationFixture;

public abstract class CommonApiTestFixture extends ReservationFixture {

    protected static final String RESERVATIONS_URI = "/api/v1.0/reservations";

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    protected static ReservationBookingRequest validReservationBookingRequest = new ReservationBookingRequest(
            null,
            "Customer",
            "+639170000000",
            "customer@example.com",
            futureDate,
            1,
            CommunicationMethod.EMAIL
    );
}
