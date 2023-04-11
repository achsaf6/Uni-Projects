package oop.ex6.check;

import oop.ex6.main.IllegalCodeException;

public class UnknownReturnTypeException extends IllegalCodeException {
    public UnknownReturnTypeException(String s) {
        super(s);
    }
}
