package attacks;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Type;

public class QuickAttack extends PhysicalMove {
    public QuickAttack(){
        super(Type.NORMAL, 40, 100);
        priority++;
    }

    @Override
    protected String describe() {
        return "uses Quick Attack";
    }
}
