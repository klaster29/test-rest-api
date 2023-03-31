CREATE TABLE drone_medication (
  drone_serial_number VARCHAR(100) NOT NULL,
  medication_id BIGINT NOT NULL,
  drone_entity_serial_number VARCHAR(100),
  PRIMARY KEY (drone_serial_number, medication_id),
  FOREIGN KEY (drone_serial_number) REFERENCES drone_entity(serial_number) ON DELETE CASCADE,
  FOREIGN KEY (medication_id) REFERENCES medication_entity(id) ON DELETE CASCADE,
  FOREIGN KEY (drone_entity_serial_number) REFERENCES drone_entity(serial_number) ON DELETE SET NULL
);
