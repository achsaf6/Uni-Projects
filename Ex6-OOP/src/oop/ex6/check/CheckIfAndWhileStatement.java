package oop.ex6.check;

import oop.ex6.main.IllegalCodeException;
import oop.ex6.syntax.SymbolTable;
import oop.ex6.syntax.Variable;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckIfAndWhileStatement {
    private static final Pattern IF_OR_WHILE_PATTERN =
            Pattern.compile("\\s*+(if|while)\\s*+\\(([^\\)]*+)\\)\\s*+\\{\\s*+");
    private static final String LEGAL_CONDITION_TYPE = "boolean";
    private static final String OR = "||";
        private static final String AND = "&&";

    /**
     * Checks the opening statement of an if and while block
     * @param statement The Line of code with the if or while statement
     * @throws IllegalCodeException thrown in case of illegal code
     */
    public static void check (String statement) throws IllegalCodeException {
        Matcher ifWhileMatcher = IF_OR_WHILE_PATTERN.matcher(statement);

        if (!ifWhileMatcher.matches())
            throw new UnknownFormatException("Wrong if or while format");

        CheckIfAndWhileStatement.checkCondition(ifWhileMatcher.group(2));
    }

    // Checks if the condition is legal
    private static void checkCondition(String condition) throws IllegalCodeException {
        ArrayList<String> tokens = Tokenizer.tokenize(condition);

        if (!tokens.isEmpty() && tokens.size() % 2 == 0)
            throw new InvalidConditionException("Invalid number of boolean values or operators");

        for (int i = 0; i < tokens.size(); i++) {
            if (i % 2 == 0)
                checkIfLegalBooleanValue(tokens.get(i));
            else
                checkBooleanOperators(tokens.get(i));
        }
    }

    // Checks if the boolean operators are legal
    private static void checkBooleanOperators(String booleanOperator) throws IllegalCodeException{
        if (!(booleanOperator.equals(AND) || booleanOperator.equals(OR)))
            throw new InvalidConditionException("Expected || or &&");
    }

    // Checks if a value in the condition is a legal boolean one
    private static void checkIfLegalBooleanValue(String givenValue) throws IllegalCodeException {
        boolean isBoolean = Pattern.compile(Variable.typeValMap.get("boolean")).matcher(givenValue).matches();
        Variable variable = SymbolTable.containsVar(givenValue);
        boolean isBooleanVar = variable != null && variable.getValue() != null &&
                variable.getValue().matches(Variable.typeValMap.get(LEGAL_CONDITION_TYPE));
        if (!isBoolean && !isBooleanVar)
            throw new InvalidConditionException("Condition values must be boolean");
    }
}
