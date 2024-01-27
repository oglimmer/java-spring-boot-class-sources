package com.oglimmer.kniffel.service;

// to make it an exception in java you need to extend it from Exception (or RuntimeException)
// using RuntimeException means it's an "unchecked" exception, so we don't need to declare
// its usage on all methods with the "throws" keyword
public class IllegalGameStateException extends RuntimeException {
    IllegalGameStateException(String reason) {
        super(reason);
    }
}