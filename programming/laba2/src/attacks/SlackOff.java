package attacks;

import ru.ifmo.se.pokemon.*;

public class SlackOff extends SpecialMove {
    public SlackOff() {super(Type.NORMAL, 0, 0);}

    @Override
    protected void applySelfEffects(Pokemon p) {
        double stat = p.getStat(Stat.HP);
        p.setMod(Stat.HP, (int) (stat + stat * 0.5));
    }

    @Override
    public String describe() {
        return "uses Slack off";
    }
}
