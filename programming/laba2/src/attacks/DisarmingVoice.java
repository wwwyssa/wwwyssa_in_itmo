package attacks;

import ru.ifmo.se.pokemon.*;

public class DisarmingVoice extends SpecialMove {
    public DisarmingVoice() {super(Type.FAIRY, 40, 1);}


    @Override
    protected boolean checkAccuracy(Pokemon p1, Pokemon p2){
        return true;
    }

    @Override
    protected String describe() {
        return "uses Disarming Voice";
    }
}
