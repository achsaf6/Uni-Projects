package oop.ex6.check;

import jdk.dynalink.beans.StaticClass;
import oop.ex6.main.IllegalCodeException;
import oop.ex6.syntax.InvalidAssignmentException;
import oop.ex6.syntax.Method;
import oop.ex6.syntax.SymbolTable;

import java.util.ArrayList;

public class CheckMethodCall {


    private static String functionCall; // saves the current statment being proccessed used for debugging

    /**
     * checks if the given function call exists and is sending the correct parameters
     * @param funCall -- function call statement
     * @throws IllegalCodeException -- exception to be thrown in the event of illegalness
     */
    public static void check(String funCall) throws IllegalCodeException {
        functionCall = funCall;
        ArrayList<String> tokens = Tokenizer.tokenize(funCall);
        Method meth;
        if ( (meth = SymbolTable.containsMethod(tokens.get(0))) == null) {
            throw new UnknownObjectException("This function doesn't exist\nFunction: " + funCall);
        }
        tokens.remove(0);
        Tokenizer.eat("(", tokens);
        int index = 0;
        while (!tokens.get(0).equals(")")){
            if (!meth.getParamAt(index).isValidParam(tokens.get(0)) ){
                throw new InvalidAssignmentException("param doesn't match function decleration\nExpected: "
                        + meth.getParamAt(index).getType() + "\nGot: " + tokens.get(0));
            }
            tokens.remove(0);
            if (!tokens.get(0).equals(")")) {
                Tokenizer.eat(",", tokens);
            }
            index += 1;
        }
        Tokenizer.eat(")", tokens);
        if (tokens.size() > 1 || !tokens.get(0).equals(";") ){
            throw new UnknownFormatException("Something went wrong along the way...\n" + functionCall);
        }
    }

}
