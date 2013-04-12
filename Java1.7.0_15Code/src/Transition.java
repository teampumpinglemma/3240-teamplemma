/**
 * The building blocks of NFAs
 *
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 4/9/13
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class Transition {
    // the characters that cause this transition
    CharacterClass transitionOn;
    // is this an empty string transition
    boolean emptyString;
    // the state from which to transition
    State currentState;
    // the state to which to transition
    State nextState;

    /**
     * Constructor creates a new destination state if not given an existing state
     * @param currentState
     * @param characterClass
     * @param emptyString
     */
    public Transition(State currentState, CharacterClass characterClass, boolean emptyString) {
        this.currentState = currentState;
        this.transitionOn = characterClass;
        this.emptyString = emptyString;
        this.nextState = new State();
    }

    /**
     * Constructor sets nextState to an existing state
     * @param currentState
     * @param characterClass
     * @param emptyString
     * @param nextState
     */
    public Transition(State currentState, CharacterClass characterClass, boolean emptyString, State nextState) {
        this.currentState = currentState;
        this.transitionOn = characterClass;
        this.emptyString = emptyString;
        this.nextState = nextState;
    }
}
