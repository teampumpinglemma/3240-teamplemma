import java.util.ArrayList;

/**
 * The union of all the NFAs built from the token definitions
 *
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 4/11/13
 * Time: 5:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class GiantNFA {
    // the start state of the NFA
    State start;
    // all the states in the NFA
    ArrayList<State> states;
    // all the transitions in the NFA
    ArrayList<Transition> transitions;
    // the accept states of the NFA (one for each token definition)
    ArrayList<State> acceptStates;

    /**
     * Constructor unions all the token NFAs
     * @param nfas :the token NFAs
     */
    public GiantNFA(ArrayList<NFA> nfas) {
        this.start = new State();
        this.states = new ArrayList<State>();
        this.states.add(this.start);
        this.transitions = new ArrayList<Transition>();
        this.acceptStates = new ArrayList<State>();
        for (int i = 0; i < nfas.size(); i++) {
            this.transitions.add(new Transition(this.start, null, true, nfas.get(i).start));
            for (int j = 0; j < nfas.get(i).states.size(); j++) {
                this.states.add(nfas.get(i).states.get(j));
            }
            for (int j = 0; j < nfas.get(i).transitions.size(); j++) {
                this.transitions.add(nfas.get(i).transitions.get(j));
            }
            this.acceptStates.add(nfas.get(i).accept);
        }
    }
}
