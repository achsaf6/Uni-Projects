package oop.ex6.check;

import oop.ex6.main.IllegalCodeException;

public class UnexpectedEatException extends IllegalCodeException {
    public UnexpectedEatException(String s) {
        super(s);
    }
}
