package oop.ex6.check;

import oop.ex6.main.IllegalCodeException;

public class InvalidConditionException extends IllegalCodeException {
    public InvalidConditionException(String message) {
        super(message);
    }
}
