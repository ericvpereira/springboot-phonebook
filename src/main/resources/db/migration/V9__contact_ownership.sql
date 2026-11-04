-- Legacy contacts remain unassigned; do not expose them to newly registered users.
ALTER TABLE tb_contacts ADD COLUMN owner_username VARCHAR(80);
CREATE INDEX idx_contacts_owner_username ON tb_contacts(owner_username);
