# Restaurant Reservation System

## TODO

- Add more OpenAPI documentation
- Implement services
- Integration Tests!
- Implement quartz scheduler logic for reminder jobs

## Design Decisions

- Top-down approach, define documented API then work way down.
- Although this is just an assignment, REST APIs evolve different versions so I prepend an API version number to the
  URLs.
- Spring Data JDBC over Spring Data JPA.
    - JPA often provides an overly leaky abstraction over the rich data access API SQL
      already provides, making it awkward, if not difficult, to make full use of the unique set of features a specific
      database vendor offers.
    - Tradeoff is finer control but having to do ORM manually.
- Support phone number validation using [libphonenumber](https://github.com/google/libphonenumber).
    - Phone numbers form a complex domain, and rolling out own validation is unnecessary.
    - Delegating to the library leads to a more forgiving phone number API argument.
- Distinct validations at the API layer and domain layer.
- Use auto-incremented IDs in h2
    - Simple, suitable for a single node as is the case in this assignment.
- Use Java 21 records to cut down on the amount of boilerplate for this assignment.
- To keep scope in check for this assignment, booking reservations returns an auto-incremented guest ID for managing
  reservations instead of managing a guest entity.
  - In a bigger system, guest IDs would just be provided.
- Limited logging only for client-friendly error messages for the API, and appropriate response status codes.
- Created an exception hierarchy for internal customer booking errors.
  - These runtime errors are allowed to bubble up to the web layer and handled accordingly.

## Assumptions Made

- Names can only be 1-100 characters long.
- Local phone numbers are from the Philippines.
- There can only be up to 8 guests per reservation per group reservation policy.
- All dates and times are local.
- There isn't a definite limit to the total number of reservations booked for a restaurant.
- Restaurant is open 24/7. 😱

## Nice to have

- Add discoverability (next, previous, etc.) to getting paginated reservations
- Consider "idempotency" of creating reservations, e.g. only allowing one reservation under one name per day. 
