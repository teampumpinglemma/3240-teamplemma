/**
 * Stores the tokens matched in the Spec file along with the characters that caused them
 *
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 3/31/13
 * Time: 5:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class TokenWithCharacters {
    SpecReader.terminal token;
    String characters;

    public TokenWithCharacters(SpecReader.terminal token, String characters) {
        this.token = token;
        this.characters = characters;
    }
}
