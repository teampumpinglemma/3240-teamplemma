/**
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 4/9/13
 * Time: 4:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class NFA {
    State start;
    State accept;
    public NFA() {
        this.start = new State(true);
        this.accept = this.start;
    }
}
