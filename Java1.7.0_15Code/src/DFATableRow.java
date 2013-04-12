import java.util.ArrayList;

/**
 * One state in the DFA
 *
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 4/10/13
 * Time: 8:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class DFATableRow {
    // the NFA states represented by this DFA state
    ArrayList<State> nfaStates;
    // array of 95 states, each the result of transitioning on the ASCII character with a value of that index + 32, since this will cover the entire range of ASCII printable characters
    int[] nextStates;
    public DFATableRow(ArrayList<State> nfaStates) {
        nextStates = new int[95];
        this.nfaStates = nfaStates;
    }
}
