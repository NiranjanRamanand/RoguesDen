package roguesden.states;

import org.powbot.api.Condition;
import org.powbot.api.rt4.Chat;
import org.powbot.api.rt4.Npcs;
import roguesden.states.util.Info;

import java.util.List;

/*
    Handle level-up, dialogue boxes
 */

public class ChatState implements State {

    @Override
    public boolean run() {
        if(Chat.canContinue()) {
            Chat.clickContinue();
        } else if(Npcs.stream().name("Brian O'Richard").first().reachable()) {
            if (Chat.stream().text("Trousers.").isNotEmpty()) {
                List<String> missing = Info.getMissing();
                String missingItem = Info.getMissing().get(missing.size() - 1);


                Chat.stream().text(missingItem).first().select();
                Condition.wait(() -> false, 500, 10);
            } else if (Chat.stream().text("A piece of Rogue equipment.").isNotEmpty()) {
                Chat.stream().text("A piece of Rogue equipment.").first().select();
                Condition.wait(() -> Chat.canContinue(), 500, 10);
            } else {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }
}
