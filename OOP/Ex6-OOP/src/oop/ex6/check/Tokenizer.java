package oop.ex6.check;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

    //all the characters that can be found with no whitespace separating them from other tokens
    public static final String regexTightCharacters = "(,)|(=)|(;)|(\\))|(\\()|(\\{)|(\\})";

    /**
     * Recieves a statment and splits it into tokens bases on whitespace and regexTightCharacters
     * @param statement -- the Text to tokenize
     * @return ArrayList of all the tokens
     */
    public static ArrayList<String> tokenize(String statement) {
        Matcher m = Pattern.compile(regexTightCharacters).matcher(statement);
        while (m.find()) {
            statement = statement.replace(m.group(), " " + m.group() + " ");
        }
        ArrayList<String> tokens = new ArrayList<>(List.of(statement.split("\\s+")));
        ArrayList<String> undesirables = new ArrayList<>();
        undesirables.add("");
        tokens.removeAll(undesirables);
        return tokens;
    }

    /**
     * ensures that the token at the head of the list is whats expected throws an Exception otherwise
     * @param s - char/token expected
     * @param tokens -- list to check
     * @throws UnexpectedEatException -- means that either the order was wrong or an unexpected variable was found
     */
    public static void eat(String s, ArrayList<String> tokens) throws UnexpectedEatException {
        if (tokens.get(0).equals(s)){
            tokens.remove(0);
        } else {
            throw new UnexpectedEatException("Didnt eat\n" +
                    "Expected: " + s + "\nGot: " + tokens.get(0));
        }
    }

}
