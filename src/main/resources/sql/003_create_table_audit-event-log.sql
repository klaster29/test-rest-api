CREATE TABLE audit_event_log (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  timestamp TIMESTAMP NOT NULL,
  message VARCHAR(255) NOT NULL,
  drone_serial_number VARCHAR(100) NOT NULL
);