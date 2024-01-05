CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(50),
    password VARCHAR(255),
    created_on TIMESTAMP DEFAULT current_timestamp,
    updated_on TIMESTAMP DEFAULT current_timestamp
);

CREATE TABLE IF NOT EXISTS notes (
    id UUID PRIMARY KEY,
    title VARCHAR(255),
    content TEXT,
    colour VARCHAR(255),
    priority INTEGER,
    owner_id UUID REFERENCES users(id),
    created_on TIMESTAMP DEFAULT current_timestamp,
    updated_on TIMESTAMP DEFAULT current_timestamp
);

CREATE TABLE IF NOT EXISTS shared_note (
    note_id UUID,
    user_id UUID,
    access_bitmap INTEGER,
    created_on TIMESTAMP DEFAULT current_timestamp,
    updated_on TIMESTAMP DEFAULT current_timestamp,
    PRIMARY KEY (note_id, user_id)
);