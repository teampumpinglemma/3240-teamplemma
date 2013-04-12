import java.util.ArrayList;
import java.util.LinkedList;

/**
 * The DFA Table, built from the Giant NFA.  Class also contains methods for walking through the table.
 *
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 4/10/13
 * Time: 8:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class DFATable {
    // the table, stored as a list of rows (which represent DFA states)
    ArrayList<DFATableRow> tableRows;
    GiantNFA nfa;
    // the current state while walking the table
    DFATableRow currentDFAState;
    // for matching an accept state with its token
    ArrayList<Definition> tokens;

    /**
     * Builds the table
     * @param nfa :from which to construct the table
     * @param tokens :tokens to return upon the accept states
     */
    public DFATable(GiantNFA nfa, ArrayList<Definition> tokens) {
        tableRows = new ArrayList<DFATableRow>();
        this.nfa = nfa;
        this.tokens = tokens;
        ArrayList<State> start = new ArrayList<State>();
        start.add(nfa.start);
        tableRows.add(new DFATableRow(eClosure(start)));
        LinkedList<DFATableRow> unfinished = new LinkedList<DFATableRow>();
        unfinished.add(tableRows.get(0));
        // while there are still states without their transitions set
        while (!unfinished.isEmpty()) {
            // set the transitions for the next unfinished state
            DFATableRow current = unfinished.remove();
            for (int j = 0; j < 95; j++) {
                ArrayList<State> next = eClosure(move(current.nfaStates, j));
                boolean completeMatch = false;
                for (int k = 0; k < tableRows.size(); k++) {
                    completeMatch = true;
                    if (tableRows.get(k).nfaStates.size() == next.size()) {
                        for (int l = 0; l < tableRows.get(k).nfaStates.size(); l++) {
                            boolean oneMatch = false;
                            for (int m = 0; m < next.size(); m++) {
                                if (tableRows.get(k).nfaStates.get(l).equals(next.get(m))) {
                                    oneMatch = true;
                                    break;
                                }
                            }
                            if (!oneMatch) {
                                completeMatch = false;
                                break;
                            }
                        }
                    }
                    else {
                        completeMatch = false;
                    }
                    if (completeMatch) {
                        current.nextStates[j] = k;
                        break;
                    }
                }
                // if the transition results in a brand new state
                if (!completeMatch) {
                    // set the transition to that new state
                    current.nextStates[j] = tableRows.size();
                    // add the row for that new state to the table
                    tableRows.add(new DFATableRow(next));
                    // add that new state to the list of states without their transitions set
                    unfinished.add(tableRows.get(tableRows.size() - 1));
                }
            }
        }
        // set the start state of the table
        currentDFAState = tableRows.get(0);
    }

    /**
     * Take one step through the table, setting the current state to the result of a transition on the read character
     * @param character :the character on which to transition
     */
    public void progress(int character) {
        currentDFAState = tableRows.get(currentDFAState.nextStates[character - 32]);
    }

    /**
     * See if the current state is an accept state
     * @return :the token corresponding with the accept state, or null if it is not an accept state
     */
    public Definition tryAccept() {
        for (int i = 0; i < currentDFAState.nfaStates.size(); i++) {
            int j = nfa.acceptStates.indexOf(currentDFAState.nfaStates.get(i));
            if (j != -1) {
                return tokens.get(j);
            }
        }
        return null;
    }

    /**
     *
     * @param inputStates :the states from which to expand on the empty string transitions
     * @return :all the states reachable from the input states by transitioning on the empty string (including the input states)
     */
    private ArrayList<State> eClosure(ArrayList<State> inputStates) {
        ArrayList<State> eClosure = new ArrayList<State>();
        LinkedList<State> investigate = new LinkedList<State>();
        for (int i = 0; i < inputStates.size(); i++) {
            eClosure.add(inputStates.get(i));
            investigate.add(inputStates.get(i));
        }
        while (!investigate.isEmpty()) {
            State investigatee = investigate.remove();
            for (int i = 0; i < nfa.transitions.size(); i++) {
                if (nfa.transitions.get(i).currentState.equals(investigatee) && nfa.transitions.get(i).emptyString && !eClosure.contains(nfa.transitions.get(i).nextState)) {
                    eClosure.add(nfa.transitions.get(i).nextState);
                    investigate.add(nfa.transitions.get(i).nextState);
                }
            }
        }
        return eClosure;
    }

    /**
     *
     * @param inputStates :the states from which to attempt to transition on the character
     * @param character :the character on which to transition
     * @return :all the states reachable from the input states after a single transition on the character
     */
    private ArrayList<State> move(ArrayList<State> inputStates, int character) {
        ArrayList<State> toReturn = new ArrayList<State>();
        for (int i = 0; i < inputStates.size(); i++) {
            for (int j = 0; j < nfa.transitions.size(); j++) {
                if (nfa.transitions.get(j).currentState.equals(inputStates.get(i)) && (nfa.transitions.get(j).transitionOn != null) && nfa.transitions.get(j).transitionOn.accepted[character] && !toReturn.contains(nfa.transitions.get(j).nextState)) {
                    toReturn.add(nfa.transitions.get(j).nextState);
                }
            }
        }
        return toReturn;
    }


}
