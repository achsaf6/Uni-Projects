package oop.ex6.check;

import oop.ex6.main.IllegalCodeException;

public class UnknownFormatException extends IllegalCodeException {
    public UnknownFormatException(String s) {
        super(s);
    }
}
