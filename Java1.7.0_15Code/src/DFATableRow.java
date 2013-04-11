import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 4/10/13
 * Time: 8:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class DFATableRow {
    ArrayList<State> nfaStates;
    int[] nextStates;
    public DFATableRow(ArrayList<State> nfaStates) {
        nextStates = new int[95];
        this.nfaStates = nfaStates;
    }
}
