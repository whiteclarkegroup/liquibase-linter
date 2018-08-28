package com.wcg.liquibase.config.rules;

public interface WithFormattedErrorMessage<T> {
    String formatErrorMessage(String errorMessage, T object);
}
