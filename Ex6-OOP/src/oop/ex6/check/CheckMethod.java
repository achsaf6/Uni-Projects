package oop.ex6.check;

import oop.ex6.main.IllegalCodeException;
import oop.ex6.syntax.CodeLine;
import oop.ex6.syntax.SymbolTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Checks a method in sjava
 */
public class CheckMethod {

    /**
     * Checks a method body in sjava
     * @param scopeOpener The opening line that also declares the function
     * @param reader The reader that can read lines of code
     * @throws IOException
     * @throws IllegalCodeException
     */
    public static void check (String scopeOpener, BufferedReader reader) throws IOException, IllegalCodeException {
        String line = reader.readLine();

        SymbolTable.createGlobalScopeCopy();

        new SymbolTable();
        String funcName = Tokenizer.tokenize(scopeOpener).get(1);
        SymbolTable.addMethodArguments(funcName);

        boolean canExitMethod = false;
        while (line != null) {
            CodeLine codeLine = new CodeLine(line);

            if (codeLine.getLineType().equals(CodeLine.SCOPE_CLOSER))
                break;
            else if (codeLine.getLineType().equals(CodeLine.SCOPE_OPENER))
                CheckScope.check(line, reader);
            else if (codeLine.getLineType().equals(CodeLine.STATEMENT_LINE))
                CheckStatement.check(line);

            canExitMethod = CheckStatement.isReturnStatement(line) ||
                    (canExitMethod && (codeLine.getLineType().equals(CodeLine.EMPTY_LINE) ||
                            codeLine.getLineType().equals(CodeLine.COMMENT_LINE)));

            line = reader.readLine();
        }
        if (line == null)
            throw new ExpectedScopeCloserException("Expected }");

        if (!canExitMethod)
            throw new MissingReturnStatementException("Missing return statement at end of method");

        SymbolTable.destroy();
        SymbolTable.destroy();
    }

}
