package roguesden.states;

import org.powbot.api.Condition;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Chat;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Npcs;
import roguesden.states.util.Info;

import java.util.List;

public class CrateState implements State {

    /*
    Handles some of crate-opening logic
    A few related interactions are also found in ChatState
    */
    @Override
    public boolean run() {
        if(!Npcs.stream().name("Brian O'Richard").first().reachable()) return false;

        if(Inventory.stream().filter(item -> item.name().contains("Rogue") && !item.name().contains("crate")).count() > 0){
            if(Bank.opened()) {
                Info.sendString("Depositing rogue items");
                Bank.depositInventory();
                List<String> missing = Info.getMissingPieces();
                Info.setMissing(missing);
                Condition.wait(() -> Inventory.stream().count() == 0, 500, 10);

            } else {
                Bank.open();
                Condition.wait(() -> Bank.opened(), 1000, 10);
            }
        } else if(Inventory.stream().name("Rogue's equipment crate").count() > 0) {
            if(Bank.opened()) {
                Bank.close();
                Condition.wait(() -> !Bank.opened(), 500, 10);
            } else {
                Inventory.stream().name("Rogue's equipment crate").first().interact("Search");
                Condition.wait(() -> Chat.stream().text("A piece of Rogue equipment.").isNotEmpty(), 500, 10);
            }
        } else {
            return false;
        }

        return true;
    }
}
