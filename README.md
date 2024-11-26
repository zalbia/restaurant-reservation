# Restaurant Reservation System

## Design Decisions

- Opted for Spring Data JDBC over Spring JPA.
    - My personal opinion is that JPA provides an unnecessary abstraction over the rich data access API SQL APIs
      already provide, making it awkward, if not difficult, to make full use of the unique set of features a specific
      database offers.
- Support phone number validation using [libphonenumber](https://github.com/google/libphonenumber).
    - Phone numbers form a complex domain, and rolling out own validation is unnecessarily risky.
    - Delegating to the library leads to a more forgiving, less frustrating API.

## Assumptions Made

- Names can only be 1-100 characters long
- Local phone numbers are from the Philippines.
- There can only be up to 8 guests per reservation per group reservation policy.
