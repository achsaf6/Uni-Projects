package oop.ex6.check;

import oop.ex6.main.IllegalCodeException;
import oop.ex6.syntax.CodeLine;
import oop.ex6.syntax.SymbolTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Pattern;

public class CheckScope {

    /**
     * Checks a block of code inside a function
     * @param scopeOpener The line that opens the block must be an if or while statement
     * @param reader Reader that can read lines of code
     * @throws IOException
     * @throws IllegalCodeException
     */
    public static void check (String scopeOpener, BufferedReader reader) throws IOException, IllegalCodeException {
        CheckIfAndWhileStatement.check(scopeOpener);

        String line = reader.readLine();

        new SymbolTable();

        while (line != null) {
            CodeLine codeLine = new CodeLine(line);

            if (codeLine.getLineType().equals(CodeLine.SCOPE_CLOSER))
                break;
            else if (codeLine.getLineType().equals(CodeLine.SCOPE_OPENER))
                CheckScope.check(line, reader);
            else if (codeLine.getLineType().equals(CodeLine.STATEMENT_LINE))
                CheckStatement.check(line);

            line = reader.readLine();
        }
        SymbolTable.destroy();
    }
}
