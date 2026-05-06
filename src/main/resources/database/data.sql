SET session_replication_role = 'replica';

INSERT INTO collectivity (id, unique_number, unique_name, location, specialty) VALUES
                                                                                   ('col-1', '1', 'Mpanorina', 'Ambatondrazaka', 'Riziculture'),
                                                                                   ('col-2', '2', 'Dobo voalahany', 'Ambatondrazaka', 'Pisciculture'),
                                                                                   ('col-3', '3', 'Tantely mamy', 'Brickaville', 'Apiculture');

INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, registration_date, occupation, collectivity_id) VALUES
                                                                                                                                                                 ('C1-M1', 'Nom membre 1', 'Prénom membre 1', '1980-02-01', 'MALE', 'Lot II V M Ambato.', 'Riziculteur', '0341234567', 'member.1@fed-agri.mg', '2025-01-01', 'PRESIDENT', 'col-1'),
                                                                                                                                                                 ('C1-M2', 'Nom membre 2', 'Prénom membre 2', '1982-03-05', 'MALE', 'Lot II F Ambato.', 'Agriculteur', '0321234567', 'member.2@fed-agri.mg', '2025-01-01', 'VICE_PRESIDENT', 'col-1'),
                                                                                                                                                                 ('C1-M3', 'Nom membre 3', 'Prénom membre 3', '1992-03-10', 'MALE', 'Lot II J Ambato.', 'Collecteur', '0331234567', 'member.3@fed-agri.mg', '2025-01-01', 'SECRETARY', 'col-1'),
                                                                                                                                                                 ('C1-M4', 'Nom membre 4', 'Prénom membre 4', '1988-05-22', 'FEMALE', 'Lot A K 50 Ambato.', 'Distributeur', '0381234567', 'member.4@fed-agri.mg', '2025-01-01', 'TREASURER', 'col-1'),
                                                                                                                                                                 ('C1-M5', 'Nom membre 5', 'Prénom membre 5', '1999-08-21', 'MALE', 'Lot UV 80 Ambato.', 'Riziculteur', '0373434567', 'member.5@fed-agri.mg', '2025-01-01', 'CONFIRMED', 'col-1'),
                                                                                                                                                                 ('C1-M6', 'Nom membre 6', 'Prénom membre 6', '1998-08-22', 'FEMALE', 'Lot UV 6 Ambato.', 'Riziculteur', '0372234567', 'member.6@fed-agri.mg', '2025-01-01', 'CONFIRMED', 'col-1'),
                                                                                                                                                                 ('C1-M7', 'Nom membre 7', 'Prénom membre 7', '1998-01-31', 'MALE', 'Lot UV 7 Ambato.', 'Riziculteur', '0374234567', 'member.7@fed-agri.mg', '2025-01-01', 'CONFIRMED', 'col-1'),
                                                                                                                                                                 ('C1-M8', 'Nom membre 8', 'Prénom membre 8', '1975-08-20', 'MALE', 'Lot UV 8 Ambato.', 'Riziculteur', '0370234567', 'member.8@fed-agri.mg', '2025-01-01', 'CONFIRMED', 'col-1');

INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, registration_date, occupation, collectivity_id) VALUES
                                                                                                                                                                 ('C2-M1', 'Nom membre 1', 'Prénom membre 1', '1980-02-01', 'MALE', 'Lot II V M Ambato.', 'Riziculteur', '0341234567', 'member.1@fed-agri.mg', '2025-01-01', 'CONFIRMED', 'col-2'),
                                                                                                                                                                 ('C2-M2', 'Nom membre 2', 'Prénom membre 2', '1982-03-05', 'MALE', 'Lot II F Ambato.', 'Agriculteur', '0321234567', 'member.2@fed-agri.mg', '2025-01-01', 'CONFIRMED', 'col-2'),
                                                                                                                                                                 ('C2-M3', 'Nom membre 3', 'Prénom membre 3', '1992-03-10', 'MALE', 'Lot II J Ambato.', 'Collecteur', '0331234567', 'member.3@fed-agri.mg', '2025-01-01', 'CONFIRMED', 'col-2'),
                                                                                                                                                                 ('C2-M4', 'Nom membre 4', 'Prénom membre 4', '1988-05-22', 'FEMALE', 'Lot A K 50 Ambato.', 'Distributeur', '0381234567', 'member.4@fed-agri.mg', '2025-01-01', 'CONFIRMED', 'col-2'),
                                                                                                                                                                 ('C2-M5', 'Nom membre 5', 'Prénom membre 5', '1999-08-21', 'MALE', 'Lot UV 80 Ambato.', 'Riziculteur', '0373434567', 'member.5@fed-agri.mg', '2025-01-01', 'PRESIDENT', 'col-2'),
                                                                                                                                                                 ('C2-M6', 'Nom membre 6', 'Prénom membre 6', '1998-08-22', 'FEMALE', 'Lot UV 6 Ambato.', 'Riziculteur', '0372234567', 'member.6@fed-agri.mg', '2025-01-01', 'VICE_PRESIDENT', 'col-2'),
                                                                                                                                                                 ('C2-M7', 'Nom membre 7', 'Prénom membre 7', '1998-01-31', 'MALE', 'Lot UV 7 Ambato.', 'Riziculteur', '0372434567', 'member.7@fed-agri.mg', '2025-01-01', 'SECRETARY', 'col-2'),
                                                                                                                                                                 ('C2-M8', 'Nom membre 8', 'Prénom membre 8', '1975-08-20', 'MALE', 'Lot UV 8 Ambato.', 'Riziculteur', '0370234567', 'member.8@fed-agri.mg', '2025-01-01', 'TREASURER', 'col-2');

INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, registration_date, occupation, collectivity_id) VALUES
                                                                                                                                                                 ('C3-M1', 'Nom membre 9', 'Prénom membre 9', '1988-01-02', 'MALE', 'Lot 33 J Antisirabe', 'Apiculteur', '034034567', 'member.9@fed-agri.mg', '2025-01-01', 'PRESIDENT', 'col-3'),
                                                                                                                                                                 ('C3-M2', 'Nom membre 10', 'Prénom membre 10', '1982-03-05', 'MALE', 'Lot 2 J Antisirabe', 'Agriculteur', '0338634567', 'member.10@fed-agri.mg', '2025-01-01', 'VICE_PRESIDENT', 'col-3'),
                                                                                                                                                                 ('C3-M3', 'Nom membre 11', 'Prénom membre 11', '1992-03-12', 'MALE', 'Lot 8 KM Antisirabe', 'Collecteur', '0338234567', 'member.11@fed-agri.mg', '2025-01-01', 'SECRETARY', 'col-3'),
                                                                                                                                                                 ('C3-M4', 'Nom membre 12', 'Prénom membre 12', '1988-05-10', 'FEMALE', 'Lot A K 50 Antisirabe', 'Distributeur', '0382334567', 'member.12@fed-agri.mg', '2025-01-01', 'TREASURER', 'col-3'),
                                                                                                                                                                 ('C3-M5', 'Nom membre 13', 'Prénom membre 13', '1999-08-11', 'MALE', 'Lot UV 80 Antisirabe', 'Apiculteur', '0373365567', 'member.13@fed-agri.mg', '2025-01-01', 'CONFIRMED', 'col-3'),
                                                                                                                                                                 ('C3-M6', 'Nom membre 14', 'Prénom membre 14', '1998-08-09', 'FEMALE', 'Lot UV 6 Antisirabe', 'Apiculteur', '0378234567', 'member.14@fed-agri.mg', '2025-01-01', 'CONFIRMED', 'col-3'),
                                                                                                                                                                 ('C3-M7', 'Nom membre 15', 'Prénom membre 15', '1998-01-13', 'MALE', 'Lot UV 7 Antisirabe', 'Apiculteur', '0374914567', 'member.15@fed-agri.mg', '2025-01-01', 'CONFIRMED', 'col-3'),
                                                                                                                                                                 ('C3-M8', 'Nom membre 16', 'Prénom membre 16', '1975-08-02', 'MALE', 'Lot UV 8 Antisirabe', 'Apiculteur', '0370634567', 'member.16@fed-agri.mg', '2025-01-01', 'CONFIRMED', 'col-3');

