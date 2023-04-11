package oop.ex6.syntax;

import oop.ex6.main.IllegalCodeException;

public class InvalidAssignmentException extends IllegalCodeException {

    public InvalidAssignmentException(String message){
        super(message);
    }

}
