import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: emilyCheatham
 * Date: 4/26/13
 * Time: 4:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class grammarRules {
    String identifier;
    ArrayList<String> rulesList;

    public grammarRules(String name) {
        identifier = name;
        rulesList = new ArrayList<String>();
    }
}
