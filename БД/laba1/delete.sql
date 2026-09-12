CREATE TABLE person (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    birth_date DATE CHECK (birth_date <= CURRENT_DATE),
    current_location_id INTEGER REFERENCES location(id),
);

CREATE TABLE location (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(100) NOT NULL
);


CREATE TABLE transport (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    current_location_id INTEGER REFERENCES location(id),
);

CREATE TABLE message (
    id SERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    sender_id INTEGER REFERENCES Person(id) ON DELETE SET NULL,
    receiver_id INTEGER REFERENCES Person(id) ON DELETE SET NULL,
    sent_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CHECK (sender_id != receiver_id)
);


CREATE TABLE communication (
    person_id INTEGER REFERENCES person(id) ON DELETE CASCADE,
    message_id REFERENCES message(id) ON DELETE CASCADE,
    PRIMARY KEY (person_id, message_id)
);


CREATE TABLE emotionalstatus (
    id SERIAL PRIMARY KEY,
    person_id INTEGER REFERENCES person(id) ON DELETE CASCADE,
    emotion_status VARCHAR(50) CHECK (emotion_status IN ('Happy', 'Sad', 'Angry', 'Neutral')),
    CHECK (person_id IS NOT NULL)
);

CREATE TABLE movement (
    id SERIAL PRIMARY KEY,
    person_id INTEGER REFERENCES person(id) ON DELETE CASCADE,
    transport_id INTEGER REFERENCES transport(id) ON DELETE CASCADE,
    to_location_id INTEGER REFERENCES location(id) ON DELETE CASCADE,
    CHECK (person_id IS NOT NULL),
    CHECK (transport_id IS NOT NULL),
    CHECK (to_location_id IS NOT NULL)
);


CREATE TABLE ride (
    movement_id INTEGER,
    location_id INTEGER,
    FOREIGN KEY (movement_id) REFERENCES movement(id) ON DELETE CASCADE,
    FOREIGN KEY (location_id) REFERENCES location(id) ON DELETE CASCADE,
    PRIMARY KEY (movement_id, location_id)
);


CREATE TABLE personloc (
    person_id INTEGER,
    loc_id INTEGER,
    FOREIGN KEY (person_id) REFERENCES person(id) ON DELETE CASCADE,
    FOREIGN KEY (loc_id) REFERENCES location(id) ON DELETE CASCADE,
    PRIMARY KEY (person_id, loc_id)
);
