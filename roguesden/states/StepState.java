package roguesden.states;

import org.powbot.api.Condition;
import org.powbot.api.Tile;
import org.powbot.api.rt4.Players;
import roguesden.states.util.Info;

import static org.powbot.api.rt4.Movement.step;

/*
    Go to the nearest tile if misclicked
 */

public class StepState implements State {
    private Tile getClosestTile(Tile loc) {
        Tile [] path = Info.getPathTiles();

        double dist = Integer.MAX_VALUE;

        Tile closest = path[0];

        for(int i = 0; i < path.length; i++) {
            if(path[i] != null && loc.distanceTo(path[i]) < dist && path[i].reachable()) {
                closest = path[i];
                dist = loc.distanceTo(path[i]);
            }
        }

        Info.sendString(closest + " has lowest dist=" + dist);
        return closest;
    }

    @Override
    public boolean run() {
        Tile c = getClosestTile(Players.local().tile());
        step(c);
        Condition.wait(() -> Players.local().tile().equals(c), 500, 10);

        return false;
    }
}
