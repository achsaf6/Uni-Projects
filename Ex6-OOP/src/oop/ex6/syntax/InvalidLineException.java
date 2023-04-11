package oop.ex6.syntax;

import oop.ex6.main.IllegalCodeException;

public class InvalidLineException extends IllegalCodeException {
    public InvalidLineException(String message) {
        super(message);
    }
}
