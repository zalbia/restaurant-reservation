package zalbia.restaurant.booking.domain;

public interface CustomerBookingService {
    public Reservation bookReservation(BookReservationParams params);

    public void cancelReservation(Long reservationId);
}
