CREATE TABLE medication_entity (
id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(100) NOT NULL,
weight DOUBLE NOT NULL,
code VARCHAR(100) NOT NULL,
image BLOB NOT NULL,
drone_entity_serial_number VARCHAR(100) REFERENCES drone_entity(serial_number)
);