ALTER TABLE posts ADD COLUMN category_id INT,
ADD FOREIGN KEY fk_post_category(category_id)
REFERENCES categories(id) ON DELETE CASCADE;