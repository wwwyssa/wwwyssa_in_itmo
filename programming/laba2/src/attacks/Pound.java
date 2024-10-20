package attacks;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Type;

public class Pound extends PhysicalMove {
    public Pound() {super(Type.NORMAL, 40, 1);}

    @Override
    protected String describe() {
        return "uses Pound";
    }

}
