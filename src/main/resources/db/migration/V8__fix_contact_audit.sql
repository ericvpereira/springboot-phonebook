-- Define valores padrão para novos contatos
ALTER TABLE tb_contacts
ALTER COLUMN created_at SET DEFAULT NOW();

ALTER TABLE tb_contacts
ALTER COLUMN updated_at SET DEFAULT NOW();

-- Corrige registros antigos
UPDATE tb_contacts
SET created_at = COALESCE(created_at, NOW()),
	updated_at = COALESCE(updated_at, NOW())
WHERE created_at IS NULL
	OR updated_at IS NULL;