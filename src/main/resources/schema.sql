CREATE SEQUENCE guest_id_seq;

CREATE TABLE reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    guest_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL CHECK (LENGTH(name) BETWEEN 1 AND 100),
    phone_number VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    preferred_communication_method VARCHAR(20) NOT NULL CHECK (preferred_communication_method IN ('EMAIL', 'SMS')),
    reservation_date_time DATETIME NOT NULL,
    number_of_guests INT NOT NULL CHECK (number_of_guests BETWEEN 1 AND 8),
    is_cancelled BOOLEAN DEFAULT FALSE
);