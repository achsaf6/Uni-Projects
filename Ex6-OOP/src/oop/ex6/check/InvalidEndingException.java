package oop.ex6.check;

import oop.ex6.main.IllegalCodeException;

public class InvalidEndingException extends IllegalCodeException {
    public InvalidEndingException(String s) {
        super(s);
    }
}
