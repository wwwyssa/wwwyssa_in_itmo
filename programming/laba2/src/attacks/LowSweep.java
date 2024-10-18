package attacks;

import ru.ifmo.se.pokemon.*;

public class LowSweep extends PhysicalMove {
    public LowSweep() {super(Type.FIGHTING, 65, 100);}
    private int count = 0;

    @Override
    protected void applyOppEffects(Pokemon p) {
        if (count < 6) {
            count++;
            p.setMod(Stat.SPEED, -1);
        }
    }

    @Override
    public String describe() {
        return "uses Low Sweep";
    }
}
