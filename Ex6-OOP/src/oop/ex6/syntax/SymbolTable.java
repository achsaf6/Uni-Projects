package oop.ex6.syntax;

import oop.ex6.main.IllegalCodeException;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolTable {
    private static SymbolTable curTable;
    private static int totalSymbolTables = 0;
    private final HashMap<String ,Variable > variablesMap;
    private final HashMap<String ,Method > methodsMap;
    private final SymbolTable prev;
    private static SymbolTable globalScope = null;
    private int symbolTableID;

    /**
     * Constructs a new Symbol table, should be called every time a new scope is entered
     */
    public SymbolTable() {
        if (curTable == null) {
            this.prev = null;
            globalScope = this;
        } else {
            this.prev = curTable;
        }
        curTable = this;
        this.variablesMap = new HashMap<>();
        this.methodsMap = new HashMap<>();
        this.symbolTableID = totalSymbolTables++;
    }

    /**
     * Creates a copy of the global scope that appears before the original
     */
    public static void createGlobalScopeCopy() {
        new SymbolTable();
        for (var variableName : globalScope.variablesMap.keySet()) {
            curTable.variablesMap.put(variableName, new Variable(globalScope.variablesMap.get(variableName)));
        }
    }

    /**
     * resets the symbol, useful for cases in which an exception is thrown
     */
    public static void resetSymbolTable() {
        while (curTable != null)
            curTable = curTable.prev;
    }

    /**
     * Adds a methods argument to the current symboltable
     * @param funcName The name of the function whose variables are to be added
     * @throws IllegalCodeException
     */
    public static void addMethodArguments(String funcName) throws IllegalCodeException {
        for (var variable : globalScope.methodsMap.get(funcName).getParameterList())
            SymbolTable.add(variable);
    }

    /**
     * Each symbolTable gets a unique ID this returns it, helps for debugging purposes
     * NOTE: this returns the ID but since the symbolTable operates like a stack, don't expect every ID to
     * be fuflilled.
     *
     * Ex.    4<-5<-7
     *
     * this function may be buggy I didn't test it
     *
     * Symboltable 6 was destroyed and a new symboltable was added to the stack
     *
     * @return
     */
    public int getID(){
        return this.symbolTableID;
    }


    /**
     *
     * Adds given variable to the current Symbol Table
     * @param var -- Variable
     * @throws IllegalCodeException - Throws exception in the event that the name already exists in the scope
     */
    public static void add(Variable var) throws IllegalCodeException {
        if ( curTable.variablesMap.containsKey(var.getName()) ){
            throw new ScopeCollisionException("Two variables cant have the same name in the same scope\n" +
                    "Variable name: " + var.getName());
        }
        curTable.variablesMap.put(var.getName(), var);
    }

    /**
     *
     * Adds given method to the current Symbol Table
     * @param meth -- Method
     * @throws IllegalCodeException - Throws exception in the event that the name already exists in the scope
     */
    public static void add(Method meth) throws IllegalCodeException {
        if ( curTable.methodsMap.containsKey(meth.getName()) ){
            throw new ScopeCollisionException("Two methods cant have the same name\n" +
                    "Method Name: " + meth.getName());
        }
        curTable.methodsMap.put(meth.getName(), meth);
    }


    /**
     * Checks if given name exists in the symbol table, if so returns it, if not returns null
     * @param varName - name of variable
     * @return Varible or null depends on existence
     */
    public static Variable containsVar(String varName) {
        SymbolTable monkey = curTable;
        while (monkey != null){
            if (monkey.variablesMap.containsKey(varName)) {
                return monkey.variablesMap.get(varName);
            }
            monkey = monkey.prev;
        }
        return null;
    }

    /**
     * Methods can only be declared in the global scope, therefore this only checks the global scope for
     * any given method. If not found returns null
     * @param methName -- name of the method
     * @return -- Method or null depending on existence
     */
    public static Method containsMethod(String methName) {
        if (globalScope.methodsMap.containsKey(methName)) {
            return globalScope.methodsMap.get(methName);
        }
        return null;
    }


    /**
     * Adds all the arguments to the symbol table of the given function to be used as local variables
     *
     * this should be called when you enter a function, but only AFTER you initialize the symbolTable of
     * the scope
     * @param methName -- method that was just entered
     * @throws Exception
     */
    public static void addMethodArgsToScope(String methName) throws IllegalCodeException {
        ArrayList<Variable> argList = globalScope.methodsMap.get(methName).getParameterList();
        for (Variable arg : argList) {
            SymbolTable.add(arg);
        }
    }

    /**
     * removes topmost symbolTable from the linked chain
     * @throws Exception - Can only be thrown if symbol table is called before a previous one has been made
     *                      in other words the global symbol table was not called
     */
    public static void destroy() throws ScopeCollisionException {
        if (curTable == null) {
            throw new ScopeCollisionException("No symbolTables to destroy :'(");
        }
        curTable = curTable.prev;
    }

}
