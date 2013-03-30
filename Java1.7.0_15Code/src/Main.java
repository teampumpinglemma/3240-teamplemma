import java.io.File;

/**
 * The Main class runs everything
 *
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 2/24/13
 * Time: 4:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    /**
     * The main method... creates a RegexParser, and then parses the Spec file
     *
     * @param args :pass in the Spec file... to do this in IntelliJ, go to Run -> Edit Configurations... and then put the path to the file in "Program arguments"
     */
    public static void main(String[] args) {
        RegexParser regexParser = new RegexParser(new File(args[0]));
        regexParser.parse();
    }
}
