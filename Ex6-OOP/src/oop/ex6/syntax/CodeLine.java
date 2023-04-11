package oop.ex6.syntax;

import java.util.regex.Pattern;

public class CodeLine {
    // Line Patterns
    public static Pattern COMMENT_PATTERN = Pattern.compile("^\\s*+//");
    public static Pattern SCOPE_OPENER_PATTERN = Pattern.compile("\\{\\s*+$");
    public static Pattern SCOPE_CLOSER_PATTERN = Pattern.compile("}\\s*+$");
    public static Pattern SCOPE_CLOSER_FULL_line_PATTERN = Pattern.compile("\\s*+}\\s*+$");
    public static Pattern STATEMENT_PATTERN = Pattern.compile(";\\s*+$");

    // Line types
    public final static String EMPTY_LINE = "Empty";
    public final static String COMMENT_LINE = "Comment";
    public final static String SCOPE_OPENER = "Scope Opener";
    public final static String SCOPE_CLOSER = "Scope Closer";
    public final static String STATEMENT_LINE = "Statement";

    private final String line;
    private String lineType;

    /**
     * Creates a CodeLine instance
     * @param line The line in the code
     * @throws InvalidLineException
     */
    public CodeLine (String line) throws InvalidLineException {
        this.line = line;
        setLineType();
    }

    // Sets the line type
    private void setLineType() throws InvalidLineException {
        if (line.isBlank())
            lineType = EMPTY_LINE;
        else if (COMMENT_PATTERN.matcher(line).find())
            lineType = COMMENT_LINE;
        else if (SCOPE_OPENER_PATTERN.matcher(line).find())
            lineType = SCOPE_OPENER;
        else if (SCOPE_CLOSER_PATTERN.matcher(line).find())
            lineType = SCOPE_CLOSER;
        else if (STATEMENT_PATTERN.matcher(line).find())
            lineType = STATEMENT_LINE;
        else
            throw new InvalidLineException("Line of code must end with ; or { or }");
        if (lineType.equals(SCOPE_CLOSER) && !SCOPE_CLOSER_FULL_line_PATTERN.matcher(line).matches())
            throw new InvalidLineException("} must be in own seperate line");

    }

    /**
     * A getter for the type of line this line is
     * @return The line type
     */
    public String getLineType () {
        return lineType;
    }
}
