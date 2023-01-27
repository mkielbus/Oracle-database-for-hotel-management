create sequence orderSequence start with 0 maxvalue 50 minvalue 0 increment by 1 cycle cache 50;

create or replace trigger review_description_check
before
insert or update of review_description on reviews
for each row
when (new.review_description not between 1 and 5)
declare
review_value reviews.review_description%TYPE;
begin
review_value := :new.review_description;
raise_application_error(-20001, 'Podana wartość recenzji: ' || review_value ||  ' nie zawiera się w przedziale od zera do pięciu!');
end;
/

create or replace trigger add_reservation
after
insert on reservations
for each row
declare
begin
update rooms set occupied = '1' where room_id = :new.room_id;
end;
/

create or replace trigger extra_service_check
before
insert or update of cost on extra_service
for each row
when (new.cost < 0)
declare
service_cost extra_service.cost%TYPE;
begin
service_cost := :new.cost;
raise_application_error(-20002, 'Podany koszt usługi: ' || service_cost || ' jest ujemny!');
end;
/

create or replace trigger add_to_guest_history
after
delete on current_guests
for each row
declare
max_id guest_history.guest_id%TYPE;
begin
select max(guest_id) into max_id from guest_history;
insert into guest_history values (max_id + 1, :old.name, :old.surname);
end;
/

create or replace trigger add_to_reservation_history
after
delete on reservations
for each row
declare
removed_guest_id current_guests.guest_id%TYPE;
reservation reservations%ROWTYPE;
res_id reservation_history.reservation_id%TYPE;
cond Integer default 0;
begin
removed_guest_id := :old.guest_id;
select max(reservation_id) into res_id from reservation_history;
insert into reservation_history values (res_id + 1, :old.room_id, :old.guest_id, :old.stay_length);
update rooms set occupied = '0' where room_id = :old.room_id;
end;
/

--create or replace trigger check_delete_rooms
--before
--delete on rooms
--for each row
--declare
--cursor reservation is select * from reservations;
--reservation_data reservations%ROWTYPE;
--begin
--open reservation;
--loop
--exit when reservation%NOTFOUND;
--fetch reservation into reservation_data;
--if :old.room_id = reservation_data.room_id then
--raise_application_error(-20004, 'Nie można usunąć pokoju, który znajduje się w tabeli rezerwacje!');
--end if;
--end loop;
--close reservation;
--end;
--/


--create or replace trigger check_add_to_guest_history
--before
--insert on guest_history
--for each row
--declare 
--cursor guest is select * from current_guests;
--guest_data current_guests%ROWTYPE;
--begin
--open guest;
--loop
--exit when guest%NOTFOUND;
--fetch guest into guest_data;
--if (:new.guest_id = guest_data.guest_id) then
--raise_application_error(-20003, 'Podany gość jest w trakcie pobytu w hotelu!');
--end if;
--end loop;
--close guest;
--end;
--/

create or replace procedure show_shifts(employee_id employees.employee_id%TYPE)
as
cursor shifts is select * from emp_schedule;
shifts_data emp_schedule%ROWTYPE;
begin
open shifts;
loop
exit when shifts%NOTFOUND;
fetch shifts into shifts_data;
if shifts_data.employee_id = employee_id then
dbms_output.put_line('Id zmiany: ' || shifts_data.shift_id || ' data zmiany: ' || shifts_data.shift_date);
end if;
end loop;
close shifts;
end;
/

create or replace procedure show_employees
as
cursor employee is select * from employees;
employees_data employees%ROWTYPE;
begin
open employee;
loop
exit when employee%NOTFOUND;
fetch employee into employees_data;
dbms_output.put_line('Id: ' || employees_data.employee_id || ' Imię: ' || employees_data.name || ' Nazwisko: ' || employees_data.surname || ' Data zatrudnienia: ' || employees_data.employment_date || ' Id departamentu: ' || employees_data.department_id);
end loop;
close employee;
end;
/

create or replace function getCostForGuest(guest_id_param current_guests.guest_id%TYPE) return Number 
as
stay_cost Number default 0;
room_cost Number default 0;
services_cost Number default 0;
begin
select sum(r.cost * res.stay_length) as room_cost into room_cost from current_guests g inner join reservations res on (g.guest_id = res.guest_id) inner join rooms r on (res.room_id = r.room_id) where g.guest_id = guest_id_param;
select sum(ex_serv.cost * g_serv.quantity) as services_cost into services_cost from current_guests g inner join guest_service g_serv on (g.guest_id = g_serv.guest_id) inner join extra_service ex_serv on (g_serv.service_id = ex_serv.service_id) where g.guest_id = guest_id_param;
stay_cost := room_cost + services_cost;
return stay_cost;
end;
/

create or replace function number_of_rooms_occupied return Integer
as
counter Integer;
begin
select count(*) into counter from rooms where occupied = '1';
return counter;
end;
/

