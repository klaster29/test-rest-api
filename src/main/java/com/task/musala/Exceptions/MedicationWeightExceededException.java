package com.task.musala.Exceptions;

public class MedicationWeightExceededException extends RuntimeException {
    public MedicationWeightExceededException(String message) {
        super(message);
    }
}
