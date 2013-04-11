import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 4/11/13
 * Time: 5:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class GiantNFA {
    State start;
    ArrayList<State> states;
    ArrayList<Transition> transitions;
    ArrayList<State> acceptStates;
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
