# Restaurant Reservation System

Umpisa Technical Project submission by Zachary Albia.

## Running

To run the API, run `./gradlew bootRun` and go to the [Swagger UI](http://localhost:8080/swagger-ui/index.html).

## Testing

To run all tests, run `./gradlew test --info`.

[CustomerBookingIntegrationTests](src/test/java/zalbia/restaurant/booking/rest/api/v1/CustomerBookingIntegrationTests.java)
is a `@SpringBootTest` which contains ordered integration tests that cover the web layer down to the data layer.

[InternalCustomerBookingErrorHandlerTests](src/test/java/zalbia/restaurant/booking/rest/api/v1/InternalCustomerBookingErrorHandlerTests.java)
is a `@WebMvcTest` which contains internal error handling at the web layer for domain layer validation errors.

[ReservationBookingValidatorTests](src/test/java/zalbia/restaurant/booking/domain/ReservationBookingValidatorTests.java)
is a plain unit for validating reservation booking.

[ReservationTests](src/test/java/zalbia/restaurant/booking/domain/ReservationTests.java) is a plain unit test for
reservation update validation.

## Design Decisions

- Top-down approach, define documented API then work way down.
- Although this is just an assignment, REST APIs evolve different versions, so I prepend an API version number to the
  URLs.
- Spring Data JDBC over Spring Data JPA for less complexity.
- Support phone number validation using [libphonenumber](https://github.com/google/libphonenumber).
    - Phone numbers form a complex domain, and rolling out own validation is unnecessary.
    - Delegating to the library leads to a more forgiving phone number API argument.
- Distinct validations at the API layer and domain layer.
- Use auto-incremented IDs in h2
    - Simple, suitable for a single node as is the case in this assignment.
- Use Java 21 records to cut down on the amount of boilerplate.
    - Great for DTOs.
    - Great for value objects such
      as [ReservationBooking](src/main/java/zalbia/restaurant/booking/domain/ReservationBooking.java).
- To keep scope in check, booking reservations returns an auto-incremented guest ID for managing reservations instead of
  managing a guest entity.
    - In a production system, guest IDs would just be provided and would probably be managed elsewhere.
- Created an exception hierarchy for internal customer booking errors.
    - These runtime errors are allowed to bubble up to the web layer and handled by returning appropriate HTTP
      responses.
- Keep domain and web layer separate with DTOs and value objects, maintaining strict boundaries.
- Cancelling a reservation soft-deletes it. Cancelling cancelled reservations returns 404.
- Barebones pagination for viewing upcoming reservations from earliest to latest.
- No HATEOAS.

## Assumptions Made

- Names can only be 1-100 characters long.
- Local phone numbers are from the Philippines.
- There can be only be 1-8 guests per reservation.
- All dates and times are local.
- There isn't a definite limit to the total number of reservations booked for a restaurant.
- Restaurant is open 24/7. 😱
- Upcoming reservations are viewed from earliest to latest.
- Names, phone numbers, and emails can be duplicated.
- Guests (identified by guest ID) can have duplicate reservation for an exact 🤔 date time.

## Nice to have

- Add discoverability (next, previous, etc.) to getting paginated reservations
- Consider "idempotency" of creating reservations, e.g. only allowing one reservation under one name per day. 
