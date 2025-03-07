CREATE TABLE IF NOT EXISTS tb_food_item (
    id BIGSERIAL PRIMARY KEY ,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    expiry_date TIMESTAMP NOT NULL
);