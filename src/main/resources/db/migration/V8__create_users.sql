CREATE TABLE tb_users (
	
	id BIGSERIAL PRIMARY KEY,
	
	username VARCHAR(80) NOT NULL UNIQUE,
	
	password VARCHAR(255) NOT NULL,
	
	role VARCHAR(20) NOT NULL,
	
	created_at TIMESTAMP,
	
	updated_at TIMESTAMP
	
);