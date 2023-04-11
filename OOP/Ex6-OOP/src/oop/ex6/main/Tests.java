package oop.ex6.main;


import oop.ex6.check.*;
import oop.ex6.syntax.InvalidAssignmentException;
import oop.ex6.syntax.SymbolTable;

/**
 * All the tests that we need to pass and whatever else needs to pass
 * not meant for submission
 */
public class Tests {


    public static void main(String[] args) throws Exception {




//        NOTE: these tests don't CheckMethodDecleration that the symbol table works
//        legal variable statments
        String[] legalStatments = {
                "final int       a  = +1 ; ",
                "String a = \"hello\" , b = \"goodbye\";",
                "char c='Z', f;",
                "double a, b;",
                "double a = -6;",
                "int       a  = 001 ;",
                "int a = 1;",
                "int a = -11;",
                "String a = \"1\";",
                "int i1, i2 = 6;",
                "boolean a, __ ,c , d = true, e, f = 5;",
                "String a = \"\";"
                };
        for (String legalStatment : legalStatments){
            new SymbolTable();
            CheckVariableStatement.check(legalStatment);
            SymbolTable.destroy();
        }
        new SymbolTable();
        CheckVariableStatement.check("int a = 1;");
        CheckVariableStatement.check("int b = a;");
        SymbolTable.destroy();
        System.out.println("Legal Statments Passed");


        String[] invalidAssignments = {
                "int 9a = 0; ",
                "int a = \"Hello\";",
                "String b = 7.5;",
                "double 2b = 7.5;",
                "String _ = \"qwert\";",
                "double c = true;",
                "char c = \"qwert\";"
        };
        for (String ilegalStatment : invalidAssignments){
            new SymbolTable();
            try {
                CheckVariableStatement.check(ilegalStatment);
                throw new Exception("failed to catch invalidAssignments statement");
            } catch (InvalidAssignmentException e) {
            }
            SymbolTable.destroy();
        }
        try {
            new SymbolTable();
            CheckVariableStatement.check("int a;");
            CheckVariableStatement.check("int b = a;");
            throw new Exception("failed to catch invalid Assignment");
        } catch (InvalidAssignmentException e) {
            SymbolTable.destroy();
        }
        System.out.println("invalidAssignments Passed");

        String[] UnknownVariable = {
                "a = 1;",
                "private int       a  = +1 ;",
        };
        for (String ilegalStatment : UnknownVariable){
            new SymbolTable();
            try {
                CheckVariableStatement.check(ilegalStatment);
                throw new Exception("failed to catch UnknownVariableException statment");
            } catch (UnknownObjectException e) {
            }
            SymbolTable.destroy();
        }
        System.out.println("UnknownVariableException Passed");


        String[] UnknownFormat = {
                "char a = 'x';;",
                "int b = 3; int a = 2;"
        };
        for (String ilegalStatment : UnknownFormat){
            new SymbolTable();
            try {
                CheckVariableStatement.check(ilegalStatment);
                throw new Exception("failed to catch UnknownFormatException statment");
            } catch (UnknownFormatException e) {
            }
            SymbolTable.destroy();
        }
        System.out.println("UnknownFormatException Passed");


        String[] validMethods = {
                "void foo(){",
                "void foo(final int a) {",
                "void foo(final int a, String b) {",
                "void foo(int a) {"
        };

        for ( String method : validMethods){
            new SymbolTable();
            CheckMethodDecleration.check(method);
            SymbolTable.destroy();
        }
        System.out.println("Legal method declarations passed");

        new SymbolTable();
        CheckMethodDecleration.check("void foo(final int a){");
        SymbolTable.addMethodArgsToScope("foo");
        CheckVariableStatement.check("int b = a;");
        CheckMethodCall.check("foo(3);");


    }
}