INSERT INTO member_referee (member_id, referee_id) VALUES
                                                       ('C1-M3', 'C1-M1'),
                                                       ('C1-M3', 'C1-M2'),
                                                       ('C1-M4', 'C1-M1'),
                                                       ('C1-M4', 'C1-M2'),
                                                       ('C1-M5', 'C1-M1'),
                                                       ('C1-M5', 'C1-M2'),
                                                       ('C1-M6', 'C1-M1'),
                                                       ('C1-M6', 'C1-M2'),
                                                       ('C1-M7', 'C1-M1'),
                                                       ('C1-M7', 'C1-M2'),
                                                       ('C1-M8', 'C1-M6'),
                                                       ('C1-M8', 'C1-M7');

INSERT INTO member_referee (member_id, referee_id) VALUES
                                                       ('C2-M3', 'C1-M1'),
                                                       ('C2-M3', 'C1-M2'),
                                                       ('C2-M4', 'C1-M1'),
                                                       ('C2-M4', 'C1-M2'),
                                                       ('C2-M5', 'C1-M1'),
                                                       ('C2-M5', 'C1-M2'),
                                                       ('C2-M6', 'C1-M1'),
                                                       ('C2-M6', 'C1-M2'),
                                                       ('C2-M7', 'C1-M1'),
                                                       ('C2-M7', 'C1-M2'),
                                                       ('C2-M8', 'C1-M6'),
                                                       ('C2-M8', 'C1-M7');

INSERT INTO member_referee (member_id, referee_id) VALUES
                                                       ('C3-M1', 'C1-M1'),
                                                       ('C3-M1', 'C1-M2'),
                                                       ('C3-M2', 'C1-M1'),
                                                       ('C3-M2', 'C1-M2'),
                                                       ('C3-M3', 'C3-M1'),
                                                       ('C3-M3', 'C3-M2'),
                                                       ('C3-M4', 'C3-M1'),
                                                       ('C3-M4', 'C3-M2'),
                                                       ('C3-M5', 'C3-M1'),
                                                       ('C3-M5', 'C3-M2'),
                                                       ('C3-M6', 'C3-M1'),
                                                       ('C3-M6', 'C3-M2'),
                                                       ('C3-M7', 'C3-M1'),
                                                       ('C3-M7', 'C3-M2'),
                                                       ('C3-M8', 'C3-M1'),
                                                       ('C3-M8', 'C3-M2');

INSERT INTO membership_fees (id, collectivity_id, label, status, frequency, eligible_from, amount) VALUES
                                                                                                       ('cot-1', 'col-1', 'Cotisation annuelle', 'ACTIVE', 'ANNUALLY', '2026-01-01', 100000),
                                                                                                       ('cot-2', 'col-2', 'Cotisation annuelle', 'ACTIVE', 'ANNUALLY', '2026-01-01', 100000),
                                                                                                       ('cot-3', 'col-3', 'Cotisation annuelle', 'ACTIVE', 'ANNUALLY', '2026-01-01', 50000);

INSERT INTO financial_account (id, collectivity_id, account_type, initial_amount, holder_name, phone_number) VALUES
                                                                                                                 ('C1-A-CASH', 'col-1', 'CASH', 0, NULL, NULL),
                                                                                                                 ('C1-A-MOBILE-1', 'col-1', 'ORANGE_MONEY', 0, 'Mpanorina', '0370489612');

INSERT INTO financial_account (id, collectivity_id, account_type, initial_amount, holder_name, phone_number) VALUES
                                                                                                                 ('C2-A-CASH', 'col-2', 'CASH', 0, NULL, NULL),
                                                                                                                 ('C2-A-MOBILE-1', 'col-2', 'ORANGE_MONEY', 0, 'Dobo voalohany', '0320489612');

INSERT INTO financial_account (id, collectivity_id, account_type, initial_amount, holder_name, phone_number) VALUES
    ('C3-A-CASH', 'col-3', 'CASH', 0, NULL, NULL);

