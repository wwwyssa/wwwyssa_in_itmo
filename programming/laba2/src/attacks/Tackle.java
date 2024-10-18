package attacks;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Type;

public class Tackle extends PhysicalMove {
    public Tackle() {super(Type.NORMAL, 40, 100);}

    @Override
    protected String describe() {
        return "uses Tackle";
    }
}
