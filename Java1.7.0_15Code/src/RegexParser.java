import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * The RegexParser class parses the regex on each line of the Spec file by
 * 1. feeding that line to a SpecReader and then
 * 2. parsing the regex using a recursive descent parser which
 * 3. requests tokens from the SpecReader to decide how to parse
 *
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 2/24/13
 * Time: 7:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegexParser {

    SpecReader specReader;
    BufferedReader br;
    // just for checking if there are still lines in the Spec file
    BufferedReader checker;

    /**
     * Constructor creates two BufferedReaders for the same Spec file,
     * one for the SpecReader and one just to check for the end of the file,
     * and creates a SpecReader
     * @param spec :the Spec file
     */
    public RegexParser(File spec) {
        try {
            br = new BufferedReader(new FileReader(spec));
            checker = new BufferedReader(new FileReader(spec));
            specReader = new SpecReader(br);
        }
        catch (Exception e) {
            System.out.println("Regex parser construction error");
            System.exit(0);
        }
    }

    /**
     * Parses the entire Spec file
     */
    public void parse() {
        try {
            // while not at the end of the Spec file
            while (checker.readLine() != null) {
                // if the line can be split up into a class and a corresponding regex
                if (specReader.set_up_new_line()) {
                    // parse through the regex using a recursive descent parser
                    reg_ex();
                    // if the recursive descent parser finishes, then make sure the entire regex has been parsed, otherwise throw an error
                    specReader.matchToken(SpecReader.terminal.END);
                    // if the recursive descent parser finishes and the entire regex has been parsed, then the parse for that line was successful
                    // System.out.println("Parse successful!");
                }
            }
        }
        catch (Exception E) {
            System.out.println("readLine error");
            System.exit(0);
        }
    }

    /**
     * The root of the recursive descent parser
     */
    private void reg_ex() {
        rexp();
    }

    private void rexp() {
        rexp1();
        rexp_prime();
    }

    private void rexp_prime() {
        SpecReader.terminal token = specReader.peekToken();
        if (token == SpecReader.terminal.UNION) {
            specReader.matchToken(token);
            rexp1();
            rexp_prime();
        }
        else {
            return;
        }
    }

    private void rexp1() {
        rexp2();
        rexp1_prime();
    }

    private void rexp1_prime() {
        SpecReader.terminal token = specReader.peekToken();
        if (token == SpecReader.terminal.PAR_OPEN || token == SpecReader.terminal.RE_CHAR || token == SpecReader.terminal.PERIOD || token == SpecReader.terminal.SQUARE_OPEN || token == SpecReader.terminal.DEFINED) {
            rexp2();
            rexp1_prime();
        }
        else {
            return;
        }
    }

    private void rexp2() {
        SpecReader.terminal token = specReader.peekToken();
        if (token == SpecReader.terminal.PAR_OPEN) {
            specReader.matchToken(token);
            rexp();
            specReader.matchToken(SpecReader.terminal.PAR_CLOSE);
            rexp2_tail();
        }
        else if (token == SpecReader.terminal.RE_CHAR) {
            specReader.matchToken(token);
            rexp2_tail();
        }
        else {
            rexp3();
        }
    }

    private void rexp2_tail() {
        SpecReader.terminal token = specReader.peekToken();
        if (token == SpecReader.terminal.STAR) {
            specReader.matchToken(token);
        }
        else if (token == SpecReader.terminal.PLUS) {
            specReader.matchToken(token);
        }
        else {
            return;
        }
    }

    private void rexp3() {
        SpecReader.terminal token = specReader.peekToken();
        if (token == SpecReader.terminal.PERIOD || token == SpecReader.terminal.SQUARE_OPEN || token == SpecReader.terminal.DEFINED) {
            char_class();
        }
        else {
            return;
        }
    }

    private void char_class() {
        SpecReader.terminal token = specReader.peekToken();
        if (token == SpecReader.terminal.PERIOD) {
            specReader.matchToken(token);
        }
        else if (token == SpecReader.terminal.SQUARE_OPEN) {
            specReader.matchToken(token);
            char_class1();
        }
        else if (token == SpecReader.terminal.DEFINED) {
            specReader.matchToken(token);
        }
        else {
            specReader.throwError(token, SpecReader.terminal.PERIOD, SpecReader.terminal.SQUARE_OPEN, SpecReader.terminal.DEFINED);
        }
    }

    private void char_class1() {
        SpecReader.terminal token = specReader.peekToken();
        if (token == SpecReader.terminal.CLS_CHAR || token == SpecReader.terminal.SQUARE_CLOSE) {
            char_set_list();
        }
        else if (token == SpecReader.terminal.CARROT) {
            exclude_set();
        }
        else {
            specReader.throwError(token, SpecReader.terminal.CLS_CHAR, SpecReader.terminal.SQUARE_CLOSE, SpecReader.terminal.CARROT);
        }
    }

    private void char_set_list() {
        SpecReader.terminal token = specReader.peekToken();
        if (token == SpecReader.terminal.CLS_CHAR) {
            char_set();
            char_set_list();
        }
        else if (token == SpecReader.terminal.SQUARE_CLOSE) {
            specReader.matchToken(token);
        }
        else {
            specReader.throwError(token, SpecReader.terminal.CLS_CHAR, SpecReader.terminal.SQUARE_CLOSE);
        }
    }

    private void char_set() {
        specReader.matchToken(SpecReader.terminal.CLS_CHAR);
        char_set_tail();
    }

    private void char_set_tail() {
        SpecReader.terminal token = specReader.peekToken();
        if (token == SpecReader.terminal.DASH) {
            specReader.matchToken(token);
            specReader.matchToken(SpecReader.terminal.CLS_CHAR);
        }
        else {
            return;
        }
    }

    private void exclude_set() {
        specReader.matchToken(SpecReader.terminal.CARROT);
        char_set();
        specReader.matchToken(SpecReader.terminal.SQUARE_CLOSE);
        specReader.matchToken(SpecReader.terminal.IN);
        exclude_set_tail();
    }

    private void exclude_set_tail() {
        SpecReader.terminal token = specReader.peekToken();
        if (token == SpecReader.terminal.SQUARE_OPEN) {
            specReader.matchToken(token);
            char_set();
            specReader.matchToken(SpecReader.terminal.SQUARE_CLOSE);
        }
        else if (token == SpecReader.terminal.DEFINED) {
            specReader.matchToken(token);
        }
        else {
            specReader.throwError(token, SpecReader.terminal.SQUARE_OPEN, SpecReader.terminal.DEFINED);
        }
    }
}
