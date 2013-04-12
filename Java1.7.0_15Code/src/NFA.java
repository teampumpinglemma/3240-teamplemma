import java.util.ArrayList;

/**
 * An NFA...
 *
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 4/9/13
 * Time: 4:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class NFA {
    // the start state of the NFA
    State start;
    // the accept state of the NFA
    State accept;
    // all the states of the NFA
    ArrayList<State> states;
    // all the transitions of the NFA
    ArrayList<Transition> transitions;

    /**
     * Constructor automatically creates one state that is the start and accept state
     */
    public NFA() {
        this.start = new State();
        this.accept = this.start;
        this.states = new ArrayList<State>();
        this.states.add(this.start);
        this.transitions = new ArrayList<Transition>();
    }
}
