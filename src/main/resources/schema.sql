CREATE TABLE reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    guest_id BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    preferred_communication_method VARCHAR(20) NOT NULL,
    reservation_date_time DATETIME NOT NULL,
    number_of_guests INT NOT NULL,
    is_cancelled BOOLEAN NOT NULL DEFAULT FALSE
);