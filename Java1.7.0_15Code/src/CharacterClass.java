/**
 * The CharacterClass class has a boolean array for keeping track of which ASCII printable characters are included in the class,
 * and has methods for easily manipulating the array in useful ways
 *
 * Created with IntelliJ IDEA.
 * User: Mickey
 * Date: 4/7/13
 * Time: 9:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class CharacterClass {
    boolean[] accepted;
    String name;
    public CharacterClass(String name) {
        this.name = name;
        // 95 is the number of ASCII printable characters
        accepted = new boolean[95];
    }

    /**
     * Adds a single character to the character class
     * @param character :the ASCII value of the character to add
     */
    public void setToTrue(int character) {
        accepted[character - 32] = true;
    }

    /**
     * Adds a range of characters to the character class
     * @param from :the ASCII value of the first character in the range to add
     * @param to :the ASCII value of the last character in the range to add
     */
    public void setToTrue(int from, int to) {
        if (from > to) {
            System.out.println("Empty range found while building a character class");
            System.exit(0);
        }
        for (int i = from; i <= to; i++) {
            accepted[i - 32] = true;
        }
    }

    /**
     * Adds all the characters of "in" to the character class, except for those in "not"
     * @param not :the characters in "in" to exclude
     * @param in :the characters to add
     */
    public void notBlankInBlank(CharacterClass not, CharacterClass in) {
        for (int i = 0; i < accepted.length; i++) {
            if (!not.accepted[i]) {
                accepted[i] = in.accepted[i];
            }
        }
    }

    /**
     * Builds an NFA of two states and a single transition on the characters in this character class from the start state to the accept state
     * @return NFA representing this character class
     */
    public NFA createNFA() {
        NFA nfa = new NFA();
        nfa.transitions.add(new Transition(nfa.start, this, false));
        nfa.states.add(nfa.transitions.get(0).nextState);
        nfa.accept = nfa.transitions.get(0).nextState;
        return nfa;
    }
}
