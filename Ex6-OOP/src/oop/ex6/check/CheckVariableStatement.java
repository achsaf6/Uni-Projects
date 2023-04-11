package oop.ex6.check;
import oop.ex6.main.IllegalCodeException;
import oop.ex6.syntax.InvalidAssignmentException;
import oop.ex6.syntax.SymbolTable;
import oop.ex6.syntax.Variable;

import java.util.ArrayList;
import java.util.HashMap;


public class CheckVariableStatement {
    private static ArrayList<String> tokens;
    private static String curStatment;
    private static String varType;
    private static HashMap<String,String> nameValueMap;
    private static String varModifier;

    /**
     * checks that an entire statment was sytatically correct and adds its information to the symboltable
     *
     * @param -- variable statment that ends in ';'
     *
     */

    public static void check(String statement) throws IllegalCodeException {
        curStatment = statement;
        if (!statement.matches(".*;\\s*")){
            throw new InvalidEndingException("Didnt end in an ';' \n" + curStatment);
        }
        tokens = Tokenizer.tokenize(statement);
        nameValueMap = new HashMap<>();
        varModifier = null;
        varType = null;

//        checks if statment begins with a type decleration
        if ( tokens.get(0).matches(Variable.typeRegEx) ||
                ( tokens.get(0).matches(Variable.modifiersRegex) ) ) {
            verifyDeclaration();
//        checks if statment begins with an assignment
        } else if ( SymbolTable.containsVar(tokens.get(0)) != null ){
            //        we want to CheckMethodDecleration if a word appears in the symbol table
            verifyAssignment();
        } else {
            throw new UnknownObjectException(tokens.get(0) + " wasn't initialized");
        }
        variableAssignment();
        if (tokens.size() > 1 || !tokens.get(0).equals(";") ){
            throw new UnknownFormatException("Something went wrong along the way...\n" + curStatment);
        }
    }

    /**
     * assigns the varible to the symbol table
     * @throws IllegalCodeException
     */
    private static void variableAssignment() throws IllegalCodeException {
        for (String varName: nameValueMap.keySet()){
            Variable var;
            if ((var = SymbolTable.containsVar(varName)) != null){
                var.setValue(nameValueMap.get(varName));
            } else {
                var = new Variable(varModifier, varType, varName, nameValueMap.get(varName));
                SymbolTable.add(var);
            }
        }
    }

    /**
     * ensures variable assignment has the correct syntax
     * @throws IllegalCodeException
     */
    private static void verifyAssignment() throws IllegalCodeException {
        String name = tokens.get(0);
        String value = null;
        tokens.remove(0);
        if (tokens.get(0).equals("=")){
            Tokenizer.eat("=", tokens);
            Variable var;
            if ( (var = SymbolTable.containsVar(tokens.get(0))) != null){
                if (var.getValue() == null){
                    throw new InvalidAssignmentException("Variable assignment is null\n" +
                            "Statement: " + curStatment);
                }
                value = var.getValue();
            } else {
                value = tokens.get(0);
            }
            tokens.remove(0);
        }
        nameValueMap.put(name, value);
    }

    /**
     * ensures variable decleration was done properly
     * @throws IllegalCodeException
     */
    private static void verifyDeclaration() throws IllegalCodeException {
        if (tokens.get(0).equals(Variable.modifiersRegex)){
            varModifier = tokens.get(0);
            tokens.remove(0);
        }
        varType = tokens.get(0);
        tokens.remove(0);
        verifyAssignment();
        while (tokens.get(0).equals(","))  {
            Tokenizer.eat( ",",tokens);
            verifyAssignment();
        }
    }

}


