import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 4/10/13
 * Time: 8:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class DFATable {
    ArrayList<DFATableRow> tableRows;
    NFA nfa;

    public DFATable(NFA nfa) {
        tableRows = new ArrayList<DFATableRow>();
        this.nfa = nfa;
        ArrayList<State> start = new ArrayList<State>();
        start.add(nfa.start);
        tableRows.add(new DFATableRow(eClosure(start)));
        LinkedList<DFATableRow> unfinished = new LinkedList<DFATableRow>();
        unfinished.add(tableRows.get(0));
        while (!unfinished.isEmpty()) {
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
                if (!completeMatch) {
                    current.nextStates[j] = tableRows.size();
                    tableRows.add(new DFATableRow(next));
                    unfinished.add(tableRows.get(tableRows.size() - 1));
                }
            }
        }
        //TODO accept states
    }

    public ArrayList<State> eClosure(ArrayList<State> inputStates) {
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

    public ArrayList<State> move(ArrayList<State> inputStates, int character) {
        ArrayList<State> toReturn = new ArrayList<State>();
        for (int i = 0; i < inputStates.size(); i++) {
            for (int j = 0; j < nfa.transitions.size(); j++) {
                if (nfa.transitions.get(j).currentState.equals(inputStates.get(i)) && nfa.transitions.get(j).transitionOn.accepted[character - 32] && !toReturn.contains(nfa.transitions.get(j).nextState)) {
                    toReturn.add(nfa.transitions.get(j).nextState);
                }
            }
        }
        return toReturn;
    }
}
