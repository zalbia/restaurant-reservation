package zalbia.restaurant.booking.domain;

import java.time.LocalDateTime;

public abstract class ReservationFixture {
    protected static final LocalDateTime futureDate =
            LocalDateTime.of(3100, 1, 1, 0, 0);

    protected static final Reservation validReservation = new Reservation(
            1L,
            1L,
            "Customer",
            "+639170000000",
            "customer@example.com",
            futureDate,
            1,
            CommunicationMethod.EMAIL
    );

    protected static final Reservation smsReservation = new Reservation(
            11L,
            1L,
            "Customer",
            "+639170000000",
            "customer@example.com",
            futureDate,
            1,
            CommunicationMethod.SMS
    );
}
