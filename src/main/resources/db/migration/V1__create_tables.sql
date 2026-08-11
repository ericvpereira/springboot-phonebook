CREATE TABLE tb_contacts (
    id BIGSERIAL PRIMARY KEY,
    contact_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    email VARCHAR(100),
    type VARCHAR(30),
    street VARCHAR(150),
    city VARCHAR(100),
    state VARCHAR(2),
    zip_code VARCHAR(10),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);