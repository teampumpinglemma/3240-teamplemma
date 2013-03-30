import java.io.BufferedReader;
import java.util.ArrayList;

/**
 * The SpecReader class separates each line of the Spec file into the class name and the corresponding regex,
 * and reads the characters of the regexes and provides the corresponding tokens for the RegexParser
 *
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 3/26/13
 * Time: 3:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class SpecReader {

    // are the characters being read inside of square brackets?
    boolean inSquare;
    // the regex portion of the current line
    String regex;
    // the un-parsed regex portion of the current line
    String to_read;
    // the previously defined classes
    ArrayList<String> defined;
    BufferedReader br;

    // the tokens provided to the RegexParser
    public enum terminal {UNION, PAR_OPEN, PAR_CLOSE, RE_CHAR, STAR, PLUS, PERIOD, SQUARE_OPEN, DEFINED, CLS_CHAR, SQUARE_CLOSE, CARROT, DASH, IN, END}

    /**
     * Constructor sets that the regexes begin outside of square brackets,
     * creates the ArrayList for defined classes,
     * and takes in a BufferedReader for reading the Spec file
     *
     * @param br :for reading the Spec file
     */
    public SpecReader(BufferedReader br) {
        inSquare = false;
        defined = new ArrayList<String>();
        this.br = br;
    }

    /**
     * Attempts to divide up new line into class name and regex
     *
     * @return :is the line not empty? if the line is empty, then RegexParser skips it
     */
    public boolean set_up_new_line() {
        try {
            to_read = br.readLine();
        }
        catch (Exception e) {
            System.out.println("Could not read line");
            System.exit(0);
        }
        // rids string of leading and trailing whitespace
        to_read = to_read.trim();
        // if line is not empty
        if (to_read.length() > 0) {
            // parse out the class name and add it to the defined classes list
            String definition = to_read.substring(0, to_read.indexOf(' '));
            // requires class names to start with $
            if (definition.charAt(0) != '$') {
                System.out.println("Class definitions should start with $");
                System.exit(0);
            }
            defined.add(definition);
            // finds the first space after the class name, and sets the beginning of the regex to that
            to_read = to_read.substring(to_read.indexOf(' '));
            to_read.trim();
            regex = to_read;
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Reads the next character in the regex (and a few more if necessary) and returns the corresponding token,
     * but does not delete any characters from to_read
     *
     * @return :token corresponding to the current character (or characters)
     */
    public terminal peekToken() {
        terminal t = null;
        try {
            // if to_read is empty, then we are at the end of the regex
            if (to_read.length() == 0) {
                return terminal.END;
            }
            // mark the current to_read string so that no characters will be lost
            String mark = to_read;
            // the first character in to_read
            char c = to_read.charAt(0);
            // remove the first character from to_read
            to_read = to_read.substring(1);
            // if c is not inside of square brackets
            if (!inSquare) {
                // if c is whitespace, then progress along to_read until c is not whitespace
                while (c == ' ') {
                    mark = to_read;
                    c = to_read.charAt(0);
                    to_read = to_read.substring(1);
                }
                // if c is a dollar-sign
                if (c == '$') {
                    int end = 0;
                    // keep reading characters until finding one that is not a capital letter or number
                    while (end < to_read.length() && ((to_read.charAt(end) >= 'A' && to_read.charAt(end) <= 'Z') || (to_read.charAt(end) >= '0' && to_read.charAt(end) <= '9'))) {
                        end++;
                    }
                    // check if those characters form a defined class
                    if (defined.contains("$" + to_read.substring(0, end))) {
                        t = terminal.DEFINED;
                    }
                    // otherwise the dollar-sign is just a regular character and is not signifying a defined class
                    else {
                        t = terminal.RE_CHAR;
                    }
                }
                // if c is a capital I
                else if (c == 'I') {
                    // check if the next character is an N and that the next character after that is not another capital letter or number
                    if (to_read.charAt(0) == 'N' && !(to_read.charAt(1) >= 'A' && to_read.charAt(1) <= 'Z') || (to_read.charAt(1) >= '0' && to_read.charAt(1) <= '9')) {
                        t = terminal.IN;
                    }
                    // otherwise the I is just a regular character and is not a part of the keyword IN
                    else {
                        t = terminal.RE_CHAR;
                    }
                }
                // if c is any printable ASCII character except for space, backslash, star, plus, question-mark, union-mark, square bracket, parentheses, period, apostrophe, or quotation-mark, then it is a regular character
                else if (c >= ' ' && c <= '~' && c != ' ' && c != '\\' && c != '*' && c != '+' && c != '?' && c != '|' && c != '[' && c != ']' && c != '(' && c != ')' && c != '.'&& c != '\'' && c != '\"') {
                    t = terminal.RE_CHAR;
                }
                // but the excluded printable characters can be escaped
                else if (c == '\\') {
                    if (to_read.charAt(0) == ' ' || to_read.charAt(0) == '\\' || to_read.charAt(0) == '*' || to_read.charAt(0) == '+' || to_read.charAt(0) == '?' || to_read.charAt(0) == '|' || to_read.charAt(0) == '[' || to_read.charAt(0) == ']' || to_read.charAt(0) == '(' || to_read.charAt(0) == ')' || to_read.charAt(0) == '.' || to_read.charAt(0) == '\'' || to_read.charAt(0) == '\"') {
                        t = terminal.RE_CHAR;
                    }
                    // if you're trying to escape something that shouldn't be escaped, an error is thrown
                    else {
                        t = null;
                    }
                }
                else if (c == '*') {
                    t = terminal.STAR;
                }
                else if (c == '+') {
                    t = terminal.PLUS;
                }
                // no instructions for what to do with this
                else if (c == '?') {
                    t = null;
                }
                else if (c == '|') {
                    t = terminal.UNION;
                }
                else if (c == '[') {
                    t = terminal.SQUARE_OPEN;
                }
                else if (c == ']') {
                    t = terminal.SQUARE_CLOSE;
                }
                else if (c == '(') {
                    t = terminal.PAR_OPEN;
                }
                else if (c == ')') {
                    t = terminal.PAR_CLOSE;
                }
                else if (c == '.') {
                    t = terminal.PERIOD;
                }
                // no instructions for what to do with this
                else if (c == '\'') {
                    t = null;
                }
                // no instructions for what to do with this
                else if (c == '\"') {
                    t = null;
                }
                // if c matches with none of the above scenarios, then an error is thrown
                else {
                    t = null;
                }
            }
            // if c is inside of square brackets
            else {
                // if c is any printable ASCII character except for backslash, carrot, dash, or square bracket, then it is a closed character
                if (c >= ' ' && c <= '~' && c != '\\' && c != '^' && c != '-' && c != '[' && c != ']') {
                    t = terminal.CLS_CHAR;
                }
                // but the excluded printable characters can be escaped
                else if (c == '\\') {
                    if (to_read.charAt(0) == '\\' || to_read.charAt(0) == '^' || to_read.charAt(0) == '-' || to_read.charAt(0) == '[' || to_read.charAt(0) == ']') {
                        t = terminal.CLS_CHAR;
                    }
                    // if you're trying to escape something that shouldn't be escaped, an error is thrown
                    else {
                        t = null;
                    }
                }
                else if (c == '^') {
                    t = terminal.CARROT;
                }
                else if (c == '-') {
                    t = terminal.DASH;
                }
                else if (c == '[') {
                    t = terminal.SQUARE_OPEN;
                }
                else if (c == ']') {
                    t = terminal.SQUARE_CLOSE;
                }
                // if c matches with none of the above scenarios, then an error is thrown
                else {
                    t = null;
                }
            }
            // restore to_read undo any deletion of characters
            to_read = mark;
        }
        catch (Exception e) {
            System.out.println("peekToken error");
            System.exit(0);
        }
        return t;
    }

    /**
     * Reads the next character in the regex (and a few more if necessary) and checks to see if the corresponding token matches what was expected,
     * if it does match the expected token, then the characters that produced that token are deleted from to_read,
     * and if instead it does not match then an error is thrown
     *
     * It's set up the same way as peekToken(), so I'm not going to comment any lines inside
     *
     * @param expected :the token that is expected from the next character or characters
     */
    public void matchToken(terminal expected) {
        try {
            if (to_read.length() == 0) {
                if (expected != terminal.END) {
                    throwError(terminal.END, expected);
                }
                else {
                    return;
                }
            }
            char c = to_read.charAt(0);
            to_read = to_read.substring(1);
            if (!inSquare) {
                while (c == ' ') {
                    c = to_read.charAt(0);
                    to_read = to_read.substring(1);
                }
                if (c == '$') {
                    int end = 0;
                    while (end < to_read.length() && ((to_read.charAt(end) >= 'A' && to_read.charAt(end) <= 'Z') || (to_read.charAt(end) >= '0' && to_read.charAt(end) <= '9'))) {
                        end++;
                    }
                    if (defined.contains("$" + to_read.substring(0, end))) {
                        matchAndProgress(terminal.DEFINED, expected, end + 1);
                    }
                    else {
                        matchAndProgress(terminal.RE_CHAR, expected, 1);
                    }
                }
                else if (c == 'I') {
                    if (to_read.charAt(0) == 'N' && !(to_read.charAt(1) >= 'A' && to_read.charAt(1) <= 'Z') || (to_read.charAt(1) >= '0' && to_read.charAt(1) <= '9')) {
                        matchAndProgress(terminal.IN, expected, 2);
                    }
                    else {
                        matchAndProgress(terminal.RE_CHAR, expected, 1);
                    }
                }
                else if (c >= ' ' && c <= '~' && c != ' ' && c != '\\' && c != '*' && c != '+' && c != '?' && c != '|' && c != '[' && c != ']' && c != '(' && c != ')' && c != '.'&& c != '\'' && c != '\"') {
                    matchAndProgress(terminal.RE_CHAR, expected, 1);
                }
                else if (c == '\\') {
                    if (to_read.charAt(0) == ' ' || to_read.charAt(0) == '\\' || to_read.charAt(0) == '*' || to_read.charAt(0) == '+' || to_read.charAt(0) == '?' || to_read.charAt(0) == '|' || to_read.charAt(0) == '[' || to_read.charAt(0) == ']' || to_read.charAt(0) == '(' || to_read.charAt(0) == ')' || to_read.charAt(0) == '.' || to_read.charAt(0) == '\'' || to_read.charAt(0) == '\"') {
                        matchAndProgress(terminal.RE_CHAR, expected, 2);
                    }
                    else {
                        throwError(null, expected);
                    }
                }
                else if (c == '*') {
                    matchAndProgress(terminal.STAR, expected, 1);
                }
                else if (c == '+') {
                    matchAndProgress(terminal.PLUS, expected, 1);
                }
                else if (c == '?') {
                    throwError(null, expected);
                }
                else if (c == '|') {
                    matchAndProgress(terminal.UNION, expected, 1);
                }
                else if (c == '[') {
                    matchAndProgress(terminal.SQUARE_OPEN, expected, 1);
                    inSquare = true;
                }
                else if (c == ']') {
                    matchAndProgress(terminal.SQUARE_CLOSE, expected, 1);
                }
                else if (c == '(') {
                    matchAndProgress(terminal.PAR_OPEN, expected, 1);
                }
                else if (c == ')') {
                    matchAndProgress(terminal.PAR_CLOSE, expected, 1);
                }
                else if (c == '.') {
                    matchAndProgress(terminal.PERIOD, expected, 1);
                }
                else if (c == '\'') {
                    throwError(null, expected);
                }
                else if (c == '\"') {
                    throwError(null, expected);
                }
                else {
                    throwError(null, expected);
                }
            }
            else {
                if (c >= ' ' && c <= '~' && c != '\\' && c != '^' && c != '-' && c != '[' && c != ']') {
                    matchAndProgress(terminal.CLS_CHAR, expected, 1);
                }
                else if (c == '\\') {
                    if (to_read.charAt(0) == '\\' || to_read.charAt(0) == '^' || to_read.charAt(0) == '-' || to_read.charAt(0) == '[' || to_read.charAt(0) == ']') {
                        matchAndProgress(terminal.RE_CHAR, expected, 2);
                    }
                    else {
                        throwError(null);
                    }
                }
                else if (c == '^') {
                    matchAndProgress(terminal.CARROT, expected, 1);
                }
                else if (c == '-') {
                    matchAndProgress(terminal.DASH, expected, 1);
                }
                else if (c == '[') {
                    matchAndProgress(terminal.SQUARE_OPEN, expected, 1);
                }
                else if (c == ']') {
                    matchAndProgress(terminal.SQUARE_CLOSE, expected, 1);
                    inSquare = false;
                }
                else {
                    throwError(null, expected);
                }
            }
        }
        catch (Exception e) {
            System.out.println("matchToken error");
            System.exit(0);
        }
    }

    /**
     * Check to see if the token provided by the characters in to_read matches the expected token
     *
     * @param received :the token provided by the characters in to_read
     * @param expected :the token expected by the RegexParser
     * @param progress :how many characters to delete from to_read if the tokens match
     */
    private void matchAndProgress(terminal received, terminal expected, int progress) {
        if (expected != received) {
            throwError(received, expected);
        }
        else {
            to_read = to_read.substring(progress - 1);
        }
    }

    /**
     * Ends the program and prints out what token was received from to_read and all of the expected tokens that would have sufficed
     *
     * @param received :the token provided by the characters in to_read
     * @param expected :the tokens that would have been accepted
     */
    public void throwError(terminal received, terminal... expected) {
        if (received != null) {
            System.out.print("Got " + received.toString() + " ...expecting ");
        }
        else {
            System.out.print("Got null ...expecting ");
        }
        for (int i = 0; i < expected.length - 1; i++) {
            System.out.print(expected[i].toString() + " or ");
        }
        System.out.println(expected[expected.length - 1]);
        System.exit(0);
    }

}
