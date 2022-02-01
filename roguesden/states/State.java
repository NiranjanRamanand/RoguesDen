package roguesden.states;

public interface State {

    boolean run(); // true -> state was valid, false -> move onto checking next state

}