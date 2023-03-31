ALTER TABLE medication_entity
  ADD COLUMN drone_entity_serial_number VARCHAR(100);

ALTER TABLE medication_entity
  ADD CONSTRAINT fk_medication_drone_entity_new
  FOREIGN KEY (drone_entity_serial_number)
  REFERENCES drone_entity (serial_number)
  ON DELETE SET NULL;