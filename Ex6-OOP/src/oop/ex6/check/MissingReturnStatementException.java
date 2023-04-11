package oop.ex6.check;

import oop.ex6.main.IllegalCodeException;

public class MissingReturnStatementException extends IllegalCodeException {
    public MissingReturnStatementException(String message) {
        super(message);
    }
}
