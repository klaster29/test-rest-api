CREATE TABLE drone_entity (
  serial_number VARCHAR(100) PRIMARY KEY,
  model VARCHAR(20) NOT NULL,
  weight_limit DOUBLE NOT NULL,
  battery_capacity INT NOT NULL,
  state VARCHAR(20) NOT NULL
);