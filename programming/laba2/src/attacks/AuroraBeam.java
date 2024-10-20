package attacks;

import ru.ifmo.se.pokemon.*;

public class AuroraBeam extends SpecialMove {
    public AuroraBeam() {super(Type.ICE, 65, 1);}

    protected int count = 0;
    protected boolean isDecreased = false;
    @Override
    protected void applyOppEffects(Pokemon p) {
        if (Math.random() <= 0.1 && count < 6) {
            count++;
            isDecreased = true;
            p.setMod(Stat.ATTACK, -1);
        }
    }

    @Override
    protected String describe(){
        if (isDecreased) {
            return "uses Aurora Beam and enemy attack decreased";
        }
        return "uses Aurora Beam, enemy attack not decreased";
    }


}
