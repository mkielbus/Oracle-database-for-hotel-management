CREATE TABLE budget (
    budget_id INTEGER NOT NULL,
    year      DATE NOT NULL,
    budget    INTEGER NOT NULL
);

ALTER TABLE budget ADD CONSTRAINT budget_pk PRIMARY KEY ( budget_id );

CREATE TABLE current_guests (
    guest_id INTEGER NOT NULL,
    name     VARCHAR2(30 CHAR),
    surname  VARCHAR2(30 CHAR) NOT NULL
);

ALTER TABLE current_guests ADD CONSTRAINT current_guests_pk PRIMARY KEY ( guest_id );

CREATE TABLE departments (
    department_id INTEGER NOT NULL,
    name          VARCHAR2(30 CHAR) NOT NULL
);

ALTER TABLE departments ADD CONSTRAINT departments_pk PRIMARY KEY ( department_id );

CREATE TABLE emp_schedule (
    shift_id    INTEGER NOT NULL,
    shift_date  DATE NOT NULL,
    employee_id INTEGER NOT NULL
);

ALTER TABLE emp_schedule ADD CONSTRAINT emp_schedule_pk PRIMARY KEY ( shift_id );

CREATE TABLE employees (
    employee_id     INTEGER NOT NULL,
    name            VARCHAR2(30 CHAR) NOT NULL,
    surname         VARCHAR2(30) NOT NULL,
    employment_date DATE,
    department_id   INTEGER
);

ALTER TABLE employees ADD CONSTRAINT employees_pk PRIMARY KEY ( employee_id );

CREATE TABLE extra_service (
    service_id INTEGER NOT NULL,
    name       VARCHAR2(30 CHAR) NOT NULL,
    cost       INTEGER NOT NULL
);

ALTER TABLE extra_service ADD CONSTRAINT extra_service_pk PRIMARY KEY ( service_id );

CREATE TABLE guest_history (
    guest_id INTEGER NOT NULL,
    name     VARCHAR2(30 CHAR),
    surname  VARCHAR2(30 CHAR) NOT NULL
);

ALTER TABLE guest_history ADD CONSTRAINT guest_history_pk PRIMARY KEY ( guest_id );

CREATE TABLE guest_service (
    service_id INTEGER NOT NULL,
    guest_id   INTEGER NOT NULL,
    quantitiy  INTEGER NOT NULL
);

ALTER TABLE guest_service ADD CONSTRAINT guest_service_pk PRIMARY KEY ( service_id,
                                                                        guest_id );

CREATE TABLE renovations_history (
    renovation_id   INTEGER NOT NULL,
    description     VARCHAR2(30 CHAR) NOT NULL,
    renovation_year DATE
);

ALTER TABLE renovations_history ADD CONSTRAINT renovations_history_pk PRIMARY KEY ( renovation_id );

CREATE TABLE reservation_history (
    reservation_id INTEGER NOT NULL,
    guest_id       INTEGER NOT NULL,
    room_id        INTEGER NOT NULL,
    stay_length    INTEGER NOT NULL
);

ALTER TABLE reservation_history ADD CONSTRAINT reservation_history_pk PRIMARY KEY ( reservation_id );

CREATE TABLE reservations (
    reservation_id INTEGER NOT NULL,
    room_id        INTEGER NOT NULL,
    guest_id       INTEGER NOT NULL,
    stay_length    INTEGER NOT NULL
);

ALTER TABLE reservations
    ADD CONSTRAINT reservations_pk PRIMARY KEY ( room_id,
                                                 guest_id,
                                                 reservation_id );

CREATE TABLE reviews (
    review_id          INTEGER NOT NULL,
    review_description INTEGER,
    guest_id           INTEGER NOT NULL
);

ALTER TABLE reviews ADD CONSTRAINT reviews_pk PRIMARY KEY ( review_id );

CREATE TABLE rooms (
    room_id         INTEGER NOT NULL,
    people_capacity INTEGER NOT NULL,
    cost            INTEGER NOT NULL,
    occupied        CHAR(1) NOT NULL
);

ALTER TABLE rooms ADD CONSTRAINT rooms_pk PRIMARY KEY ( room_id );

ALTER TABLE guest_service
    ADD CONSTRAINT current_guest_fk FOREIGN KEY ( guest_id )
        REFERENCES current_guests ( guest_id );

ALTER TABLE reservations
    ADD CONSTRAINT current_guests_fk FOREIGN KEY ( guest_id )
        REFERENCES current_guests ( guest_id );

ALTER TABLE employees
    ADD CONSTRAINT departments_fk FOREIGN KEY ( department_id )
        REFERENCES departments ( department_id );

ALTER TABLE emp_schedule
    ADD CONSTRAINT employees_fk FOREIGN KEY ( employee_id )
        REFERENCES employees ( employee_id );

ALTER TABLE guest_service
    ADD CONSTRAINT extra_service_fk FOREIGN KEY ( service_id )
        REFERENCES extra_service ( service_id );

ALTER TABLE reviews
    ADD CONSTRAINT guest_history_fk FOREIGN KEY ( guest_id )
        REFERENCES guest_history ( guest_id );

ALTER TABLE reservations
    ADD CONSTRAINT rooms_fk FOREIGN KEY ( room_id )
        REFERENCES rooms ( room_id );

ALTER TABLE departments
    ADD UNIQUE (name);

ALTER TABLE extra_service
    ADD UNIQUE (name);
