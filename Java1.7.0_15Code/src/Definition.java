import java.util.ArrayList;

/**
 * Used for storing what the recursive descent parser parses: the name for what is being defined, and the tokens that make up the definition
 *
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 3/30/13
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class Definition {
    String name;
    ArrayList<TokenWithCharacters> tokens;

    /**
     * Constructor
     * @param name :the name for what is being defined
     */
    public Definition(String name) {
        this.name = name;
        tokens = new ArrayList<TokenWithCharacters>();
    }
}
