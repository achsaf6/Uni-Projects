package oop.ex6.syntax;

import oop.ex6.main.IllegalCodeException;

import java.util.HashMap;
import java.util.List;

public class Variable {
    public static String modifiersRegex = "final";
    public static final HashMap<String, String> typeValMap;

    static {
        typeValMap  = new HashMap<>();
        typeValMap.put("int","[+-]*\\d+");
        typeValMap.put("double","[-+]*(\\.\\d+|\\d+(\\.\\d+)?)");
        typeValMap.put("String", "\".*\"");
        typeValMap.put("boolean", "true|false|[-+]*(\\.\\d+|\\d+(\\.\\d+)?)");
        typeValMap.put("char", "'.'");
    }
    public static final HashMap<String, String> typeDefaultValMap;

    static {
        typeDefaultValMap  = new HashMap<>();
        typeDefaultValMap.put("int", "0" );
        typeDefaultValMap.put("double","0.0");
        typeDefaultValMap.put("String", "\"\"");
        typeDefaultValMap.put("boolean", "false");
        typeDefaultValMap.put("char", "'.'");
    }
    public static final String typeRegEx = typeValMap.keySet().toString()
            .replaceAll("[\\[\\] ]","")
            .replace(",","|");
    public  static final String nameRegexLegal = "\\b([^\\d|\\s]\\w*)\\b";
    public  static final String nameRegexIllegal = "\\b_\\b";
    private final String name;
    private final String type;
    private final String modifiers;
    private String value;

    /**
     * Constructor that gives variable instance a default value
     * @param modifiers -- final or null
     * @param type -- type of value
     * @param name -- name of value
     * @throws Exception
     */
    public Variable(String modifiers, String type, String name) throws IllegalCodeException {
        this(modifiers, type, name, typeDefaultValMap.get(type));
    }

    /**
     * Creates an instance of a variable
     * @param modifier -- modifier
     * @param type -- type
     * @param name -- name
     * @param value -- value
     */
    public Variable(String modifier, String type, String name, String value) throws IllegalCodeException {
        if (value != null && !value.matches(typeValMap.get(type))){
            throw new InvalidAssignmentException("Wrong value assigned to this type\n" +
                    "VarName: " + name +
                    "\nType Expected: " + type +
                    "\nValue got: " + value);
        }
        if (name.matches(nameRegexIllegal) || !name.matches(nameRegexLegal)){
            throw new InvalidAssignmentException("Illegal name of variable\nName: " + name);
        }
        if (modifier != null && !modifier.matches(modifiersRegex)){
            throw new InvalidAssignmentException("Unrecognized modifier\nModifier: " + modifier);
        }
        this.name = name;
        this.type = type;
        this.modifiers = modifier;
        this.value = value;
    }

    /**
     * Copy constructor for Variable
     * @param otherVar The variable to copy
     */
    public Variable(Variable otherVar) {
        this.name = otherVar.getName();
        this.modifiers = otherVar.getModifiers();
        this.value = otherVar.getValue();
        this.type = otherVar.getType();
    }

    /**
     * changes the value of the variable instance
     * @param value -- value to be changed
     * @throws Exception
     */
    public void setValue(String value) throws IllegalCodeException {
        if (value == null){
            throw new InvalidAssignmentException("Tried to assign an uninitialized value to variable\n" +
                    "VarName: " + this.getName());
        }
        if (!value.matches(typeValMap.get(this.type))){
            throw new InvalidAssignmentException("Wrong value assigned to this type\n" +
                    "VarName: " + this.name +
                    "\nType Expected: " + this.type +
                    "\nValue got: " + value);
        }
        if (this.modifiers != null && this.modifiers.equals("final")){
            throw new InvalidAssignmentException("Can't reassign a final variable" +
                    "\nVariable: " + name + "\nTried to Assign: " + value);
        }
//        needs to CheckMethodDecleration if value matches type
        this.value = value;
    }

    /**
     * checks that given parameter is of the same type as is expected of this variable
     * @param param -- param to verify
     * @return
     */
    public boolean isValidParam(String param) {
        return param.matches(typeValMap.get(this.type))
                || (SymbolTable.containsVar(param) != null &&
                SymbolTable.containsVar(param).getValue() != null &&
                SymbolTable.containsVar(param).getValue().matches(typeValMap.get(this.type)));
    }

    /**
     * Getter for variable value
     * @return The variables value
     */
    public String getValue() {
        return value;
    }
    /**
     * Getter for variable name
     * @return The variables name
     */
    public String getName() {
        return name;
    }
    /**
     * Getter for variable type
     * @return The variables type
     */
    public String getType() {
        return type;
    }
    /**
     * Getter for variable modifiers
     * @return The variables modifiers
     */
    public String getModifiers() {
        return modifiers;
    }
}
