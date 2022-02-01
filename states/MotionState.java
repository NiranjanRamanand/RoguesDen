package states;

import org.powbot.api.Condition;
import org.powbot.api.rt4.Movement;
import org.powbot.api.rt4.Players;
import states.util.Info;

/*
    Enables run
 */

public class MotionState implements State {

    @Override
    public boolean run() {
        if(Players.local().inMotion()) {
            Condition.wait(() -> !Players.local().inMotion(), 100, 5);
        } else if(!Movement.running()) {
            Movement.running(true);
            Condition.wait(() -> Movement.running(), 500, 5);
        } else {
            return false;
        }

        return true;
    }
}
