package roguesden.states;

import org.powbot.api.Condition;
import org.powbot.api.Tile;
import org.powbot.api.rt4.*;
import roguesden.states.util.Info;

import static org.powbot.api.rt4.Movement.step;

public class NavigateState implements State {
    private final int flashPowder = 5559;
    private final int gateId = 7255;

    @Override
    public boolean run() {
        Tile [] path = Info.getPathTiles();
        int [] obstacles = Info.getObjects();

        // Info.sendString("reachable :" + Npcs.stream().name("Brian O'Richard").first().reachable());
        // End of maze
        if (Players.local().tile().equals(new Tile(3018, 5047, 1))) {
            Objects.stream().name("Wall safe").nearest().first().interact("Crack");

            Condition.wait(() -> Npcs.stream().name("Brian O'Richard").first().reachable(), 50, 100);
        } else if(!Npcs.stream().name("Brian O'Richard").first().reachable()) {
            // Navigate through maze
            Tile t = Players.local().tile();

            // Find the current position in order to find the next position
            for (int i = 0; i < path.length - 1; i++) {
                if (t.equals(path[i])) {

                    // Obstacle exception: requires id AND tile
                    if (t.equals(new Tile(2989, 5057, 1))) {
                        GameObject gate = Objects.stream().
                                filter(o -> o.id() == gateId && o.tile(). equals(new Tile(2989, 5057, 1))).
                                first();

                        gate.click();
                        Condition.wait(() -> false, 500, 10);

                        return true;
                    }

                    // Another exception: requires coordination and timing to stun guards
                    if (t.equals(new Tile(3009, 5063, 1))) {
                        Info.sendString("Stunning guards");

                        if (Inventory.stream().id(flashPowder).count() > 0) {
                            if (Inventory.selectedItem().id() == flashPowder) {
                                Npc guard = Npcs.stream().id(3191).first();

                                if (guard.valid()) {
                                    if (guard.click()) {
                                        Condition.wait(() -> false, 500, 2);
                                        step(new Tile(3029, 5056, 1));
                                        Condition.wait(() -> Players.local().tile().equals(new Tile(3004, 5088, 1)), 500, 9);
                                    }
                                }
                            } else {
                                Inventory.stream().id(flashPowder).first().interact("Use");
                            }
                        } else {
                            Info.sendString("Picking up flash powder");
                            GroundItem g = GroundItems.stream().filter(o -> o.id() == flashPowder && o.tile().equals(new Tile(3009, 5063, 1))).first();

                            g.click();
                            Condition.wait(() -> Inventory.stream().id(flashPowder).count() > 0, 500, 10);

                        }

                        return true;
                    }


                   // General structure: if the next tile is not null, there is no intermediate obstacle
                   // Otherwise, there is one
                   if (path[i + 1] == null) {
                        Info.sendString("Clicking object: " + obstacles[i + 1]);
                        Objects.stream().id(obstacles[i + 1]).nearest().first().click();
                        Info.sendString(Objects.stream().id(obstacles[i + 1]).first().name());
                        Condition.wait(() -> false, 5000, 1); // todo: make this conditional
                    } else {
                        Info.sendString("Stepping");
                        step(path[i + 1]);
                        final int idx = i;
                        Condition.wait(() -> Players.local().tile().equals(path[idx + 1]), 500, 10);
                    }

                   return true;
                }
            }

            return false;
        }

        return true;
    }
}
