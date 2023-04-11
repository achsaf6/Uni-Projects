package oop.ex6.check;

import oop.ex6.main.IllegalCodeException;

public class ExpectedScopeCloserException extends IllegalCodeException {
    public ExpectedScopeCloserException(String message) {
        super(message);
    }
}
