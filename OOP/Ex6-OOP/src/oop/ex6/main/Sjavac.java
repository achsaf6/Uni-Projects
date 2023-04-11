package oop.ex6.main;

import oop.ex6.check.CheckMethod;
import oop.ex6.check.CheckMethodDecleration;
import oop.ex6.check.CheckVariableStatement;
import oop.ex6.syntax.CodeLine;
import oop.ex6.syntax.SymbolTable;

import java.io.*;
import java.sql.SQLOutput;

public class Sjavac {
    private static final int IO_ERROR_CODE = 2;
    private static final int ILLEGAL_CODE_ERROR = 1;
    private static final int SUCCESSFUL_COMPILATION_CODE = 0;

    /**
     * The main function
     * @param args First argument should be a path to a sjava file
     */
    public static void main(String[] args){
        Sjavac.verifyCompilation(args[0]);
    }

    /**
     * Verifys the compilation of a given sjava file
     * @param fileName The path to the sjava file
     */
    public static void verifyCompilation (String fileName) {
        int output = SUCCESSFUL_COMPILATION_CODE;
        try {
            new SymbolTable();
            Sjavac.buildGlobalSymbolTable(fileName);
            Sjavac.verifyMethodBodies(fileName);
            SymbolTable.destroy();
        } catch (IOException e) {
            SymbolTable.resetSymbolTable();
            output = IO_ERROR_CODE;
            System.err.println(e.getMessage());
        } catch (IllegalCodeException e) {
            SymbolTable.resetSymbolTable();
            output = ILLEGAL_CODE_ERROR;
            System.err.println(e.getMessage());
        }
        System.out.println(output);
    }

    // Verifys the bodies of the methods in the code
    private static void verifyMethodBodies(String fileName) throws IOException, IllegalCodeException {
        BufferedReader reader;
        reader = new BufferedReader(new FileReader(fileName));
        String line = reader.readLine();
        CodeLine codeLine;

        while (line != null) {
            codeLine = new CodeLine(line);

            if (codeLine.getLineType().equals(CodeLine.SCOPE_OPENER))
                CheckMethod.check(line, reader);

            line = reader.readLine();
        }
        reader.close();
    }

    // Build the global symbol table and checks global lines of code
    private static void buildGlobalSymbolTable(String fileName) throws IOException, IllegalCodeException {
        BufferedReader reader;
        reader = new BufferedReader(new FileReader(fileName));
        String line = reader.readLine();
        int scopeLevel = 0;
        while (line != null) {
            CodeLine codeLine = new CodeLine(line);
            if (scopeLevel == 0 && codeLine.getLineType().equals(CodeLine.STATEMENT_LINE))
                CheckVariableStatement.check(line);
            if (codeLine.getLineType().equals(CodeLine.SCOPE_CLOSER))
                scopeLevel -= 1;
            else if (codeLine.getLineType().equals(CodeLine.SCOPE_OPENER)) {
                if (scopeLevel == 0)
                    CheckMethodDecleration.check(line);
                scopeLevel += 1;
            }
            if (scopeLevel < 0)
                throw new TooManyScopeClosersException("Extra }");

            line = reader.readLine();
        }
    }
}
