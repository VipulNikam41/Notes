CREATE INDEX IF NOT EXISTS idx_user_email ON users(email);

CREATE INDEX IF NOT EXISTS idx_note_owner ON notes(owner_id);
CREATE INDEX IF NOT EXISTS idx_note_title_content ON notes(title, content);

CREATE INDEX IF NOT EXISTS idx_shared_note ON shared_note(note_id);
CREATE INDEX IF NOT EXISTS idx_shared_user ON shared_note(user_id);
CREATE INDEX IF NOT EXISTS idx_shared_note_user ON shared_note(note_id, user_id);
