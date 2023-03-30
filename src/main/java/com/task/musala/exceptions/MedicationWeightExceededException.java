package com.task.musala.exceptions;

public class MedicationWeightExceededException extends RuntimeException {
    public MedicationWeightExceededException(String message) {
        super(message);
    }
}
