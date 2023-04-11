package oop.ex6.syntax;

import oop.ex6.main.IllegalCodeException;

public class ScopeCollisionException extends IllegalCodeException {
    public ScopeCollisionException(String s) {
        super(s);
    }
}
