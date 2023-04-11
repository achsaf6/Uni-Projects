package oop.ex6.syntax;

import oop.ex6.check.UnknownFormatException;

import java.util.ArrayList;
import java.util.List;

public class Method {

    private final String name;
    private final ArrayList<Variable> parameterList;

    public  static final String nameRegexLegal = "\\b([^\\d|\\s|_]\\w*)\\b";
    public  static final String returnTypeRegexLegal = "void";

    /**
     * creates a method instance
     * @param returnType -- void only
     * @param name -- name of method
     * @param parameterList -- the parameters this method accepts
     * @throws InvalidAssignmentException
     * @throws UnknownFormatException
     */
    public Method(String returnType, String name, ArrayList<Variable> parameterList)
            throws InvalidAssignmentException, UnknownFormatException {
        if (!returnType.matches(returnTypeRegexLegal)){
            throw new UnknownFormatException("Unknown return type\nType: " + returnType);
        }
        if (!name.matches(nameRegexLegal)){
            throw new InvalidAssignmentException("Illegal name of variable\nName: " + name);
        }
        this.name = name;
        this.parameterList = parameterList;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Variable> getParameterList() {
        return parameterList;
    }

    public Variable getParamAt(int index) {
        return parameterList.get(index);
    }
}
