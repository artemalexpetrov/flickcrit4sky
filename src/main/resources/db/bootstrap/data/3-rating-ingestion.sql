INSERT INTO rating (user_id, movie_id, rating)
SELECT users.id AS user_id, movie.id AS movie_id, FLOOR(1 + RANDOM() * 5)::INT AS rating
FROM users LEFT OUTER JOIN movie ON TRUE
ON CONFLICT(user_id, movie_id) DO UPDATE SET rating = EXCLUDED.rating;
