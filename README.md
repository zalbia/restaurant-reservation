# Restaurant Reservation System

## Design Decisions

- Although this is just an assignment, REST APIs evolve different versions so I prepend an API version number to the URLs.
- Spring Data JDBC over Spring Data JPA.
    - JPA often provides an unnecessary abstraction over the rich data access API SQL
      already provides, making it awkward, if not difficult, to make full use of the unique set of features a specific
      database vendor offers.
    - I have to do pagination myself.
- Support phone number validation using [libphonenumber](https://github.com/google/libphonenumber).
    - Phone numbers form a complex domain, and rolling out own validation is unnecessarily risky.
    - Delegating to the library leads to a more forgiving, less frustrating API.
- Use auto-incremented IDs in h2
    - Simple, suitable for a single node as is the case in this assignment.
- Use Java 21 records to cut down on the amount of boilerplate for this assignment.

## Assumptions Made

- Names can only be 1-100 characters long
- Local phone numbers are from the Philippines.
- There can only be up to 8 guests per reservation per group reservation policy.

## Nice to have
- Add discoverability (next, previous) to getting reservations
- Consider "idempotency" of creating reservations, e.g. only allowing one reservation under one name per day. 
