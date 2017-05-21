CREATE TABLE tweet_min
(
  id BIGINT PRIMARY KEY,
  hash character varying(140),
  user_name character varying(100),
  profile_image_url character varying(300),
  text character varying(200)
);

CREATE INDEX idx_hash ON tweet_min (hash);