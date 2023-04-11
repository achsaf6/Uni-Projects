package oop.ex6.check;

import oop.ex6.main.IllegalCodeException;

import java.util.regex.Pattern;

public class CheckStatement {
    private static final Pattern RETURN_PATTERN = Pattern.compile("\\s*+return\\s*+;\\s*+");
    private static final Pattern METHOD_CALL_PATTERN =
            Pattern.compile("\\s*+\\w+\\s*+\\([^)]*+\\)\\s*+;\\s*+");

    /**
     * Checks a line of code that is a statement
     * @param line The line of code
     * @throws IllegalCodeException
     */
    public static void check(String line) throws IllegalCodeException {
        if (RETURN_PATTERN.matcher(line).matches())
            return;
        else if (METHOD_CALL_PATTERN.matcher(line).matches())
            CheckMethodCall.check(line);
        else
            CheckVariableStatement.check(line);
    }

    /**
     * Checks if a line of code is a return statement
     * @param statement The line of code
     * @return true if the line of code is a legal return statement
     */
    public static boolean isReturnStatement(String statement) {
        return RETURN_PATTERN.matcher(statement).matches();
    }
}
