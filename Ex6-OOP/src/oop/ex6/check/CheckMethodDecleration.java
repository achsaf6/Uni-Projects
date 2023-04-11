package oop.ex6.check;

import oop.ex6.main.IllegalCodeException;
import oop.ex6.syntax.InvalidAssignmentException;
import oop.ex6.syntax.Method;
import oop.ex6.syntax.SymbolTable;
import oop.ex6.syntax.Variable;

import java.util.ArrayList;

public class CheckMethodDecleration {

    public static final String regExLegalName = "[a-zA-Z]+[a-zA-Z_\\d]*";
    public static final String regExReturnType = "void";
    private static String methodStatement;
    private static ArrayList<String> tokens;
    private static ArrayList<Variable> parameterList;

    /**
     * checks that the method is syntactically corrects and adds it to the symbol table
     * @param methodDeclaration -- method declecation statement
     * @throws IllegalCodeException -- exception to be thrown in case of an error
     */
    public static void check(String methodDeclaration) throws IllegalCodeException {
        methodStatement = methodDeclaration;
        if (!methodDeclaration.matches(".+\\{$")){
            throw new InvalidEndingException("Method decleration doesn't end in '{'\n" +
                    "Decleration: " + methodStatement);
        }
        tokens = Tokenizer.tokenize(methodDeclaration);
        parameterList = new ArrayList<>();
        if (tokens.get(0).matches(regExReturnType)) {
            verifyMethodDeclaration();
        } else {
            throw new UnknownReturnTypeException(tokens.get(0) + " not recognized as a valid return type.");
        }
    }

    /**
     * ensures the syntax of all the function is correct
     * @throws IllegalCodeException
     */
    private static void verifyMethodDeclaration() throws IllegalCodeException {
        String returnType = tokens.get(0);
        Tokenizer.eat("void", tokens);

        if (!tokens.get(0).matches(regExLegalName)){
            throw new InvalidAssignmentException("Invalid method name\n" + tokens.get(0));
        }
        String methodName = tokens.get(0);
        tokens.remove(0);
        Tokenizer.eat("(", tokens);
        if (!tokens.get(0).equals(")")){
            verifyParameterList();
        }
        Tokenizer.eat(")", tokens);
        if (tokens.size() > 1 || !tokens.get(0).equals("{") ){
            throw new UnknownFormatException("Something went wrong along the way...\n" + methodStatement);
        }
        Method meth = new Method(returnType, methodName, parameterList);
        SymbolTable.add(meth);
    }

    /**
     * ensures that parameter list has been properly declared
     * @throws IllegalCodeException
     */
    private static void verifyParameterList() throws IllegalCodeException {
        verifyParameter();
        while (tokens.get(0).equals(",")){
            Tokenizer.eat(",",tokens);
            verifyParameter();
        }
    }


    /**
     * ensures each parameter has been declared properly
     * @throws IllegalCodeException
     */
    private static void verifyParameter() throws IllegalCodeException {

        String argModifier = null;
        if (tokens.get(0).matches(Variable.modifiersRegex)){
            argModifier = tokens.get(0);
            tokens.remove(0);
        }
        String argType = tokens.get(0);
        tokens.remove(0);
        String argName = tokens.get(0);
        tokens.remove(0);
        Variable var = new Variable(argModifier, argType, argName);
        parameterList.add(var);
    }

}
