/**
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 4/9/13
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class Transition {
    CharacterClass transitionOn;
    State nextState;
    boolean emptyString;
    public Transition(CharacterClass characterClass, boolean emptyString) {
        this.transitionOn = characterClass;
        this.emptyString = emptyString;
        this.nextState = new State(false);
    }
    public Transition(CharacterClass characterClass, boolean emptyString, State nextState) {
        this.transitionOn = characterClass;
        this.emptyString = emptyString;
        this.nextState = nextState;
    }
}
