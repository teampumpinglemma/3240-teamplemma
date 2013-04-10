/**
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 4/9/13
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class Transition {
    CharacterClass transitionOn;
    boolean emptyString;
    State currentState;
    State nextState;
    public Transition(State currentState, CharacterClass characterClass, boolean emptyString) {
        this.currentState = currentState;
        this.transitionOn = characterClass;
        this.emptyString = emptyString;
        this.nextState = new State();
    }
    public Transition(State currentState, CharacterClass characterClass, boolean emptyString, State nextState) {
        this.currentState = currentState;
        this.transitionOn = characterClass;
        this.emptyString = emptyString;
        this.nextState = nextState;
    }
}
