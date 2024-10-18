package pokemons;

import attacks.*;
import ru.ifmo.se.pokemon.*;

public class Chimchar extends Pokemon {
    public Chimchar(String name, int lvl) {
        super(name, lvl);
        setStats(44, 58, 44, 58, 44, 61);
        setType(Type.FIRE);
        setMove(new Rest(), new LowSweep());
    }
}
