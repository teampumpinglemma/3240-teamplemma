import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 3/30/13
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class Definition {
    String name;
    ArrayList<TokenWithCharacters> tokens;

    public Definition(String name) {
        this.name = name;
        tokens = new ArrayList<TokenWithCharacters>();
    }
}
