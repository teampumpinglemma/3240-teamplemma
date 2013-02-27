import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 2/24/13
 * Time: 7:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegexParser {
    public RegexParser(File f) {
        reg_ex();
    }

    private enum terminal {UNION, PAR_OPEN, PAR_CLOSE, RE_CHAR, STAR, PLUS, PERIOD, SQUARE_OPEN, DOLLAR, CLS_CHAR, SQUARE_CLOSE, CARROT, DASH, I, N}

    private terminal peekToken() {
        return null;
    }

    private void matchToken(terminal expected) {
        terminal received = null;
        if (received != expected) {
            throwError(received, expected);
        }
    }

    private void throwError(terminal token, terminal... expected) {
        System.out.print("Got " + token.toString() + "... expecting ");
        for (int i = 0; i < expected.length - 1; i++) {
            System.out.print(expected[i].toString() + " or ");
        }
        System.out.println(expected[expected.length - 1]);
        System.exit(0);
    }

    private void reg_ex() {
        rexp();
    }

    private void rexp() {
        rexp1();
        rexp_prime();
    }

    private void rexp_prime() {
        terminal token = peekToken();
        if (token == terminal.UNION) {
            matchToken(token);
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
        terminal token = peekToken();
        if (token == terminal.PAR_OPEN || token == terminal.RE_CHAR || token == terminal.PERIOD || token == terminal.SQUARE_OPEN || token == terminal.DOLLAR) {
            rexp2();
            rexp1_prime();
        }
        else {
            return;
        }
    }

    private void rexp2() {
        terminal token = peekToken();
        if (token == terminal.PAR_OPEN) {
            matchToken(token);
            rexp();
            matchToken(terminal.PAR_CLOSE);
            rexp2_tail();
        }
        else if (token == terminal.RE_CHAR) {
            matchToken(token);
            rexp2_tail();
        }
        else {
            rexp3();
        }
    }

    private void rexp2_tail() {
        terminal token = peekToken();
        if (token == terminal.STAR) {
            matchToken(token);
        }
        else if (token == terminal.PLUS) {
            matchToken(token);
        }
        else {
            return;
        }
    }

    private void rexp3() {
        terminal token =  peekToken();
        if (token == terminal.PERIOD || token == terminal.SQUARE_OPEN || token == terminal.DOLLAR) {
            char_class();
        }
        else {
            return;
        }
    }

    private void char_class() {
        terminal token =  peekToken();
        if (token == terminal.PERIOD) {
            matchToken(token);
        }
        else if (token == terminal.SQUARE_OPEN) {
            matchToken(token);
            char_class1();
        }
        else if (token == terminal.DOLLAR) {
            defined_class();
        }
        else {
            throwError(token, terminal.PERIOD, terminal.SQUARE_OPEN, terminal.DOLLAR);
        }
    }

    private void char_class1() {
        terminal token = peekToken();
        if (token == terminal.CLS_CHAR || token == terminal.SQUARE_CLOSE) {
            char_set_list();
        }
        else if (token == terminal.CARROT) {
            exclude_set();
        }
        else {
            throwError(token, terminal.CLS_CHAR, terminal.SQUARE_CLOSE, terminal.CARROT);
        }
    }

    private void char_set_list() {
        terminal token = peekToken();
        if (token == terminal.CLS_CHAR) {
            char_set();
            char_set_list();
        }
        else if (token == terminal.SQUARE_CLOSE) {
            matchToken(token);
        }
        else {
            throwError(token, terminal.CLS_CHAR, terminal.SQUARE_CLOSE);
        }
    }

    private void char_set() {
        matchToken(terminal.CLS_CHAR);
        char_set_tail();
    }

    private void char_set_tail() {
        terminal token = peekToken();
        if (token == terminal.DASH) {
            matchToken(token);
            matchToken(terminal.CLS_CHAR);
        }
        else {
            return;
        }
    }

    private void exclude_set() {
        matchToken(terminal.CARROT);
        char_set();
        matchToken(terminal.SQUARE_CLOSE);
        matchToken(terminal.I);
        matchToken(terminal.N);
        exclude_set_tail();
    }

    private void exclude_set_tail() {
        terminal token = peekToken();
        if (token == terminal.SQUARE_OPEN) {
            matchToken(token);
            char_set();
            matchToken(terminal.SQUARE_CLOSE);
        }
        else if (token == terminal.DOLLAR) {
            defined_class();
        }
        else {
            throwError(token, terminal.SQUARE_OPEN, terminal.DOLLAR);
        }
    }

    private void defined_class() {
        matchToken(terminal.DOLLAR);
    }
}
