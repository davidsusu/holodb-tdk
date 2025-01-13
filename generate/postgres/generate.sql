CREATE EXTENSION IF NOT EXISTS anon;

CREATE TABLE companies (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    city VARCHAR(255),
    contact_phone VARCHAR(36)
);

INSERT INTO companies
SELECT
  generate_series::BIGINT,
  concat(anon.dummy_company_name(), anon.dummy_company_suffix()),
  anon.dummy_city_name(),
  anon.dummy_phone_number()
FROM generate_series(1, 200);

CREATE INDEX idx_companies_name ON companies (name);
CREATE INDEX idx_companies_city ON companies (city);


CREATE TABLE employees (
    id BIGSERIAL PRIMARY KEY,
    company_id INT NOT NULL REFERENCES companies(id) ON DELETE CASCADE,
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    birth_year INT,
    birth_country VARCHAR(255),
    preferred_color VARCHAR(255)
);

INSERT INTO employees
SELECT
  generate_series::BIGINT,
  anon.random_bigint_between(1, 200),
  anon.dummy_first_name(),
  anon.dummy_last_name(),
  anon.random_int_between(1950, 2000),
  anon.dummy_country_name(),
  lower(faker.color_name())
FROM generate_series(1, 2000);

CREATE INDEX idx_employees_name ON employees (company_id);
CREATE INDEX idx_employees_firstname ON employees (firstname);
CREATE INDEX idx_employees_lastname ON employees (lastname);
CREATE INDEX idx_employees_birth_year ON employees (birth_year);
CREATE INDEX idx_employees_birth_country ON employees (birth_country);
CREATE INDEX idx_employees_preferred_color ON employees (preferred_color);


CREATE TABLE coupons (
    id BIGSERIAL PRIMARY KEY,
    employee_id INT NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
    identifier VARCHAR(20) NOT NULL
);

INSERT INTO coupons
SELECT
  generate_series::BIGINT,
  anon.random_bigint_between(1, 2000),
  concat(chr((random()*6)::INT+65), chr((random()*6)::INT+65), chr((random()*6)::INT+65), '-', anon.random_bigint_between(100000000000, 999999999999))
FROM generate_series(1, 5000);

CREATE INDEX idx_coupons_employee_id ON coupons (employee_id);
CREATE INDEX idx_coupons_identifier ON coupons (identifier);
