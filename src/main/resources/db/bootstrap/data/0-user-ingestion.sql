INSERT INTO users (email, password, roles)
VALUES
    ('admin@flickcrit.com', '$2a$12$O6h9QPDjF/xR4DLWAQTtBeWod.NkLYKgfstF0mNcZfbPrgRHoF3Tq', '{ADMIN}'),
    ('user1@flickcrit.com', '$2a$12$VRdjyLhinaPxw08VgyuF7OemAH3pTlqbyYYujYVquXmal8l9usJsW', '{USER}'),
    ('user2@flickcrit.com', '$2a$12$3mqi13wFdbepjEjj4Y44wu813x1NbgAlx9AIgTL0sBoZ2rYgg8w9m', '{USER}'),
    ('user3@flickcrit.com', '$2a$12$3mqi13wFdbepjEjj4Y44wu813x1NbgAlx9AIgTL0sBoZ2rYgg8w9m', '{USER}'),
    ('user4@flickcrit.com', '$2a$12$3mqi13wFdbepjEjj4Y44wu813x1NbgAlx9AIgTL0sBoZ2rYgg8w9m', '{USER}'),
    ('user5@flickcrit.com', '$2a$12$3mqi13wFdbepjEjj4Y44wu813x1NbgAlx9AIgTL0sBoZ2rYgg8w9m', '{USER}'),
    ('user6@flickcrit.com', '$2a$12$3mqi13wFdbepjEjj4Y44wu813x1NbgAlx9AIgTL0sBoZ2rYgg8w9m', '{USER}'),
    ('user7@flickcrit.com', '$2a$12$3mqi13wFdbepjEjj4Y44wu813x1NbgAlx9AIgTL0sBoZ2rYgg8w9m', '{USER}'),
    ('user8@flickcrit.com', '$2a$12$3mqi13wFdbepjEjj4Y44wu813x1NbgAlx9AIgTL0sBoZ2rYgg8w9m', '{USER}'),
    ('user9@flickcrit.com', '$2a$12$3mqi13wFdbepjEjj4Y44wu813x1NbgAlx9AIgTL0sBoZ2rYgg8w9m', '{USER}'),
    ('user10@flickcrit.com', '$2a$12$3mqi13wFdbepjEjj4Y44wu813x1NbgAlx9AIgTL0sBoZ2rYgg8w9m', '{USER}'),
    ('user11@flickcrit.com', '$2a$12$3mqi13wFdbepjEjj4Y44wu813x1NbgAlx9AIgTL0sBoZ2rYgg8w9m', '{USER}'),
    ('user12@flickcrit.com', '$2a$12$3mqi13wFdbepjEjj4Y44wu813x1NbgAlx9AIgTL0sBoZ2rYgg8w9m', '{USER}'),
    ('user13@flickcrit.com', '$2a$12$3mqi13wFdbepjEjj4Y44wu813x1NbgAlx9AIgTL0sBoZ2rYgg8w9m', '{USER}'),
    ('user14@flickcrit.com', '$2a$12$3mqi13wFdbepjEjj4Y44wu813x1NbgAlx9AIgTL0sBoZ2rYgg8w9m', '{USER}'),
    ('user15@flickcrit.com', '$2a$12$3mqi13wFdbepjEjj4Y44wu813x1NbgAlx9AIgTL0sBoZ2rYgg8w9m', '{USER}')
ON CONFLICT DO NOTHING;