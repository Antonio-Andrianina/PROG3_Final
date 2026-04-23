
CREATE TYPE gender_type AS ENUM ('MALE', 'FEMALE');
CREATE TYPE member_occupation_type AS ENUM ('PRESIDENT', 'VICE_PRESIDENT', 'TREASURER', 'SECRETARY', 'SENIOR', 'JUNIOR', 'CONFIRMED');
CREATE TYPE frequency_type AS ENUM ('WEEKLY', 'MONTHLY', 'ANNUALLY', 'PUNCTUALLY');
CREATE TYPE activity_status_type AS ENUM ('ACTIVE', 'INACTIVE');
CREATE TYPE account_type_type AS ENUM ('CASH', 'ORANGE_MONEY', 'MVOLA', 'BANK_TRANSFER');
CREATE TYPE payment_method_type AS ENUM ('CASH', 'MOBILE_MONEY', 'BANK_TRANSFER');
CREATE TYPE transaction_type_type AS ENUM ('FEE_PAYMENT', 'CONTRIBUTION', 'OTHER');

CREATE TABLE collectivity (
                              id VARCHAR(50) PRIMARY KEY,
                              unique_number VARCHAR(50),
                              unique_name VARCHAR(255),
                              location VARCHAR(255) NOT NULL,
                              specialty VARCHAR(255),
                              federation_approval BOOLEAN DEFAULT TRUE,
                              creation_date DATE DEFAULT CURRENT_DATE
);

CREATE TABLE member (
                        id VARCHAR(50) PRIMARY KEY,
                        first_name VARCHAR(100) NOT NULL,
                        last_name VARCHAR(100) NOT NULL,
                        birth_date DATE NOT NULL,
                        gender gender_type NOT NULL,
                        address TEXT,
                        profession VARCHAR(100),
                        phone_number VARCHAR(20),
                        email VARCHAR(255) UNIQUE NOT NULL,
                        registration_date DATE DEFAULT CURRENT_DATE,
                        occupation member_occupation_type,
                        collectivity_id VARCHAR(50),
                        FOREIGN KEY (collectivity_id) REFERENCES collectivity(id) ON DELETE SET NULL
);

CREATE TABLE member_referee (
                                member_id VARCHAR(50) NOT NULL,
                                referee_id VARCHAR(50) NOT NULL,
                                PRIMARY KEY (member_id, referee_id),
                                FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE,
                                FOREIGN KEY (referee_id) REFERENCES member(id) ON DELETE CASCADE
);

CREATE TABLE membership_fees (
                                 id VARCHAR(50) PRIMARY KEY,
                                 collectivity_id VARCHAR(50) NOT NULL,
                                 label VARCHAR(255),
                                 status activity_status_type DEFAULT 'ACTIVE',
                                 frequency frequency_type NOT NULL,
                                 eligible_from DATE NOT NULL,
                                 amount DECIMAL(15, 2) NOT NULL,
                                 FOREIGN KEY (collectivity_id) REFERENCES collectivity(id) ON DELETE CASCADE
);

CREATE TABLE financial_account (
                                   id VARCHAR(50) PRIMARY KEY,
                                   collectivity_id VARCHAR(50) NOT NULL,
                                   account_type account_type_type NOT NULL,
                                   initial_amount DECIMAL(15, 2) DEFAULT 0,
                                   holder_name VARCHAR(255),
                                   phone_number VARCHAR(20),
                                   FOREIGN KEY (collectivity_id) REFERENCES collectivity(id) ON DELETE CASCADE
);

CREATE TABLE payment (
                         id SERIAL PRIMARY KEY,
                         collectivity_id VARCHAR(50) NOT NULL,
                         member_id VARCHAR(50) NOT NULL,
                         amount DECIMAL(15, 2) NOT NULL,
                         account_id VARCHAR(50) NOT NULL,
                         payment_method payment_method_type NOT NULL,
                         payment_date DATE NOT NULL,
                         FOREIGN KEY (collectivity_id) REFERENCES collectivity(id) ON DELETE CASCADE,
                         FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE,
                         FOREIGN KEY (account_id) REFERENCES financial_account(id) ON DELETE CASCADE
);

CREATE TABLE transaction (
                             id SERIAL PRIMARY KEY,
                             collectivity_id VARCHAR(50) NOT NULL,
                             member_id VARCHAR(50) NOT NULL,
                             amount DECIMAL(15, 2) NOT NULL,
                             account_id VARCHAR(50) NOT NULL,
                             payment_method payment_method_type NOT NULL,
                             transaction_date DATE NOT NULL,
                             FOREIGN KEY (collectivity_id) REFERENCES collectivity(id) ON DELETE CASCADE,
                             FOREIGN KEY (member_id) REFERENCES member(id) ON DELETE CASCADE,
                             FOREIGN KEY (account_id) REFERENCES financial_account(id) ON DELETE CASCADE
);