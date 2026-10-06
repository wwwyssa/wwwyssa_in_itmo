BEGIN;

CREATE TABLE coordinates (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    x REAL NOT NULL,
    y REAL NOT NULL,
    CONSTRAINT ck_coordinates_id_positive CHECK (id > 0),
    CONSTRAINT ck_coordinates_y_max CHECK (y <= 249)
);

CREATE TABLE house (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL,
    year BIGINT NOT NULL,
    number_of_floors INTEGER NOT NULL,
    CONSTRAINT ck_house_id_positive CHECK (id > 0),
    CONSTRAINT ck_house_year_positive CHECK (year > 0),
    CONSTRAINT ck_house_floors_positive CHECK (number_of_floors > 0)
);

CREATE TABLE flat (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT NOT NULL,
    coordinates_id BIGINT NOT NULL,
    creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    area BIGINT NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    balcony BOOLEAN,
    time_to_metro_on_foot REAL NOT NULL,
    number_of_rooms INTEGER NOT NULL,
    furniture BOOLEAN,
    view_type VARCHAR(16) NOT NULL,
    transport_type VARCHAR(16),
    house_id BIGINT,
    CONSTRAINT ck_flat_id_positive CHECK (id > 0),
    CONSTRAINT ck_flat_name_not_blank CHECK (name ~ '[^[:space:]]'),
    CONSTRAINT ck_flat_area_positive CHECK (area > 0),
    CONSTRAINT ck_flat_price_positive_finite CHECK (price > 0 AND price < 'Infinity'::DOUBLE PRECISION),
    CONSTRAINT ck_flat_metro_time_positive_finite CHECK (time_to_metro_on_foot > 0 AND time_to_metro_on_foot < 'Infinity'::REAL),
    CONSTRAINT ck_flat_rooms_range CHECK (number_of_rooms BETWEEN 1 AND 11),
    CONSTRAINT ck_flat_view CHECK (view_type IN ('STREET', 'YARD', 'PARK', 'BAD', 'TERRIBLE')),
    CONSTRAINT ck_flat_transport CHECK (transport_type IS NULL OR transport_type IN ('FEW', 'LITTLE', 'NORMAL')),
    CONSTRAINT fk_flat_coordinates FOREIGN KEY (coordinates_id) REFERENCES coordinates(id) ON DELETE RESTRICT,
    CONSTRAINT fk_flat_house FOREIGN KEY (house_id) REFERENCES house(id) ON DELETE RESTRICT
);
COMMIT;
