package com.whiteclarkegroup.liquibaselinter.config.rules;

public interface WithFormattedErrorMessage<T> {
    String formatErrorMessage(String errorMessage, T object);
}
