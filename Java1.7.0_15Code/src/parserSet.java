import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: emilyCheatham
 * Date: 4/27/13
 * Time: 5:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class parserSet {
    String nonTerminal;
    ArrayList<String> set;

    public parserSet(String name) {
        nonTerminal = name;
        set = new ArrayList<String>();
    }
}
