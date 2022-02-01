package roguesden.states;

import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Npcs;
import org.powbot.api.rt4.Objects;
import org.powbot.api.rt4.Players;
import roguesden.states.util.Info;

import java.util.List;

import static org.powbot.api.rt4.Movement.step;

/*
    Check what items player has to detect what more is needed
 */

public class UpdateState implements State {

    @Override
    public boolean run() {
        if(!Npcs.stream().name("Brian O'Richard").first().reachable()) return false;


        if (!Info.isUpdated()) {
            // Check which outfit items remain
            if (Bank.opened()) {
                List<String> missing = Info.getMissingPieces();
                Info.setMissing(missing);

                Info.sendString("====================");

                for (String s : missing) {
                    Info.sendString("Required: " + s);
                }

                Info.sendString("====================");

                Info.update();
            } else {
                Info.sendString("Opening bank to update");
                if (Objects.stream().
                        name("Bank chest").
                        first().
                        tile().
                        distanceTo(Players.local().tile()) < 5) {
                    Bank.open();
                    Condition.wait(() -> Bank.opened(), 500, 5);
                } else {
                    step(Objects.stream().name("Bank chest").first().tile());
                    Condition.wait(() -> Objects.stream().name("Bank chest").first().tile().distanceTo(Players.local().tile()) < 5, 250, 5);
                }
            }
        } else {
            return false;
        }

        return true;
    }
}
