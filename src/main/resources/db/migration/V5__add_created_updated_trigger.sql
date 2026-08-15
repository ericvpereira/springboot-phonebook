CREATE OR REPLACE FUNCTION update_updated_at()

RETURNS TRIGGER AS
$$
BEGIN

NEW.updated_at = NOW();

RETURN NEW;

END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_timestamp

BEFORE UPDATE 

ON tb_contacts

FOR EACH ROW

EXECUTE FUNCTION update_updated_at();