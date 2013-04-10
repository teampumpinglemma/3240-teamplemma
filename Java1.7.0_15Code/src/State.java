import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 4/9/13
 * Time: 3:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class State {
    ArrayList<Transition> transitionsFrom;
    boolean isAccept;
    public State(boolean isAccept) {
        this.transitionsFrom = new ArrayList<Transition>();
        this.isAccept = isAccept;
    }
}
