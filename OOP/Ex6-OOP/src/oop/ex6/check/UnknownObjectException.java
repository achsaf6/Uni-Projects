package oop.ex6.check;

import oop.ex6.main.IllegalCodeException;

public class UnknownObjectException extends IllegalCodeException {
    public UnknownObjectException(String s) {
        super(s);
    }
}
