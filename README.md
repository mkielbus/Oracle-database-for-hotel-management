# BD1-project

## Autorzy

Mateusz Kiełbus
Alicja Kubiszyn
Sara Fojt

## Opis

Projekt polegał na opracowaniu prostej aplikacji wykorzystującej powłokę konsoli do obsługi zarządzania hotelem. Można między innymi dodawać gości, wymeldowywać gości oraz wyświetlać listę gości.

## Wymagania

System Linux, dystrybucja Ubuntu 22.04 LTS
Maven 3.8.8
Java 21

## Instalacja własnej instancji bazy danych

Instrukcja do instalacji własnej instancji bazy danych znajduje się pod adresem https://github.com/oracle/docker-images/tree/main/OracleDatabase/SingleInstance#how-to-build-and-run. Użyte komendy:

1. ./buildContainerImage.sh -v 19.3.0 -t oracle_database_19_3_0_s_e:oracle_db -s
2. sudo docker run --name oracle_db_19_3_0_s_e -d -p 1521:1521 -p 5500:5500 -p 2484:2484 --ulimit nofile=1024:65536 --ulimit nproc=2047:16384 --ulimit stack=10485760:33554432 --ulimit memlock=3221225472 -e ORACLE_SID=oracleSID -e ORACLE_PDB=oraclePDB -e ORACLE_PWD=password -e ORACLE_EDITION=standard oracle_database_19_3_0_s_e:oracle_db

## Uruchomienie

Przygotowany projekt jest obsługiwany przez narzędzie do budowania projektów Maven. Należy używać poleceń Mavena do kontroli procesu budowy projektu.

## Skrypty SQL

Skryptów należy użyć w następującej kolejności (można je wywołać na zainstalowanej instancji bazy danych np. za pomocą narzędzia Sqldeveloper):

1. script.sql
2. insert_data.sql
3. functionality.sql
