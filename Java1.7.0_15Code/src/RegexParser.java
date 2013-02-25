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

    }

    private enum terminal {UNION}

    private terminal peekToken() {
        return terminal.UNION;
    }

    private void matchToken(terminal token) {

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

    }

    private void rexp2() {

    }

    private void rexp2_tail() {

    }

    private void rexp3() {

    }

    private void char_class() {

    }

    private void char_class1() {

    }

    private void char_set_list() {

    }

    private void char_set() {

    }

    private void char_set_tail() {

    }

    private void exclude_set() {

    }

    private void exclude_set_tail() {

    }
}
