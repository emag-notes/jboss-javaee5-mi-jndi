DROP TABLE IF EXISTS test;
CREATE TABLE test (
	id serial primary key,
	value text
);
INSERT INTO test (value) VALUES ('node1');