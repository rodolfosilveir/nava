CREATE TABLE script (
    id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_user_owner UUID,
    id_user_creator UUID NOT NULL,
    name TEXT NOT NULL UNIQUE,
    content TEXT NOT NULL,
    status TEXT NOT NULL,
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_user_owner FOREIGN KEY (id_user_owner) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_creator FOREIGN KEY (id_user_creator) REFERENCES users(id) ON DELETE CASCADE
);