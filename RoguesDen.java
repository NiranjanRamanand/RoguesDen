import org.powbot.api.script.AbstractScript;
import org.powbot.api.script.ScriptManifest;
import org.powbot.mobile.script.ScriptManager;
import org.powbot.mobile.service.ScriptUploader;
import states.*;
import states.util.Info;

import java.util.*;

@ScriptManifest(
        name = "Rogue's Den",
        description = "Completes the minigame",
        version = "1.0.0"
)

public class RoguesDen extends AbstractScript {

    public static void main(String[] args) {
        new ScriptUploader().uploadAndStart("Rogue's Den", "acc 1", "localhost:5555", true, true);
    }

    ArrayList<State> states = new ArrayList<>();

    @Override
    public void onStart() {
        states.add(new MotionState());
        states.add(new UpdateState());
        states.add(new ChatState());
        states.add(new CrateState());
        states.add(new SetupState());
        states.add(new NavigateState());
        states.add(new StepState());
    }

    @Override
    public void poll() {
        if(Info.getMissing() != null && Info.getMissing().isEmpty()) {
            ScriptManager.INSTANCE.stop();
        }

        for(State s : states) {
            if(s.run()) {
                return;
            }
        }
    }

}
