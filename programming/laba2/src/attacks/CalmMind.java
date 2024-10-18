package attacks;

import ru.ifmo.se.pokemon.*;

public class CalmMind extends StatusMove {
    public CalmMind() {super(Type.PSYCHIC, 0, 0);}
    private int count = 0;
    @Override
    protected void applySelfEffects(Pokemon p) {
        if (count < 6) {
            p.setMod(Stat.SPECIAL_DEFENSE, 1);
            p.setMod(Stat.SPECIAL_ATTACK, 1);
            count++;
        }
    }
    @Override
    protected String describe() {
        if (count <= 6) {
            return "used Calm Mind, SPECIAL_DEFENSE and SPECIAL_ATTACK raise 1";
        }
        return "used Calm Mind, but nothing changed";
    }
}
