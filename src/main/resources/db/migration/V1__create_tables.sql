CREATE TABLE tb_contacts (
    id BIGSERIAL PRIMARY KEY,

    contact_name VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255),
    email VARCHAR(255),
    type VARCHAR(255),

    street VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    zip_code VARCHAR(255)
);