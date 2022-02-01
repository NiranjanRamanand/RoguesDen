package states;

import org.powbot.api.Condition;
import org.powbot.api.rt4.*;
import states.util.Info;

import static org.powbot.api.rt4.Movement.step;

/*
    Sets up player in between runs
    (Drinks staminas or waits for run energy)
 */

public class SetupState implements State {

    private final int [] staminaPots = {12625, 12627, 12629, 12631};
    private final int doorId = 7256;
    private final int energyThreshold = 70;

    @Override
    public boolean run() {
        if(!Npcs.stream().name("Brian O'Richard").first().reachable()) return false;


        if(Movement.energyLevel() > energyThreshold && !Inventory.isEmpty()) {
            Info.sendString("Depositing inventory");
            if(Bank.opened()) {
                Bank.depositInventory();
                Condition.wait(() -> Inventory.stream().count() == 0, 500, 10);
            } else {
                Bank.open();
                Condition.wait(() -> Bank.opened(), 500, 10);
            }
        } else if (Movement.energyLevel() > energyThreshold) { // Do boosting after first code thru
            if(Objects.stream().id(doorId).nearest().first().tile().distanceTo(Players.local().tile()) < 5) {
                Info.sendString("Clicking door");
                Objects.stream().id(doorId).first().click();
                Condition.wait(() -> !Npcs.stream().name("Brian O'Richard").first().reachable(), 500, 10);
            } else {
                Info.sendString("Moving to door");
                step(Objects.stream().id(doorId).nearest().first().tile());
                Condition.wait(() -> Objects.stream().id(doorId).nearest().first().tile().distanceTo(Players.local().tile()) < 5, 500, 3);
            }
        } else if (Movement.energyLevel() <= energyThreshold){
            Info.sendString("Restoring run");
            if(Inventory.stream().id(staminaPots).count() > 0) {
                if(!Bank.opened()) {
                    final int energy = Movement.energyLevel();
                    Inventory.stream().id(staminaPots).first().click();
                    Condition.wait(() -> Movement.energyLevel() < energy + 20, 500, 10);
                } else {
                    Bank.close();
                    Condition.wait(() -> !Bank.opened(), 500, 10);
                }
            } else {
                if(!Bank.opened()) {
                    Bank.open();
                    Condition.wait(() -> Bank.opened(), 500, 10);
                } else {
                    Bank.withdraw(Bank.stream().id(staminaPots).first().id(), Bank.Amount.ALL);
                    Condition.wait(() -> !Bank.opened(), 500, 10);
                }
            }
        } else {
            return false;
        }

        return true;
    }
}