INSERT INTO payment (collectivity_id, member_id, amount, account_id, payment_method, payment_date) VALUES
                                                                                                       ('col-1', 'C1-M1', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
                                                                                                       ('col-1', 'C1-M2', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
                                                                                                       ('col-1', 'C1-M3', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
                                                                                                       ('col-1', 'C1-M4', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
                                                                                                       ('col-1', 'C1-M5', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
                                                                                                       ('col-1', 'C1-M6', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
                                                                                                       ('col-1', 'C1-M7', 60000, 'C1-A-CASH', 'CASH', '2026-01-01'),
                                                                                                       ('col-1', 'C1-M8', 90000, 'C1-A-CASH', 'CASH', '2026-01-01');

INSERT INTO payment (collectivity_id, member_id, amount, account_id, payment_method, payment_date) VALUES
                                                                                                       ('col-2', 'C2-M1', 60000, 'C2-A-CASH', 'CASH', '2026-01-01'),
                                                                                                       ('col-2', 'C2-M2', 90000, 'C2-A-CASH', 'CASH', '2026-01-01'),
                                                                                                       ('col-2', 'C2-M3', 100000, 'C2-A-CASH', 'CASH', '2026-01-01'),
                                                                                                       ('col-2', 'C2-M4', 100000, 'C2-A-CASH', 'CASH', '2026-01-01'),
                                                                                                       ('col-2', 'C2-M5', 100000, 'C2-A-CASH', 'CASH', '2026-01-01'),
                                                                                                       ('col-2', 'C2-M6', 100000, 'C2-A-CASH', 'CASH', '2026-01-01'),
                                                                                                       ('col-2', 'C2-M7', 40000, 'C2-A-MOBILE-1', 'MOBILE_MONEY', '2026-01-01'),
                                                                                                       ('col-2', 'C2-M8', 60000, 'C2-A-MOBILE-1', 'MOBILE_MONEY', '2026-01-01');

INSERT INTO transaction (collectivity_id, member_id, amount, account_id, payment_method, transaction_date) VALUES
                                                                                                               ('col-1', 'C1-M1', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
                                                                                                               ('col-1', 'C1-M2', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
                                                                                                               ('col-1', 'C1-M3', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
                                                                                                               ('col-1', 'C1-M4', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
                                                                                                               ('col-1', 'C1-M5', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
                                                                                                               ('col-1', 'C1-M6', 100000, 'C1-A-CASH', 'CASH', '2026-01-01'),
                                                                                                               ('col-1', 'C1-M7', 60000, 'C1-A-CASH', 'CASH', '2026-01-01'),
                                                                                                               ('col-1', 'C1-M8', 90000, 'C1-A-CASH', 'CASH', '2026-01-01');

INSERT INTO transaction (collectivity_id, member_id, amount, account_id, payment_method, transaction_date) VALUES
                                                                                                               ('col-2', 'C2-M1', 60000, 'C2-A-CASH', 'CASH', '2026-01-01'),
                                                                                                               ('col-2', 'C2-M2', 90000, 'C2-A-CASH', 'CASH', '2026-01-01'),
                                                                                                               ('col-2', 'C2-M3', 100000, 'C2-A-CASH', 'CASH', '2026-01-01'),
                                                                                                               ('col-2', 'C2-M4', 100000, 'C2-A-CASH', 'CASH', '2026-01-01'),
                                                                                                               ('col-2', 'C2-M5', 100000, 'C2-A-CASH', 'CASH', '2026-01-01'),
                                                                                                               ('col-2', 'C2-M6', 100000, 'C2-A-CASH', 'CASH', '2026-01-01'),
                                                                                                               ('col-2', 'C2-M7', 40000, 'C2-A-MOBILE-1', 'MOBILE_MONEY', '2026-01-01'),
                                                                                                               ('col-2', 'C2-M8', 60000, 'C2-A-MOBILE-1', 'MOBILE_MONEY', '2026-01-01');

SET session_replication_role = 'origin';
