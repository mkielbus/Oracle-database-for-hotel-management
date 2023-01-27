INSERT INTO departments
    VALUES (1, 'Kuchnia');
INSERT INTO departments
    VALUES (2, 'Kelnerka');
INSERT INTO departments
    VALUES (3, 'Recepcja');
INSERT INTO departments
    VALUES (4, 'Sprzątanie');
INSERT INTO departments
    VALUES (5, 'Administracja');


INSERT INTO employees
    VALUES (1, 'Barbara', 'Kowalska', To_DATE('2013-12-10'), 1);
INSERT INTO employees
    VALUES (2, 'Krzysztof', 'Miazga', TO_DATE('2020/11/05'), 3);
INSERT INTO employees
    VALUES (3, 'Julia', 'Przepiórka', TO_DATE('2022/07/25'), 5);
INSERT INTO employees
    VALUES (4, 'Filip', 'Poziomka', TO_DATE('2015/03/17'), 2);

INSERT INTO emp_schedule
    VALUES (1, TO_DATE('2023/01/28'), 3);
INSERT INTO emp_schedule
    VALUES (2, TO_DATE('2023/01/29'), 4);
INSERT INTO emp_schedule
    VALUES (3, TO_DATE('2023/01/25'), 4);
INSERT INTO emp_schedule
    VALUES (4, TO_DATE('2023/01/27'), 3);
INSERT INTO emp_schedule
    VALUES (5, TO_DATE('2023/01/26'), 2);
INSERT INTO emp_schedule
    VALUES (6, TO_DATE('2023/01/26'), 1);

INSERT INTO current_guests
    VALUES (1, 'Karol', 'Chocian');
INSERT INTO current_guests
    VALUES (2, 'Paulina', 'Fralina');
INSERT INTO current_guests
    VALUES (3, 'Katarzyna', 'Kózka');
INSERT INTO current_guests
    VALUES (4, 'Martyna', 'Bób');
INSERT INTO current_guests
    VALUES (5, 'Aleksandra', 'Bala');

INSERT INTO rooms
    VALUES (101, 4, 100, 0);
INSERT INTO rooms
    VALUES (102, 2, 70, 0);
INSERT INTO rooms
    VALUES (103, 2, 70, 0);
INSERT INTO rooms
    VALUES (104, 2, 70, 0);
INSERT INTO rooms
    VALUES (105, 3, 90, 0);
INSERT INTO rooms
    VALUES (106, 3, 90, 0);
INSERT INTO rooms
    VALUES (107, 5, 120, 0);

INSERT INTO reservations
    VALUES (1, 102, 1, 3);
INSERT INTO reservations
    VALUES (2,101, 2, 5);
INSERT INTO reservations
    VALUES (3, 106, 5, 1);
INSERT INTO reservations
    VALUES (4, 104, 4, 6);

INSERT INTO extra_service
    VALUES (1, 'Spa', 100);
INSERT INTO extra_service
    VALUES (2, 'Basen', 20);
INSERT INTO extra_service
    VALUES (3, 'Kręgielnia', 50);
INSERT INTO extra_service
    VALUES (4, 'Siłownia', 50);
INSERT INTO extra_service
    VALUES (5, 'Śniadanie', 20);

INSERT INTO guest_service
    VALUES (1, 1, 2);
INSERT INTO guest_service
    VALUES (1, 2, 3);
INSERT INTO guest_service
    VALUES (4, 4, 2);
INSERT INTO guest_service
    VALUES (5, 3, 1);
INSERT INTO guest_service
    VALUES (2, 2, 5);
INSERT INTO guest_service
    VALUES (2, 4, 1);

INSERT INTO renovations_history
    VALUES (1, 'Pomalowanie elewacji', DATE('2022'));
INSERT INTO renovations_history
    VALUES (2, 'Zakupienie sprzętu na wyposażenie kuchni', DATE('2018'));
INSERT INTO renovations_history
    VALUES (3, 'Wyłożenie basenu nowymi płytkami', DATE('2019'));

INSERT INTO budget
    VALUES (1, TO_DATE('2020', 'yyyy'), 1000000);
INSERT INTO budget
    VALUES (2, TO_DATE('2021', 'yyyy'), 1200000);
INSERT INTO budget
    VALUES (3, TO_DATE('2022', 'yyyy'), 1300000);
INSERT INTO budget
    VALUES (4, TO_DATE('2023', 'yyyy'), 1150000);


INSERT INTO guest_history
    VALUES (6, 'Patryk', 'Skorupa');
INSERT INTO guest_history
    VALUES (7, 'Tomasz', 'Odkrywca');
INSERT INTO guest_history
    VALUES (8, 'Karolina', 'Grabska');

INSERT INTO reservation_history
    VALUES (4, 6, 101, 4);
INSERT INTO reservation_history
    VALUES (5, 7, 102, 1);
INSERT INTO reservation_history
    VALUES (6, 7, 103, 3);
INSERT INTO reservation_history
    VALUES (7, 8, 102, 5);

INSERT INTO reviews
    VALUES (1, 4, 6);
INSERT INTO reviews
    VALUES (2, 5, 7);
INSERT INTO reviews
    VALUES (3, 2, 8);




