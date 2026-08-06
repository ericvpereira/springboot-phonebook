ALTER TABLE tb_contacts

ADD CONSTRAINT uk_contact_email

UNIQUE(email);