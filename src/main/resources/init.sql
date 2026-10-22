CREATE TABLE IF NOT EXISTS trips (
    id             BIGINT         PRIMARY KEY,
    route          VARCHAR(100)   NOT NULL,
    trip_type      VARCHAR(15)    NOT NULL,
    departure_time TIMESTAMP      NOT NULL,
    price          NUMERIC(10, 2) NOT NULL,
    stops_count    INT,
    wifi           BOOLEAN,
    country        VARCHAR(50),
    border_fee     NUMERIC(10, 2),
    CONSTRAINT uq_trips_route_time UNIQUE (route, departure_time),
    CONSTRAINT chk_trips_type CHECK (trip_type IN ('SUBURBAN', 'INTERCITY', 'INTERNATIONAL')),
    CONSTRAINT chk_trips_price CHECK (price > 0),
    CONSTRAINT chk_trips_fields CHECK (
        (trip_type = 'SUBURBAN' AND stops_count >= 0 AND wifi IS NULL AND country IS NULL AND border_fee IS NULL)
        OR (trip_type = 'INTERCITY' AND stops_count IS NULL AND wifi IS NOT NULL AND country IS NULL AND border_fee IS NULL)
        OR (trip_type = 'INTERNATIONAL' AND stops_count IS NULL AND wifi IS NOT NULL AND country IS NOT NULL AND border_fee >= 0)
    )
);

CREATE TABLE IF NOT EXISTS bookings (
    id             BIGINT       PRIMARY KEY,
    passenger_name VARCHAR(100) NOT NULL,
    trip_id        BIGINT       NOT NULL,
    seats          INT          NOT NULL,
    status         VARCHAR(10)  NOT NULL DEFAULT 'NEW',
    CONSTRAINT chk_bookings_seats CHECK (seats > 0),
    CONSTRAINT chk_bookings_status CHECK (status IN ('NEW', 'PAID', 'BOARDED', 'COMPLETED', 'CANCELLED')),
    CONSTRAINT fk_bookings_trip FOREIGN KEY (trip_id) REFERENCES trips (id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS trip_seats (
    trip_id    BIGINT PRIMARY KEY,
    free_seats INT    NOT NULL,
    CONSTRAINT chk_trip_seats_free CHECK (free_seats >= 0),
    CONSTRAINT fk_trip_seats_trip FOREIGN KEY (trip_id) REFERENCES trips (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_bookings_trip ON bookings (trip_id);
CREATE INDEX IF NOT EXISTS idx_trips_route ON trips (route);
